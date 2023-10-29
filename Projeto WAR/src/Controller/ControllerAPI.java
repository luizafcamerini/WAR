package Controller;

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
        model = ModelAPI.getInstance();
        view = ViewAPI.getInstance();
    }

    public void inicializa(){
        view.inicializaGameScreen();
        model.inicializaJogo();
        model.adicionaJogador("LUIZA", 0);
        model.adicionaJogador("THOMAS", 1);
        model.adicionaJogador("JERONIMO", 2);

    }

}

// jogo.adicionaJogador(new Jogador(Cores.BRANCO, "LUIZA"));
// jogo.adicionaJogador(new Jogador(Cores.VERMELHO, "THOMAS"));
// jogo.adicionaJogador(new Jogador(Cores.VERDE, "JERONIMO"));
