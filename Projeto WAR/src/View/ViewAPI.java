package View;

import java.awt.*;
import java.util.Hashtable;

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
	private Images images;
	private int[][] dados;
	// private boolean exibeCartas;
	// private boolean exibeTabelas;
	// private boolean exibeObjetivo;
	private GamePanel gP;
	private InfoPainel iP;

	// private Hashtable<String, Image> imagensHashtable = new Hashtable<String,
	// Image>();

	private final Color[] cores = { Color.YELLOW, Color.BLUE, Color.WHITE, Color.BLACK, Color.GREEN, Color.RED };
	private final String[] coresStr = { "AMARELO", "AZUL", "BRANCO", "PRETO", "VERDE", "VERMELHO" };
	private GameScreen gameScreen;

	private ViewAPI() {
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
		gP.addMouseListener(iP);
		gP.addMouseMotionListener(iP);

		gameScreen = new GameScreen(gP);
		gameScreen.setVisible(true);

		images = Images.getInstance();
		// imagensHashtable = images.getImagensHashtable();

		Territorio[] lst = Territorio.getTerritorios();
		for (Territorio t : lst) {
			int n = model.getQtdExercitos(t.getNome());
			t.setNum(n);
			t.addObservador(gP);
			gP.addMouseListener(t);
			gP.addMouseMotionListener(t);
		}
	}

	public Color setViewColor(String territorio) {
		int i = model.getCor(territorio);
		if (i == -1)
			return null;
		return cores[i];
	}

	// public void notify(ObservadoIF o){
	// 	if (o instanceof Territorio){
	// 		Territorio t = (Territorio) o;
	// 		click(t.getNome());
	// 	}
	// }










	public void setEtapa(int etapa, String[] territorios, int cor, int qtd) {
		if (this.territorios != null) {
			for (String nome : this.territorios) {
				atualizaTerritorio(nome, false);
			}
		}

		this.territorios = territorios;
		this.etapa = etapa;
		this.corAtual = cor;
		this.qtdExe = qtd;
		if (this.territorios != null) {
			for (String nome : territorios) {
				atualizaTerritorio(nome, true);
			}
		}



		iP.setInfo((etapa/10)*10, coresStr[corAtual], qtd);
	}

	private void atualizaTerritorio(String territorio, boolean click) {

		int n = model.getQtdExercitos(territorio);
		Territorio t = Territorio.getTerritorio(territorio);
		t.setNum(n);
		int cor = model.getCor(territorio);
		t.setCor(cores[cor]);
		t.setClicavel(click);
	}

	public void click(String territorio) {
		System.out.printf("Etapa %d\n", etapa);
		// exibeCartas = false;
		// exibeTabelas = false;
		// exibeObjetivo = false;

		if (etapa == 12) {
			etapa = 10;
			click(selecionado);
			dados = null;
		}

		Territorio t, t2;

		if (territorios != null)
			for (String nome : this.territorios) {
				atualizaTerritorio(nome, false);
			}
		if (vizinhos != null)
			for (String nome : this.vizinhos) {
				atualizaTerritorio(nome, false);
			}

		if (etapa == 0) {
			if (territorio != null) {
				model.addExe(territorio, 1);
				qtdExe--;
				iP.setInfo(etapa, coresStr[corAtual], qtdExe);
				if (qtdExe == 0)
					control.proxEtapa();
			}
			if (territorios != null) {
				for (String nome : territorios) {
					atualizaTerritorio(nome, true);
				}
			}
		}

		else if (etapa == 10) {
			if (territorio == null) {
				if (selecionado != null) {
					atualizaTerritorio(selecionado, false);
					t = Territorio.getTerritorio(selecionado);
					t.setMarcado(false);
					selecionado = null;
				}

				if (territorios != null) {
					for (String nome : territorios) {
						atualizaTerritorio(nome, true);
					}
				}

				return;
			}
			selecionado = territorio;
			atualizaTerritorio(selecionado, true);

			t = Territorio.getTerritorio(selecionado);
			t.setMarcado(true);

			vizinhos = model.getVizinhos(selecionado);
			for (String nome : vizinhos) {
				t = Territorio.getTerritorio(nome);
				if (corAtual != model.getCor(nome))
					t.setClicavel(true);
			}
			etapa = 11;
		} else if (etapa == 11) {
			if (territorio == null) {
				if (selecionado != null) {
					atualizaTerritorio(selecionado, false);
					t = Territorio.getTerritorio(selecionado);
					t.setMarcado(false);
					selecionado = null;
				}
				if (territorios != null) {
					for (String nome : territorios) {
						atualizaTerritorio(nome, true);
					}
				}

				etapa = 10;
				return;
			}

			t = Territorio.getTerritorio(territorio);
			if (corAtual == model.getCor(territorio)) {
				t.setMarcado(false);
				for (String nome : territorios) {
					atualizaTerritorio(nome, true);
				}

				selecionado = null;
				etapa = 10;
			} else {
				dados = control.ataca(selecionado, territorio);

			}
		}


		else if (etapa == 20){
			if (territorio == null) {
				if (selecionado != null) {
					atualizaTerritorio(selecionado, false);
					t = Territorio.getTerritorio(selecionado);
					t.setMarcado(false);
					selecionado = null;
				}

				if (territorios != null) {
					for (String nome : territorios) {
						atualizaTerritorio(nome, true);
					}
				}

				return;
			}
			selecionado = territorio;
			atualizaTerritorio(selecionado, true);

			t = Territorio.getTerritorio(selecionado);
			t.setMarcado(true);

			vizinhos = model.getVizinhos(selecionado);
			
			for (String nome : vizinhos) {
				t = Territorio.getTerritorio(nome);
				if (corAtual == model.getCor(nome))
					t.setClicavel(true);
			}
			etapa = 21;
		}

		else if (etapa == 21){
			if (territorio == null) {
				if (selecionado != null) {
					atualizaTerritorio(selecionado, false);
					t = Territorio.getTerritorio(selecionado);
					t.setMarcado(false);
					selecionado = null;
				}

				if (territorios != null) {
					for (String nome : territorios) {
						atualizaTerritorio(nome, true);
					}
				}
				etapa = 20;
				return;
			}
			selecionado2 = territorio;
			atualizaTerritorio(selecionado2, true);

			t = Territorio.getTerritorio(selecionado2);
			t.setMarcado(true);

			etapa = 22;
		}

		else if (etapa == 22){
			if (territorio == null) {
				if (selecionado != null) {
					atualizaTerritorio(selecionado, false);
					t = Territorio.getTerritorio(selecionado);
					t.setMarcado(false);
					selecionado = null;
				}

				if (selecionado2 != null) {
					atualizaTerritorio(selecionado2, false);
					t = Territorio.getTerritorio(selecionado2);
					t.setMarcado(false);
					selecionado2 = null;
				}

				if (territorios != null) {
					for (String nome : territorios) {
						atualizaTerritorio(nome, true);
					}
				}

				etapa = 20;

				return;
			}

			int qtd1 = model.getQtdExercitos(selecionado);
			int qtd2 = model.getQtdExercitos(selecionado2);
			if (territorio == selecionado2 && qtd1 > 1){
				model.reduzExe(selecionado,1);
				model.addExe(selecionado2,1);
			}
			else if (territorio == selecionado && qtd2 > 1){
				model.reduzExe(selecionado2,1);
				model.addExe(selecionado,1);
			}
			atualizaTerritorio(selecionado,true);
			atualizaTerritorio(selecionado2,true);
		}

	}

	public int[][] getListaDados() {
		return dados;
	}

	// public void ataca(){
	// int listaDados[][] = model.getListaDados();
	// System.out.println("Dados de ataque: ");
	// for (int i = 0; i < listaDados[0].length; i++) {
	// System.out.print(listaDados[0][i] + " ");
	// }
	// System.out.println("\nDados de defesa: ");
	// for (int j = 0; j < listaDados[1].length; j++) {
	// System.out.print(listaDados[1][j] + " ");
	// }
	// System.out.println();

	// }

	public void clickBotao(int i) {
		switch (i){
			case 0:
				// objetivo
				gP.setExibeObjetivo(true);
				break;
			case 1:
				gP.setExibeCartas(true);
				// exibir cartas de territorio
				break;
			case 2:
				//tabela de exe
				gP.setExibeTabelas(true);
				break;
			case 3:
				if (etapa == 0) break;
				control.proxEtapa();
				break;
		}
	}

	// public boolean getExibeCartas(){
	// 	return exibeCartas;
	// }

	// public boolean getExibeTabelas(){
	// 	return exibeTabelas;
	// }

	// public boolean getExibeObjetivo(){
	// 	return exibeObjetivo;
	// }

}
