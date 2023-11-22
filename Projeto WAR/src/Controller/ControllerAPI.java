package Controller;

import View.SoundEffect;
//import View.GameScreen;
import View.ViewAPI;
import Model.ModelAPI;
import java.io.IOException;


public class ControllerAPI {

    private static ControllerAPI instance;
    private ModelAPI model;
    private ViewAPI view;
    
    private int etapa = 0;
    private int corAtual;
    
    

    public static ControllerAPI getInstance(){
        if(instance == null){
            instance = new ControllerAPI();
        }
        return instance;
    }

    private ControllerAPI(){}

    public void inicializa(){
    	instance.model = ModelAPI.getInstance();
    	instance.view = ViewAPI.getInstance();
    	
//         model.adicionaJogador("LUIZA", 2);
//         model.adicionaJogador("THOMAS", 5);
//         model.adicionaJogador("JERONIMO", 4);
//        
//         model.inicializaJogo();
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
        
        System.out.printf("Da etapa %d\n",etapa);
        if (etapa == 0) { // Posicionamento
        	int qtdExeAd = model.getExeAd();
        	view.setEtapa(etapa, territorios, corAtual, qtdExeAd);
        }
        else if (etapa == 10) { // Ataque
        	view.setEtapa(etapa, territorios, corAtual, 0);
            // return;
        }
        else if (etapa == 20){ // Deslocamento de exércitos
            view.setEtapa(etapa, territorios, corAtual, 0);
        }
        else if (etapa == 30){ // Entrega de carta
            if(model.getConquista()){
                model.entregaCarta();
            }
            view.setEtapa(etapa, null, corAtual, 0);

            model.saveState(); // Salva o estado do jogo

            if(model.verificaObjetivo()){ // Verifica se o jogador atual venceu
                boolean continua = view.exibeVencedor();
                if(continua){
                    // inicializa(); ainda não funciona
                }
                else{
                    System.exit(0);
                }
            }
           
            
        }
        else {
        	etapa = -10;
            model.getProxCor();
        }
        
        etapa += 10;
        System.out.printf("Para a etapa %d\n",etapa);
    }
    
    public void ataca2(String atacante, String defensor) {
    	int nDadosAtaque, nDadosDefesa, tmp;
    	
    	tmp = model.getQtdExercitos(atacante);
    	nDadosAtaque = tmp > 3 ? 3 : tmp - 1;
    	
    	tmp = model.getQtdExercitos(defensor);
    	nDadosDefesa = tmp > 3 ? 3 : tmp;
    	
    	view.ataca(atacante, defensor, nDadosAtaque, nDadosDefesa);
    	
    }
    
    public int[][] ataca(String atacante, String defensor) {
        SoundEffect.play("src/View/sounds/attack.wav");
    	int [][] dados = model.ataca(atacante, defensor);
        // view.ataca();
    	
    	int corAtual = model.getCorAtual();
    	String territorios[] = model.getTerritorios(corAtual);
        view.setEtapa(12, territorios, corAtual, 0);
        return dados;
    }
    
    public void ataque(String atacante, String defensor) {
    	int [][] dados = model.ataca(atacante, defensor);
    	
    	int corAtual = model.getCorAtual();
    	
    	if (model.getCor(defensor) == corAtual) { //Conquistou território
    		
    		view.conquista(dados);
//    		String territorios[] = model.getTerritorios(corAtual);
//    		view.setEtapa(12, territorios, corAtual, 0);
    	}
    	//view.ataque(atacante, defensor, dados)
    }
    
    public void movePosConquista(String atacante, String defensor, String clicado) {
    	int qtdA = model.getQtdExercitos(atacante);
    	int qtdD = model.getQtdExercitos(defensor);
    	
    	if (atacante == clicado && qtdD > 1){
			model.reduzExe(defensor,1);
			model.addExe(atacante,1);
		}
    	else if (defensor == clicado && qtdD < 3 && qtdA > 1){
			model.reduzExe(atacante,1);
			model.addExe(defensor,1);
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
