package Controller;

//import View.SoundEffect;
import View.ViewAPI;
import Model.ModelAPI;
import java.io.IOException;
import java.util.Hashtable;

public class ControllerAPI {

	private static ControllerAPI instance;
	private ModelAPI model;
	private ViewAPI view;

	private int etapa = 0;
	private int corAtual;
	private int qtdExeAd;
	private String[] continentes;
	private int iCont = -1;

	private static Hashtable<String, Integer> qtdDeslocaveis;
	private static Hashtable<String, Integer> qtdDeslocados;
	

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
		try {
			model.loadGame();
		} catch (IOException e) {
			e.printStackTrace();
		}
		view.inicializaGameScreen();

		etapa = 0;

		proxEtapa();
	}

	public void proxEtapa() {
		corAtual = model.getCorAtual();

		String territorios[] = model.getTerritorios(corAtual);
		System.out.printf("Da etapa %d\n", etapa);

		// Posicionamento de exércitos
		if (etapa == 0) {
			if (qtdExeAd != 0) return;
			// qtdExeAd = 0;
			// Verifica há exércitos extras para colocar em cada continente
			while (qtdExeAd == 0 && iCont < continentes.length-1){
				iCont++;
				qtdExeAd = model.getExeAdContinente(continentes[iCont]);
				if (qtdExeAd > 0)
					territorios = model.getTerritoriosContinente(continentes[iCont]);
			}
			if (qtdExeAd == 0){
				qtdExeAd = model.getExeAd();
				iCont = -1;
			}
			view.setEtapa(etapa, territorios, corAtual, qtdExeAd);
		}

		// Ataque
		else if (etapa == 10) {
			if (qtdExeAd > 0) return;
			view.setEtapa(etapa, territorios, corAtual, 0);
			etapa = 20;
		}

		// Deslocamento de exércitos
		else if (etapa == 20) {
			
			qtdDeslocaveis = new Hashtable<String, Integer>();
			qtdDeslocados = new Hashtable<String, Integer>();
			for(String nome: territorios) {
				
				// Salva quantos exércitos cada território pode doar
				qtdDeslocaveis.put(nome, model.getQtdExercitos(nome)-1);
				
				// Salva quantos exércitos cada um doou para cada um
				for (String nome2: territorios) {
					if (nome != nome2) {
						qtdDeslocados.put(nome+"-"+nome2, 0);
					}
				}
			}
			
			view.setEtapa(etapa, territorios, corAtual, 0);
			etapa = 30;
		}

		// Entrega de carta
		else if (etapa == 30) {
			if (model.getConquista()) {
				model.entregaCarta();
			}
			view.setEtapa(etapa, null, corAtual, 0);
			etapa = 40;

			model.saveState(); // Salva o estado do jogo

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
		else if (etapa == 40){
			model.getProxCor();
			etapa = 0;
			proxEtapa();
		}

		System.out.printf("Para a etapa %d\n", etapa);
	}

	public String getContinenteAtual(){
		if (iCont < 0) return null;
		return continentes[iCont];
	}

	public void addExe(String territorio, int i){
		model.addExe(territorio, 1);
		qtdExeAd--;
		if (qtdExeAd == 0){
			if (iCont == -1)
				etapa = 10;
			proxEtapa();
		}
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
			view.conquista();
		}
	}

	public void ataque(String atacante, String defensor, int[][] dados) {
		model.ataca(atacante, defensor, dados);
		int corAtual = model.getCorAtual();

		view.resultadoAtaque(dados);

		if (model.getCor(defensor) == corAtual) { // Conquistou território
			view.conquista();
		}
	}

	public void movePosConquista(String atacante, String defensor, String clicado) {
		int qtdA = model.getQtdExercitos(atacante);
		int qtdD = model.getQtdExercitos(defensor);

		if (atacante == clicado && qtdD > 1) {
			model.reduzExe(defensor, 1);
			model.addExe(atacante, 1);
		} else if (defensor == clicado && qtdD < 3 && qtdA > 1) {
			model.reduzExe(atacante, 1);
			model.addExe(defensor, 1);
		}
	}
	
	public void desloca (String tDe, String tPara) {
		
		int qtdDePara = qtdDeslocados.get(tDe+"-"+tPara);
		int qtdDe = qtdDeslocaveis.get(tDe);
		int qtdPara = qtdDeslocaveis.get(tPara);
		
		// Verifica se tentativa de deslocamento está retornando um exército para dono anterior
		if (qtdDePara < 0) {
			model.reduzExe(tDe, 1);
			model.addExe(tPara, 1);
			
			qtdDePara++;
			qtdPara++;
			
			qtdDeslocaveis.put(tPara, qtdPara);
			qtdDeslocados.put(tDe+"-"+tPara, qtdDePara);
			qtdDeslocados.put(tPara+"-"+tDe, -qtdDePara);
		}
		
		// Realiza deslocamento de "tDe" para "tPara"
		else if (qtdDe > 0) {
			model.addExe(tPara, 1);
			model.reduzExe(tDe, 1);
			
			qtdDePara++;
			qtdDe--;
			
			qtdDeslocaveis.put(tDe, qtdDe);
			qtdDeslocados.put(tDe+"-"+tPara, qtdDePara);
			qtdDeslocados.put(tPara+"-"+tDe, -qtdDePara);
			
		}
		
	}

	public static void main(String[] args) {
		ControllerAPI control = ControllerAPI.getInstance();
		control.inicializa();

	}
}

// jogo.adicionaJogador(new Jogador(Cores.BRANCO, "LUIZA"));
// jogo.adicionaJogador(new Jogador(Cores.VERMELHO, "THOMAS"));
// jogo.adicionaJogador(new Jogador(Cores.VERDE, "JERONIMO"));
