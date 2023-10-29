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
    }

}
