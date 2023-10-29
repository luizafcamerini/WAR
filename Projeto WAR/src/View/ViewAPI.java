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

    public void criaView(){
        gameScreen = new GameScreen();
    }
}