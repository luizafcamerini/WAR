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
    	
        model.adicionaJogador("LUIZA", 2);
        model.adicionaJogador("THOMAS", 5);
        model.adicionaJogador("JERONIMO", 4);
        
        model.inicializaJogo();
        // try {
        // model.loadGame();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        view.inicializaGameScreen();
        
        etapa = 0;
        
        proxEtapa();
    }
    
    public void proxEtapa() {
        corAtual = model.getCorAtual();
        String territorios[] = model.getTerritorios(corAtual);
        
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
            model.saveState();
        }
        else {
            model.getProxCor();
            etapa = -10;
        }
        
        etapa += 10;
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

    public static void main(String[] args) {
    	ControllerAPI control = ControllerAPI.getInstance();
    	control.inicializa();
    	
	}
}

// jogo.adicionaJogador(new Jogador(Cores.BRANCO, "LUIZA"));
// jogo.adicionaJogador(new Jogador(Cores.VERMELHO, "THOMAS"));
// jogo.adicionaJogador(new Jogador(Cores.VERDE, "JERONIMO"));
