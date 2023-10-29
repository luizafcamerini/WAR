package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;

class GamePanel  extends JPanel implements MouseListener {
    private ArrayList<Territorio> territorios = new ArrayList<Territorio>();
    Image tabuleiro;
    
    public GamePanel(Image img) {
    	tabuleiro = img;
        // this.addMouseListener(this);
    }

    public void add(Territorio t){
        territorios.add(t);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(tabuleiro, 0, 0, getWidth(), getHeight(), null);

        for(int i = 0; i < territorios.size(); i++){
            territorios.get(i).draw(g);
        }
        
    }

    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse Clicked");
    }

    public void mouseEntered(MouseEvent e) {
        System.out.println("Mouse Entered");
    }


    public void mouseExited(MouseEvent e) {
        System.out.println("Mouse Exited");
    }


    public void mousePressed(MouseEvent e) {
        System.out.println("Mouse Pressed");
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("Mouse Released");
    }



}
