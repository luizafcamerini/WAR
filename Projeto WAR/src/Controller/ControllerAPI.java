package Controller;

//import View.GameScreen;
import View.ViewAPI;
import Model.ModelAPI;


public class ControllerAPI {

    private static ControllerAPI instance;
    private ModelAPI model;
    private ViewAPI view;
    

    public static ControllerAPI getInstance(){
        if(instance == null){
            instance = new ControllerAPI();
        }
        return instance;
    }

    private ControllerAPI(){
    }

    public void inicializa(){
    	instance.model = ModelAPI.getInstance();
    	instance.view = ViewAPI.getInstance();
    	
        model.adicionaJogador("LUIZA", 2);
        model.adicionaJogador("THOMAS", 5);
        model.adicionaJogador("JERONIMO", 4);
        
        model.inicializaJogo();
        view.inicializaGameScreen();
        

    }

    public static void main(String[] args) {
    	
    	
    	ControllerAPI control = ControllerAPI.getInstance();
    	control.inicializa();

	}
}

// jogo.adicionaJogador(new Jogador(Cores.BRANCO, "LUIZA"));
// jogo.adicionaJogador(new Jogador(Cores.VERMELHO, "THOMAS"));
// jogo.adicionaJogador(new Jogador(Cores.VERDE, "JERONIMO"));
