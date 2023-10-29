package View;

public class ViewAPI {
    private static ViewAPI instance;
    private GameScreen gameScreen;

    public static ViewAPI getInstance(){
        if(instance == null){
            instance = new ViewAPI();
        }
        return instance;
    }

    public void inicializaGameScreen(){
        gameScreen = new GameScreen();
        gameScreen.setVisible(true);
    }
}

// package View;

// public class Main {

// 	public static void main(String[] args) {
// 		GameScreen f = new GameScreen();
// 		f.setVisible(true);

// 	}

// }