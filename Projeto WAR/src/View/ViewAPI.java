package View;

import java.awt.*;
import Model.ModelAPI;
import Controller.ControllerAPI;

public class ViewAPI {
    private static ViewAPI instance;
    private ControllerAPI control;
    private ModelAPI model;
    
    private final Color [] cores = {Color.YELLOW, Color.BLUE, Color.WHITE, Color.BLACK, Color.GREEN, Color.RED};
	
    
    
    private GameScreen gameScreen;
    
    private ViewAPI() {
    }

    public static ViewAPI getInstance(){
        if(instance == null){
            instance = new ViewAPI();
        }

    	instance.control = ControllerAPI.getInstance();
    	instance.model = ModelAPI.getInstance();
        return instance;
    }

    public void inicializaGameScreen(){
        gameScreen = new GameScreen();
        
        gameScreen.setVisible(true);
    }
    
    public Color setViewColor(String territorio) {
    	int i = model.getCor(territorio);
    	return cores[i];
    }
    
    public int setViewQtdExe(String territorio) {
    	return model.getQtdExercitos(territorio);
    }
}

// package View;

// public class Main {

// 	public static void main(String[] args) {
// 		GameScreen f = new GameScreen();
// 		f.setVisible(true);

// 	}

// }