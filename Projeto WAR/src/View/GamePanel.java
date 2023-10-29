package View;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

class GamePanel extends JPanel {
    Image tabuleiro;
    
    public GamePanel(Image img) {
    	tabuleiro = img;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(tabuleiro, 0, 0, getWidth(), getHeight(), null);
//        g.drawImage(tabuleiro, 0, 0, null);
    }
}
