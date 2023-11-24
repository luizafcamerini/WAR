package View;

import java.awt.*;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

import Model.ModelAPI;
import Controller.ControllerAPI;

public class ViewAPI {
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
	private GamePanel gP;
	private InfoPainel iP;

	private final Color[] cores = { Color.YELLOW, Color.BLUE, Color.WHITE, Color.BLACK, Color.GREEN, Color.RED };
	private final String[] coresStr = { "AMARELO", "AZUL", "BRANCO", "PRETO", "VERDE", "VERMELHO" };
	private GameScreen gameScreen;

	private ViewAPI() {
	}

	Color int2color(int i) {
		return cores[i];
	}

	public static ViewAPI getInstance() {
		if (instance == null) {
			instance = new ViewAPI();
		}

		instance.control = ControllerAPI.getInstance();
		instance.model = ModelAPI.getInstance();
		return instance;
	}

	public void inicializaGameScreen() {
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

	public Color setViewColor(String territorio) {
		int i = model.getCor(territorio);
		if (i == -1)
			return null;
		return cores[i];
	}

	public void ataca(String atacante, String defensor, int nAtaque, int nDefesa) {
		dados = new int[2][];
		dados[0] = new int[nAtaque];
		dados[1] = new int[nDefesa];
		for (int i = 0; i < nAtaque; i++)
			dados[0][i] = 0;

		for (int i = 0; i < nDefesa; i++)
			dados[1][i] = 0;

		gP.ataque(atacante, defensor);
	}

	public void setDados(int dados[][]) {
		this.dados = dados;
	}

	public void resultadoAtaque(int[][] dados) {
		this.dados = dados;
		gP.resultadoAtaque();
	}

	public void conquista() {
		gP.conquista();
	}

	private String constroiMsg() {
		String msg;
		int corAtual = model.getCorAtual();
		String cor = coresStr[corAtual];
		String nomeJogador = model.getJogadorAtual();

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

	public void setEtapa(int etapa, String[] territorios, int cor, int qtd) {
		// Torna não clicáveis os territórios anteriores
		if (this.territorios != null) {
			for (String nome : this.territorios) {
				Territorio.getTerritorio(nome).setClicavel(false);
			}
		}

		if (this.vizinhos != null) {
			for (String nome : this.vizinhos) {
				Territorio.getTerritorio(nome).setClicavel(false);
			}
		}

		if (selecionado != null) {

			Territorio t = Territorio.getTerritorio(selecionado);
			t.setMarcado(false);
			t.setClicavel(false);
			selecionado = null;
		}

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
		if (this.territorios != null) {
			for (String nome : territorios) {
				Territorio.getTerritorio(nome).setClicavel(true);
			}
		}
		if(etapa == 0){
			gP.setClicavelSalvar(false);
		}
		else if (etapa == 30){
			gP.setClicavelSalvar(true);
		}
		iP.setInfo(constroiMsg());
	}

	public void click(String territorio) {
		System.out.printf("input = %s\n", territorio == null ? "null" : territorio);
		System.out.printf("Etapa i = %d\n", etapa);

		Territorio t, t2;

		// Nesta etapa o jogador posiciona os exércitos nos territórios que a ele pertencem
		if (etapa == 0) {
			if (territorio != null) {
				qtdExe--;
				iP.setInfo(constroiMsg());
				control.addExe(territorio, 1);
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
				gP.conquista(model.getQtdExercitos(selecionado), model.getQtdExercitos(selecionado2));
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

		// Selcionar outro território para deslocar exércitos
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

		System.out.printf("Etapa f = %d\n", etapa);
		return;

	}

	public int[][] getListaDados() {
		return dados;
	}

	// public void clickBotao(int i) {
	// 	switch (i) {
	// 		case 0:
	// 			// objetivo
	// 			gP.setExibeObjetivo(true);
	// 			break;
	// 		case 1:
	// 			gP.setExibeCartas(true);
	// 			// exibir cartas de territorio
	// 			break;
	// 		case 2:
	// 			// tabela de exe
	// 			gP.setExibeTabelas(true);
	// 			break;
	// 		case 3:
	// 			//prox etapa
	// 			if (etapa == 0)
	// 				break;
	// 			control.proxEtapa();
	// 			break;
	// 		case 4:
	// 			//salvamento do jogo
	// 			if (etapa == 30) {
	// 				control.botaoSalvaJogo();
	// 			}
	// 			break;
	// 	}
	// }

	public boolean exibeVencedor() {
		int indexCorVencedor = model.getCorAtual();
		String corVencedor = coresStr[indexCorVencedor];

		System.out.println("Jogador " + corVencedor + " venceu!");

		if (gP.exibeVencedor(corVencedor))
			return true;
		else
			return false;
	}

	public String selecionaFile(){
		/** Funcao que retorna o path absoluto de salvamento do jogo por escolha do usuário. */
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); //cria um novo selecionador de arq
		int returnValue = jfc.showOpenDialog(null); //abre a janela do selecionador e retorna se ele salvou ou não
		String path;
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			// System.out.println(selectedFile.getAbsolutePath());
			path = selectedFile.getAbsolutePath();
			return path;
		}
		return null;
	}
}


