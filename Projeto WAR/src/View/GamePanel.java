package View;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

class GamePanel extends JPanel {
    Image tabuleiro;

    public void paintComponent(Graphics g, int x, int y) {
        super.paintComponent(g);
        
        try {
            tabuleiro = ImageIO.read(new File("src/View/Imagens/tabuleiro.png"));
        }
       catch(IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
        g.drawImage(tabuleiro, x, y, null);

    }
}
