package View;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

class GameScreen extends JFrame {
    private Image tabuleiro;

    private static final int ALTURA_TELA = 700;
    private static final int LARGURA_TELA = 1200;

    public GameScreen(){
        setTitle("War");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(LARGURA_TELA, ALTURA_TELA);
       
    }


}