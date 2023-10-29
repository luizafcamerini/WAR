package View;

import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

class GameScreen extends JFrame {
    private Image tabuleiro;
    private Container content;
    private final String path = "src/View/images/";
    private static final int ALTURA_TELA = 700;
    private static final int LARGURA_TELA = 1200;

    public GameScreen(){
        setTitle("War");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(LARGURA_TELA, ALTURA_TELA);
        setResizable(false);
        content = getContentPane();
        
        // Centraliza a janela na tela
        Toolkit tk=Toolkit.getDefaultToolkit();
        Dimension screenSize=tk.getScreenSize();
        int sl=screenSize.width;
        int sa=screenSize.height;
        int x=sl/2-LARGURA_TELA/2;
        int y=sa/2-ALTURA_TELA/2;
        setBounds(x,y,LARGURA_TELA, ALTURA_TELA);
        
        String nomeImgTab = path + "war_tabuleiro.png";
        try {
            tabuleiro = ImageIO.read(new File(nomeImgTab));
        }
        catch(IOException e) {
            System.out.println(nomeImgTab);
            System.out.println(e.getMessage());
            System.exit(1);
        }
        GamePanel p = new GamePanel(tabuleiro);
        // Territorio t = new Territorio("Brasil",360,400);
        // t.setCor(Color.BLUE);
        // p.add(t);
        p.setBackground(Color.BLACK);

        content.add(p);
//        addMouseListener(t);
       
    }


}