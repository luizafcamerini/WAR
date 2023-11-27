package Controller;

import View.ViewAPI;
import Model.ModelAPI;
import java.util.Hashtable;

public class ControllerAPI {
	private static Hashtable<String, Integer> qtdDeslocaveis;
	private static Hashtable<String, Integer> qtdDeslocados;
	private static ControllerAPI instance;

	private final String[] coresStr = { "AMARELO", "AZUL", "BRANCO", "PRETO", "VERDE", "VERMELHO" };
	private final String pathAuto = "src/autoSave.txt";
	private final boolean DEBUG = false;

	private ModelAPI model;
	private ViewAPI view;

	private int etapa = 0;
	private int corAtual;
	private int qtdExeAd;
	private String[] continentes;
	private int iCont = -1;
	private boolean conquista = false;

	private ControllerAPI() {
	}

	public static ControllerAPI getInstance() {
		/** Funcao que retorna a unica instancia de ControllerAPI. */
		if (instance == null) {
			instance = new ControllerAPI();
		}
		return instance;
	}

	public void inicializa() {
		/**
		 * Funcao que cria as instancias de ModelAPI, ViewAPI, inicializa a tela de jogo
		 * e executa a primeira etapa.
		 */
		instance.model = ModelAPI.getInstance();
		instance.view = ViewAPI.getInstance();
		continentes = model.getContinentes();

		view.inicializaGameScreen();

		etapa = 0;

		proxEtapa();
	}

	public void proxEtapa() {
		/** Funcao que executa/passa para a proxima etapa. */
		corAtual = model.getCorAtual();
		if (corAtual == -1)
			return;

		if (DEBUG)
			System.out.println(coresStr[corAtual]);

		String territorios[] = model.getTerritorios(corAtual);

		if (DEBUG)
			System.out.printf("Da etapa %d\n", etapa);

		// Posicionamento de exércitos
		if (etapa == 0) {
			// Verifica há exércitos extras para colocar em cada continente
			while (qtdExeAd == 0 && iCont < continentes.length - 1) {
				iCont++;
				qtdExeAd = model.getExeAdContinente(continentes[iCont]);
				if (DEBUG)
					System.out.printf("%s %d\n", continentes[iCont], qtdExeAd);
				if (qtdExeAd > 0)
					territorios = model.getTerritoriosContinente(continentes[iCont]);
			}
			if (qtdExeAd == 0) {
				qtdExeAd = model.getExeAd();
				iCont = -1;
			}
			if (iCont == -1 && model.getCartasJogador().length == 5) {
				view.obrigaTroca();
			}
			view.setEtapa(etapa, territorios, corAtual, qtdExeAd);
		}

		// Ataque
		else if (etapa == 10) {
			if (qtdExeAd > 0)
				return;
			view.setEtapa(etapa, territorios, corAtual, 0);
			etapa = 20;
		}

		// Deslocamento de exércitos
		else if (etapa == 20) {

			qtdDeslocaveis = new Hashtable<String, Integer>();
			qtdDeslocados = new Hashtable<String, Integer>();
			for (String nome : territorios) {

				// Salva quantos exércitos cada território pode doar
				qtdDeslocaveis.put(nome, model.getQtdExercitos(nome) - 1);

				// Salva quantos exércitos cada um doou para cada um
				for (String nome2 : territorios) {
					if (nome != nome2) {
						qtdDeslocados.put(nome + "-" + nome2, 0);
					}
				}
			}

			view.setEtapa(etapa, territorios, corAtual, 0);
			etapa = 30;
		}

		// Entrega de carta
		else if (etapa == 30) {
			if (conquista) {
				model.entregaCarta();
				conquista = false;
			}
			view.setEtapa(etapa, null, corAtual, 0);
			etapa = 40;
		}

		// Passa a vez para o próximo jogador
		else if (etapa == 40) {
			model.getProxCor();
			etapa = 0;
			proxEtapa();
		}

		saveState(pathAuto);
		verificaObjetivo();

		if (DEBUG)
			System.out.printf("Para a etapa %d\n", etapa);
	}

	public void saveState(String path) {
		/** Funcao que salva o jogo dado um path para o arquivo txt. */
		model.saveState(path);
	}

	public String getContinenteAtual() {
		/** Funcao que retorna o continente que o jogador atual posiciona exercitos. */
		if (iCont < 0)
			return null;
		return continentes[iCont];
	}

	public void addExe(String territorio) {
		/**
		 * Funcao que incrementa exercitos em um territorio dado o seu nome e passa a
		 * etapa.
		 */
		model.addExe(territorio, 1);
		qtdExeAd--;
		if (qtdExeAd == 0) {
			if (iCont == -1)
				etapa = 10;
			proxEtapa();
		}
		saveState(pathAuto);
		verificaObjetivo();
	}

	public boolean ataca(String atacante, String defensor) {
		/** Funcao que verifica se a tela de ataque será exibida. */
		if (!model.verificaCondicoesAtaque(atacante, defensor)) {
			System.out.println("Não foi possível atacar");
			return false;
		}

		int nDadosAtaque, nDadosDefesa, tmp;

		tmp = model.getQtdExercitos(atacante);
		nDadosAtaque = tmp > 3 ? 3 : tmp - 1;

		tmp = model.getQtdExercitos(defensor);
		nDadosDefesa = tmp > 3 ? 3 : tmp;

		view.ataca(atacante, defensor, nDadosAtaque, nDadosDefesa);
		return true;
	}

	public void ataque(String atacante, String defensor) {
		/** Funcao que realiza um ataque automatico. */
		int[][] dados = model.ataque(atacante, defensor);
		int corAtual = model.getCorAtual();

		// Conquistou o territorio:
		if (model.getCor(defensor) == corAtual) {
			conquista = true;
		}

		view.resultadoAtaque(dados);
		saveState(pathAuto);
		verificaObjetivo();
	}

	public void ataque(String atacante, String defensor, int[][] dados) {
		/** Funcao que realiza um ataque manual. */
		model.ataque(atacante, defensor, dados);
		int corAtual = model.getCorAtual();

		// Conquistou o territorio:
		if (model.getCor(defensor) == corAtual) {
			conquista = true;
		}
		view.resultadoAtaque(dados);
		saveState(pathAuto);
		verificaObjetivo();
	}

	public void movePosConquista(String atacante, String defensor, String territorioClicado) {
		/**
		 * Funcao que move um exercito por vez após a conquista de um territorio. Usada
		 * na ViewAPI.
		 */
		int qtdAtacante = model.getQtdExercitos(atacante);
		int qtdDefensor = model.getQtdExercitos(defensor);

		if (atacante == territorioClicado && qtdDefensor > 1) {
			model.reduzExe(defensor, 1);
			model.addExe(atacante, 1);
			saveState(pathAuto);
			verificaObjetivo();
		} else if (defensor == territorioClicado && qtdDefensor < 3 && qtdAtacante > 1) {
			model.reduzExe(atacante, 1);
			model.addExe(defensor, 1);
			saveState(pathAuto);
			verificaObjetivo();
		}
	}

	public void desloca(String territorioOrigem, String territorioDestino) {
		/** Funcao que realiza o deslocamento de exercitos. */
		int qtdDePara = qtdDeslocados.get(territorioOrigem + "-" + territorioDestino);
		int qtdDe = qtdDeslocaveis.get(territorioOrigem);
		int qtdPara = qtdDeslocaveis.get(territorioDestino);

		// Verifica se tentativa de deslocamento está retornando um exército para dono
		// anterior:
		if (qtdDePara < 0) {
			model.reduzExe(territorioOrigem, 1);
			model.addExe(territorioDestino, 1);

			qtdDePara++;
			qtdPara++;

			qtdDeslocaveis.put(territorioDestino, qtdPara);
			qtdDeslocados.put(territorioOrigem + "-" + territorioDestino, qtdDePara);
			qtdDeslocados.put(territorioDestino + "-" + territorioOrigem, -qtdDePara);
		}

		// Realiza deslocamento de "territorioOrigem" para "territorioDestino":
		else if (qtdDe > 0) {
			model.addExe(territorioDestino, 1);
			model.reduzExe(territorioOrigem, 1);

			qtdDePara++;
			qtdDe--;

			qtdDeslocaveis.put(territorioOrigem, qtdDe);
			qtdDeslocados.put(territorioOrigem + "-" + territorioDestino, qtdDePara);
			qtdDeslocados.put(territorioDestino + "-" + territorioOrigem, -qtdDePara);
		}
		saveState(pathAuto);
		verificaObjetivo();
	}

	public int getEtapa() {
		/** Funcao que retorna a etapa do jogo. */
		return this.etapa;
	}

	public int loadGame(String path) {
		/** Funcao que carrega o jogo dado um path do arquivo txt existente. */
		int load = model.loadGame(path);
		if (load == 0) {
			qtdDeslocados = null;
			qtdDeslocaveis = null;
			if (etapa > 0)
				etapa -= 10;
			if (etapa == 20)
				etapa = 30;
			proxEtapa();
		}
		return load;
	}

	public int loadGameAuto() {
		/** Funcao que carrega o jogo automaticamente. */
		return loadGame(pathAuto);
	}

	public String getEstadoStr() {
		String estado = String.format("%d,%d,%d,%d", etapa, iCont, qtdExeAd, conquista ? 1 : 0);
		return estado;
	}

	public void setEstado(String estado) {
		String[] info = estado.split(",");
		etapa = Integer.parseInt(info[0]);
		iCont = Integer.parseInt(info[1]);
		qtdExeAd = Integer.parseInt(info[2]);
		conquista = (Integer.parseInt(info[3]) == 1);
		if (DEBUG)
			System.out.printf("Etapa: %d\n", etapa);
		if (DEBUG)
			System.out.printf("iCont: %d\n", iCont);
		if (DEBUG)
			System.out.printf("qtdExeAd: %d\n", qtdExeAd);
		if (DEBUG)
			System.out.printf("conquista: %s\n", conquista ? "true" : "false");
	}

	public void novoJogo(String[] nomes) {
		/** Funcao que começa um novo jogo dados os nomes dos jogadores. */
		model.novoJogo(nomes);
		iCont = -1;
		etapa = 0;
		qtdExeAd = 0;
		qtdDeslocados = null;
		qtdDeslocaveis = null;
		proxEtapa();
	}

	public boolean podeTrocar() {
		/**
		 * Funcao que verifica se a etapa do jogo esta condizente com a troca de cartas.
		 */
		return (etapa == 0) && (iCont == -1);
	}

	public static void main(String[] args) {
		ControllerAPI control = ControllerAPI.getInstance();
		control.inicializa();
	}

	public boolean verificaTrocaCartas(boolean[] cartasSelecionadas) {
		/** Funcao que verifica as condicoes de troca de cartas. */
		return model.verificaTrocaCartas(cartasSelecionadas);
	}

	public void confirmaTroca(boolean[] cartasSelecionadas) {
		/** Funcao que confirma e executa a troca de cartas. */
		qtdExeAd += model.trocaCartas(cartasSelecionadas);
		proxEtapa(); // Executa a etapa
	}

	private void verificaObjetivo(){
		if (model.verificaObjetivo()) { // Verifica se o jogador atual venceu
				boolean continua = view.exibeVencedor();
				if (continua) {
					view.exibeNovoJogoNovamente();
				} else {
					System.exit(0);
				}
			}
	}

}
