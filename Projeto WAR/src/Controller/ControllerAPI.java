package Controller;

//import View.GameScreen;
import View.ViewAPI;
import Model.ModelAPI;


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
        view.inicializaGameScreen();
        
        etapa = 0;
        
        proxEtapa();
    }
    
    public void proxEtapa() {
        corAtual = model.getCorAtual();
        String territorios[] = model.getTerritorios(corAtual);
        
        if (etapa == 0) {
        	int qtdExeAd = model.getExeAd();
        	view.setEtapa(etapa, territorios, corAtual, qtdExeAd);
        }
        else if (etapa == 10) {
        	view.setEtapa(etapa, territorios, corAtual, 0);
        }
        
        
        etapa = (etapa +10)%20;
    }
    
    public void ataca(String atacante, String defensor) {
    	model.ataca(atacante, defensor);
    	
    	int corAtual = model.getCorAtual();
    	String territorios[] = model.getTerritorios(corAtual);
        view.setEtapa(10, territorios, corAtual, 0);
    }

    public static void main(String[] args) {
    	ControllerAPI control = ControllerAPI.getInstance();
    	control.inicializa();
    	
    	

	}
}

// jogo.adicionaJogador(new Jogador(Cores.BRANCO, "LUIZA"));
// jogo.adicionaJogador(new Jogador(Cores.VERMELHO, "THOMAS"));
// jogo.adicionaJogador(new Jogador(Cores.VERDE, "JERONIMO"));
