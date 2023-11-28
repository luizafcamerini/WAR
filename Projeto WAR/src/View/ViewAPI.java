package View;

import java.awt.*;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import Model.ModelAPI;
import Controller.ControllerAPI;

public class ViewAPI {
	private final Color[] cores = { Color.YELLOW, Color.BLUE, Color.WHITE, Color.BLACK, Color.GREEN, Color.RED };
	private final String[] coresStr = { "AMARELO", "AZUL", "BRANCO", "PRETO", "VERDE", "VERMELHO" };
	private final boolean DEBUG = false;

	private static ViewAPI instance;

	private ControllerAPI control;
	private ModelAPI model;

	private int etapa;
	private String[] territorios;
	private String[] vizinhos;
	private String selecionado;
	private String selecionado2;
	private int corAtual;
	private int qtdExe;
	private int[][] dados;
	private GameScreen gameScreen;
	private GamePanel gP;
	private InfoPainel iP;
	private int corDefensor;

	private SoundEffect somAtaque = new SoundEffect("sounds/attack.wav");
	private SoundEffect somJogadorMorto = new SoundEffect("sounds/damageMine.wav");
	private SoundEffect somVitoria = new SoundEffect("sounds/victory.wav");
	private SoundEffect somConquista = new SoundEffect("sounds/conquista.wav");

	private ViewAPI() {
	}

	public static ViewAPI getInstance() {
		/** Metodo que pega a unica instancia de ViewAPI. */
		if (instance == null) {
			instance = new ViewAPI();
		}
		instance.control = ControllerAPI.getInstance();
		instance.model = ModelAPI.getInstance();
		return instance;
	}

	/*------------------------------------- Métodos com visibilidade no pacote -------------------------------------------------------------------------- */

	Color int2color(int i) {
		/** Metodo que retorna a cor de um dado indice. */
		return cores[i];
	}

	Color getColor(String territorio) {
		/** Metodo que retorna a cor do dono de um territorio dado seu nome. */
		int i = model.getCor(territorio);
		if (i == -1)
			return null;
		return cores[i];
	}

	String selecionaFile() {
		/**
		 * Metodo que retorna o path absoluto de salvamento do jogo por escolha do
		 * usuário.
		 */
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir")); // cria um novo selecionador de arq
		int returnValue = jfc.showOpenDialog(null); // abre a janela do selecionador e retorna se ele salvou ou não
		String path;

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			path = selectedFile.getAbsolutePath();
			return path;
		}
		return null;
	}

	String salvaFile() {
		/**
		 * Metodo que retorna o path absoluto de salvamento do jogo por escolha do
		 * usuário.
		 */
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir")); // cria um novo selecionador de arq
		int returnValue = jfc.showSaveDialog(jfc); // abre a janela do selecionador e retorna se ele salvou ou não
		String path;

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			// System.out.println(selectedFile.getAbsolutePath());
			path = selectedFile.getAbsolutePath();
			return path;
		}
		return null;
	}

	void click(String territorio) {
		/** Metodo que trata os click com relacao as etapas do jogo, dado um nome do territorio clicado. */
		if (DEBUG)
			System.out.printf("input = %s\n", territorio == null ? "null" : territorio);
		if (DEBUG)
			System.out.printf("Etapa i = %d\n", etapa);

		Territorio t, t2;

		// Nesta etapa o jogador posiciona os exércitos nos territórios que a ele
		// pertencem
		if (etapa == 0) {
			if (territorio != null) {
				qtdExe--;
				iP.setInfo(constroiMsg());
				control.addExe(territorio);
			}
			if (territorios != null) {
				for (String nome : territorios) {
					Territorio.getTerritorio(nome).setClicavel(true);
				}
			}
		}

		// Nesta etapa, o jogador seleciona o território que será o atacante
		else if (etapa == 10) {
			// Clicou em um território válido para atacar
			if (territorio != null) {
				// Define os territórios como não clicáveis
				if (territorios != null)
					for (String nome : this.territorios) {
						Territorio.getTerritorio(nome).setClicavel(false);
					}
				// Destaca o atacante
				selecionado = territorio;
				t = Territorio.getTerritorio(selecionado);
				t.setMarcado(true);
				t.setClicavel(true);
				// Torna clicável todos os possíveis defensores
				vizinhos = model.getVizinhos(selecionado);
				for (String nome : vizinhos) {
					t = Territorio.getTerritorio(nome);
					if (corAtual != model.getCor(nome))
						t.setClicavel(true);
				}
				etapa = 11;
			}
		}

		// Seleciona o defensor
		else if (etapa == 11) {
			// Clicou do lado de fora
			if (territorio == null) {
				// Define vizinhos como não clicáveis
				if (vizinhos != null)
					for (String nome : this.vizinhos) {
						Territorio.getTerritorio(nome).setClicavel(false);
					}
				// Desmarca o selecionado
				if (selecionado != null) {
					t = Territorio.getTerritorio(selecionado);
					t.setMarcado(false);
					t.setClicavel(false);
					selecionado = null;
				}
				// Define os territórios do jogador como clicáveis
				if (territorios != null) {
					for (String nome : territorios) {
						Territorio.getTerritorio(nome).setClicavel(true);
					}
				}
				etapa = 10;
			}

			// Clicou em um território
			else {
				t = Territorio.getTerritorio(territorio);
				// Território clicado é o selecionado
				if (corAtual == model.getCor(territorio)) {
					click(null);
				}
				// Território clicado é um defensor válido
				else {
					selecionado2 = territorio;
					corDefensor = model.getCor(territorio);
					if (control.ataca(selecionado, selecionado2))
						etapa = 12;
				}
			}
		}

		// Tela de ataque ou de conquista
		else if (etapa == 12) {
			// Encerramento do ataque ou do deslocamento pós conquista
			if (territorio == null) {
				// Define vizinhos como não clicáveis
				if (vizinhos != null)
					for (String nome : this.vizinhos) {
						Territorio.getTerritorio(nome).setClicavel(false);
					}
				// Desmarca o selecionado
				if (selecionado != null) {
					t = Territorio.getTerritorio(selecionado);
					t.setMarcado(false);
					t.setClicavel(false);
					selecionado = null;
				}
				selecionado2 = null;

				setEtapa(10, model.getTerritorios(corAtual), corAtual, 0);
			}

			// Deslocando exército para território conquistado
			else {
				control.movePosConquista(selecionado, selecionado2, territorio);
			}
		}

		// Selcionar um território para deslocar exércitos
		else if (etapa == 20) {
			if (territorio != null) {
				if (territorios != null)
					for (String nome : this.territorios) {
						Territorio.getTerritorio(nome).setClicavel(false);
					}
				selecionado = territorio;
				t = Territorio.getTerritorio(selecionado);
				t.setMarcado(true);
				t.setClicavel(true);
				vizinhos = model.getVizinhos(selecionado);
				for (String nome : vizinhos) {
					t = Territorio.getTerritorio(nome);
					if (corAtual == model.getCor(nome))
						t.setClicavel(true);
				}
				etapa = 21;
			}
		}

		// Selecionar outro território para deslocar exércitos
		else if (etapa == 21) {
			if (territorio == null || territorio == selecionado) {
				if (selecionado != null) {
					t = Territorio.getTerritorio(selecionado);
					t.setMarcado(false);
					selecionado = null;
				}
				if (territorios != null) {
					for (String nome : territorios) {
						Territorio.getTerritorio(nome).setClicavel(true);
					}
				}
				etapa = 20;
			}

			else {
				for (String nome : vizinhos) {
					Territorio.getTerritorio(nome).setClicavel(false);
				}
				selecionado2 = territorio;
				t = Territorio.getTerritorio(selecionado);
				t2 = Territorio.getTerritorio(selecionado2);
				t.setMarcado(true);
				t2.setMarcado(true);
				t.setClicavel(true);
				t2.setClicavel(true);
				etapa = 22;
			}
		}

		//
		else if (etapa == 22) {
			if (territorio == null) {
				if (selecionado != null) {
					t = Territorio.getTerritorio(selecionado);
					t.setMarcado(false);
					selecionado = null;
				}
				if (selecionado2 != null) {
					t = Territorio.getTerritorio(selecionado2);
					t.setMarcado(false);
					selecionado2 = null;
				}
				if (territorios != null) {
					for (String nome : territorios) {
						Territorio.getTerritorio(nome).setClicavel(true);
					}
				}
				etapa = 20;
			}

			else {
				// Desloca exército para exército clicado
				if (territorio == selecionado)
					control.desloca(selecionado2, selecionado);
				else
					control.desloca(selecionado, selecionado2);
			}
		}

		if (DEBUG)
			System.out.printf("Etapa f = %d\n", etapa);
		return;
	}

	/*----------------------------------------------------------------------------------------------------------------------- */
	// Métodos de visibilidade private

	private String constroiMsg() {
		/** Metodo que constroi o texto do InfoPainel. */
		String msg;
		int corAtual = model.getCorAtual();
		String cor = coresStr[corAtual];
		String nomeJogador = model.getNomeJogadorAtual();
		int etapa = (this.etapa / 10) * 10;
		msg = String.format("Jogador: %s\nCor: %s\n", nomeJogador, cor);

		// Posicionamento
		if (etapa == 0) {
			msg += String.format("Etapa:  Posicionamento de exércitos\n");
			String cont = control.getContinenteAtual();
			if (cont != null) {
				msg += String.format("Continente: %s\n", cont);
			}
			msg += String.format("Qtd exércitos: %d", qtdExe);
		}

		// Ataque
		else if (etapa == 10) {
			msg += String.format("Etapa: Ataque\n");
		}

		// Deslocamento
		else if (etapa == 20) {
			msg += String.format("Etapa: Deslocamento");
		}

		// Recebimento de carta
		else if (etapa == 30) {
			msg += String.format("Etapa: Recebimento de carta");
		}
		return msg;
	}

	public boolean exibeVencedor(String corVencedor) {
		/**
		 * Metodo que exibe um Dialog do vencedor e outra de "Jogar novamente?". Retorna
		 * se o usuário escolheu jogar novamente.
		 */
		somVitoria.play();
		JOptionPane.showMessageDialog(null, "O jogador " + corVencedor + " venceu!", "Fim de jogo!",
				JOptionPane.INFORMATION_MESSAGE);
		int resposta = JOptionPane.showConfirmDialog(null, "Jogar novamente?", "Continuar?", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (resposta == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}

	private void exibeJogadorMorto() {
		/** Metodo que exibe um Dialog com um jogador morto. */
		String nomeMorto = model.getNomeJogador(corDefensor);
		String nomeAssassino = model.getNomeJogadorAtual();
		String corMorto = coresStr[corDefensor];
		String corAssassino = coresStr[corAtual];

		String msg = String.format("%s (%s) eliminou %s (%s).", nomeAssassino, corAssassino, nomeMorto, corMorto);
		JOptionPane.showMessageDialog(null, msg, "Alerta do jogo!", JOptionPane.INFORMATION_MESSAGE);
	}

	/*----------------------------------------------------------------------------------------------------------------------- */
	// Métodos de visibilidade public

	public void inicializaGameScreen() {
		/** Metodo que inicializa a tela do jogo. */
		iP = new InfoPainel(10, 350, 200, 250);

		gP = new GamePanel(iP);
		gP.setBackground(Color.BLACK);

		iP.addObservador(gP);
		iP.atializaListeners(gP);

		gameScreen = new GameScreen(gP);
		gameScreen.setVisible(true);

		Territorio[] lst = Territorio.getTerritorios();
		for (Territorio t : lst) {
			model.registra(t.getNome(), t);
			int n = model.getQtdExercitos(t.getNome());
			t.setNum(n);

			t.addObservador(gP);
			gP.addMouseListener(t);
			gP.addMouseMotionListener(t);
		}
	}

	public void setDado(int iDado, int valor) {
		/** Metodo que define um valor de um dado dado seu indice e seu valor. */
		if (dados != null) {
			if (iDado < 3) { // Dado de ataque
				if (dados[0].length > iDado)
					dados[0][iDado] = valor;
			} else { // Dado de defesa
				if (dados[1].length > iDado - 3)
					dados[1][iDado - 3] = valor;
			}
		}
	}

	public void ataca(String atacante, String defensor, int nAtaque, int nDefesa) {
		/** Metodo que define que a tela de atque será exibida. */
		// Define os dados de acordo com a quantidade de exercitos dos atacante e
		// defensor:
		dados = new int[2][];
		dados[0] = new int[nAtaque];
		dados[1] = new int[nDefesa];
		for (int i = 0; i < nAtaque; i++)
			dados[0][i] = 0;
		for (int i = 0; i < nDefesa; i++)
			dados[1][i] = 0;

		gP.ataque(atacante, defensor);
	}

	public void resultadoAtaque(int[][] dados) {
		/** Metodo que exibe o resultado do ataque. */
		this.dados = dados;
		// Conquistou o territorio:
		if (model.getCor(selecionado) == model.getCor(selecionado2)) {
			// Morte de um jogador:
			if (model.getTerritorios(corDefensor).length == 0) {
				somJogadorMorto.play();
				exibeJogadorMorto();
			}
			gP.conquista();
			somConquista.play();
		}
		else {
			somAtaque.play();
			gP.resultadoAtaque();
		}
	}

	public void setEtapa(int etapa, String[] territorios, int cor, int qtd) {
		/**
		 * Metodo que define a configuracao das etapas dado seu numero, os nomes dos
		 * territorios, sua cor e quantidade de exercito.
		 */
		// Torna não clicáveis os territórios anteriores:
		if (this.territorios != null) {
			for (String nome : this.territorios) {
				Territorio.getTerritorio(nome).setClicavel(false);
			}
		}

		// Torna clicáveis os seus vizinhos:
		if (this.vizinhos != null) {
			for (String nome : this.vizinhos) {
				Territorio.getTerritorio(nome).setClicavel(false);
			}
		}

		// Desmarca os selecionados:
		if (selecionado != null) {
			Territorio t = Territorio.getTerritorio(selecionado);
			t.setMarcado(false);
			t.setClicavel(false);
			selecionado = null;
		}

		// Desmarca os selecionados:
		if (selecionado2 != null) {
			Territorio t = Territorio.getTerritorio(selecionado2);
			t.setMarcado(false);
			t.setClicavel(false);
			selecionado2 = null;
		}

		this.territorios = territorios;
		this.etapa = etapa;
		this.corAtual = cor;
		this.qtdExe = qtd;
		// Torna clicáveis os territorios atuais:
		if (this.territorios != null) {
			for (String nome : territorios) {
				Territorio.getTerritorio(nome).setClicavel(true);
			}
		}
		// Atualiza o texto do InfoPainel:
		iP.setInfo(constroiMsg());
	}

	public int[][] getListaDados() {
		/* Metodo que retorna a matriz de dados. */
		return dados;
	}

	public void exibeNovoJogoNovamente() {
		/**
		 * Metodo que pega os nomes dos jogadores passados e exibe o novo jogo
		 * novamente.
		 */
		String nomes[] = new String[6];
		for (int i = 0; i < 6; i++) {
			if (model.getNomeJogador(i) != null) {
				nomes[i] = model.getNomeJogador(i);
			}
		}
		gP.exibeNovoJogoNovamente(nomes);
	}

	public void obrigaTroca() {
		/** Funcao que obriga a troca de cartas. */
		gP.obrigaTroca();
	}

	/****** Conexao com o controller ******/
	public void proxEtapa() {
		/** Metodo que executa/passa para a proxima etapa. */
		control.proxEtapa();
	}

	public void ataque(String atacante, String defensor) {
		/** Metodo que realiza um ataque automatico. */
		control.ataque(atacante, defensor);
	}

	public void ataque(String atacante, String defensor, int[][] dados) {
		/** Metodo que realiza um ataque manual. */
		control.ataque(atacante, defensor, dados);
	}

	public boolean ataca(String atacante, String defensor) {
		/** Metodo que verifica se a tela de ataque será exibida. */
		return control.ataca(atacante, defensor);
	}

	public int getEtapa() {
		/** Metodo que retorna a etapa do jogo. */
		return control.getEtapa();
	}

	public void saveState(String path) {
		/** Metodo que salva o jogo dado um path para o arquivo txt. */
		control.saveState(path);
	}

	public int loadGame(String path) {
		/** Metodo que carrega o jogo dado um path do arquivo txt existente. */
		return control.loadGame(path);
	}

	public int loadGameAuto() {
		/** Metodo que carrega o jogo automaticamente. */
		return control.loadGameAuto();
	}

	public void novoJogo(String[] nomes) {
		/** Metodo que começa um novo jogo dados os nomes dos jogadores. */
		control.novoJogo(nomes);
	}

	public void confirmaTroca(boolean[] cartasSelecionadas) {
		/** Metodo que confirma e executa a troca de cartas. */
		control.confirmaTroca(cartasSelecionadas);
	}

	public boolean podeTrocar() {
		/**
		 * Metodo que verifica se a etapa do jogo esta condizente com a troca de cartas.
		 */
		return control.podeTrocar();
	}

	public int getExeAdCartas(){
		return model.getExeAdCartas();
	}

}
