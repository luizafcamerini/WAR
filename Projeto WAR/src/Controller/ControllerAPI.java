package Controller;

//import View.SoundEffect;
import View.ViewAPI;
import Model.ModelAPI;
// import java.io.IOException;
import java.util.Hashtable;

public class ControllerAPI {
	private final String pathAuto = "src/autoSave.txt";
	private final boolean DEBUG = false;
	private static ControllerAPI instance;
	private ModelAPI model;
	private ViewAPI view;

	private int etapa = 0;
	private int corAtual;
	private int qtdExeAd;
	private String[] continentes;
	private int iCont = -1;
	private boolean conquista = false;

	private static Hashtable<String, Integer> qtdDeslocaveis;
	private static Hashtable<String, Integer> qtdDeslocados;

	private final String[] coresStr = { "AMARELO", "AZUL", "BRANCO", "PRETO", "VERDE", "VERMELHO" };

	public static ControllerAPI getInstance() {
		if (instance == null) {
			instance = new ControllerAPI();
		}
		return instance;
	}

	private ControllerAPI() {
	}

	public void inicializa() {
		instance.model = ModelAPI.getInstance();
		instance.view = ViewAPI.getInstance();
		continentes = model.getContinentes();

		// model.adicionaJogador("LUIZA", 2);
		// model.adicionaJogador("THOMAS", 5);
		// model.adicionaJogador("JERONIMO", 4);
		//
		// model.inicializaJogo();

		// tela de menu, com duas opcoes: comecar novo jogo, ou carregar um jogo ja
		// existente

		// String path = view.selecionaFile();
		// String path = "src/gameState.txt";
		// int load = model.loadGame(path);
		// model.novoJogo(coresStr);
		// se o load for -1, volta pra tela de menu
		// se for 0, da um load em um txt ja existente
		view.inicializaGameScreen();

		etapa = 0;

		proxEtapa();
	}

	public void proxEtapa() {
		corAtual = model.getCorAtual();
		if (corAtual == -1)
			return;

		// saveState(pathAuto);

		if (DEBUG)
			System.out.println(coresStr[corAtual]);

		String territorios[] = model.getTerritorios(corAtual);
		if (DEBUG)
			System.out.printf("Da etapa %d\n", etapa);

		// Posicionamento de exércitos
		if (etapa == 0) {
			// if (qtdExeAd != 0) return;
			// qtdExeAd = 0;
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
			view.setEtapa(etapa, territorios, corAtual, qtdExeAd);
		}

		// Ataque
		else if (etapa == 10) {
			if (qtdExeAd > 0)
				return;
			view.setEtapa(etapa, territorios, corAtual, 0);
			etapa = 20;
			// saveState(pathAuto);
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
			// saveState(pathAuto);
		}

		// Entrega de carta
		else if (etapa == 30) {
			if (conquista) {
				model.entregaCarta();
				conquista = false;
			}
			view.setEtapa(etapa, null, corAtual, 0);
			etapa = 40;
			// saveState(pathAuto);

			// model.saveState(); // Salva o estado do jogo

			if (model.verificaObjetivo()) { // Verifica se o jogador atual venceu
				boolean continua = view.exibeVencedor();
				if (continua) {
					// inicializa(); ainda não funciona
				} else {
					System.exit(0);
				}
			}

		}

		// Passa a vez para o próximo jogador
		else if (etapa == 40) {
			model.getProxCor();
			etapa = 0;
			proxEtapa();
		}

		saveState(pathAuto);

		if (DEBUG)
			System.out.printf("Para a etapa %d\n", etapa);
	}

	public void saveState(String path) {
		model.saveState(path);
	}

	public String getContinenteAtual() {
		if (iCont < 0)
			return null;
		return continentes[iCont];
	}

	public void addExe(String territorio, int i) {
		model.addExe(territorio, 1);
		qtdExeAd--;
		if (qtdExeAd == 0) {
			if (iCont == -1)
				etapa = 10;
			proxEtapa();
		}
		saveState(pathAuto);
	}

	public boolean ataca(String atacante, String defensor) {
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
		int[][] dados = model.ataca(atacante, defensor);
		int corAtual = model.getCorAtual();

		view.resultadoAtaque(dados);

		if (model.getCor(defensor) == corAtual) { // Conquistou território
			conquista = true;
			view.conquista();
			saveState(pathAuto);
		}
	}

	public void ataque(String atacante, String defensor, int[][] dados) {
		model.ataca(atacante, defensor, dados);
		int corAtual = model.getCorAtual();

		view.resultadoAtaque(dados);

		if (model.getCor(defensor) == corAtual) { // Conquistou território
			view.conquista();
			saveState(pathAuto);
		}
	}

	public void movePosConquista(String atacante, String defensor, String clicado) {
		int qtdA = model.getQtdExercitos(atacante);
		int qtdD = model.getQtdExercitos(defensor);

		if (atacante == clicado && qtdD > 1) {
			model.reduzExe(defensor, 1);
			model.addExe(atacante, 1);
			saveState(pathAuto);
		} else if (defensor == clicado && qtdD < 3 && qtdA > 1) {
			model.reduzExe(atacante, 1);
			model.addExe(defensor, 1);
			saveState(pathAuto);
		}
	}

	public void desloca(String tDe, String tPara) {

		int qtdDePara = qtdDeslocados.get(tDe + "-" + tPara);
		int qtdDe = qtdDeslocaveis.get(tDe);
		int qtdPara = qtdDeslocaveis.get(tPara);

		// Verifica se tentativa de deslocamento está retornando um exército para dono
		// anterior
		if (qtdDePara < 0) {
			model.reduzExe(tDe, 1);
			model.addExe(tPara, 1);

			qtdDePara++;
			qtdPara++;

			qtdDeslocaveis.put(tPara, qtdPara);
			qtdDeslocados.put(tDe + "-" + tPara, qtdDePara);
			qtdDeslocados.put(tPara + "-" + tDe, -qtdDePara);
		}

		// Realiza deslocamento de "tDe" para "tPara"
		else if (qtdDe > 0) {
			model.addExe(tPara, 1);
			model.reduzExe(tDe, 1);

			qtdDePara++;
			qtdDe--;

			qtdDeslocaveis.put(tDe, qtdDe);
			qtdDeslocados.put(tDe + "-" + tPara, qtdDePara);
			qtdDeslocados.put(tPara + "-" + tDe, -qtdDePara);

		}
		saveState(pathAuto);

	}

	public int getEtapa() {
		return this.etapa;
	}

	public int loadGame(String path) {
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
		model.novoJogo(nomes);
		iCont = -1;
		etapa = 0;
		qtdExeAd = 0;
		qtdDeslocados = null;
		qtdDeslocaveis = null;
		proxEtapa();
	}

	public static void main(String[] args) {
		ControllerAPI control = ControllerAPI.getInstance();

		control.inicializa();

	}
}

// jogo.adicionaJogador(new Jogador(Cores.BRANCO, "LUIZA"));
// jogo.adicionaJogador(new Jogador(Cores.VERMELHO, "THOMAS"));
// jogo.adicionaJogador(new Jogador(Cores.VERDE, "JERONIMO"));
