package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;

class GamePanel  extends JPanel implements MouseListener, MouseMotionListener {
    // private ArrayList<Territorio> territorios = new ArrayList<Territorio>();
    private Territorio[] territorios;
    Image tabuleiro;
    private Territorio foco;


    private int i = 0;
    
    public GamePanel(Image img) {
    	tabuleiro = img;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        territorios = Territorio.getTerritorios();
        Territorio t1 = territorios[i];
        // System.out.printf("\n%s,",t1.getNome());
    }

    // public void add(Territorio t){
    //     territorios.add(t);
    // }

    

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(tabuleiro, 0, 0, getWidth(), getHeight(), null);

        for(Territorio t: territorios){
            t.draw(g);
        }
        
    }


    public void mouseClicked(MouseEvent e) {
        // System.out.println("Mouse Clicked at X: " + e.getX() + " Y: " + e.getY());
        // Territorio t1 = territorios[i];
        // int x = Math.round(e.getX() / 5.0f) * 5;
        // int y = Math.round(e.getY() / 5.0f) * 5;
        // t1.setCoord(x,y);
        // t1 = territorios[++i];
        // System.out.printf("%d,%d\n%s,",x,y,t1.getNome());
        // if (i >= territorios.length)
        //     i=0;
        SoundEffect.play("src/View/sounds/attack.wav");
        for(Territorio t: territorios){

            if (t.estaEm(e.getX(),e.getY())) {
                t.marca(true);
            	// System.out.println("Está em "+t.getNome());
            };
        }
        repaint();
    }


    public void mouseEntered(MouseEvent e) {
//        System.out.println("Mouse Entered");
    }


    public void mouseExited(MouseEvent e) {
//        System.out.println("Mouse Exited");
    }


    public void mousePressed(MouseEvent e) {
//        System.out.println("Mouse Pressed");
    }

    public void mouseReleased(MouseEvent e) {
//        System.out.println("Mouse Released");
    }

    public void mouseDragged(MouseEvent e) {}


    public void mouseMoved(MouseEvent e){
        if (foco != null){
            // foco.setCor(Color.BLUE);
            // foco.setModo(0);
            foco.ocuta(false);
            repaint();

            foco = null;
        }
        for(Territorio t: territorios){

            if (t.estaEm(e.getX(),e.getY())) {
                // t.setCor(Color.RED);
                // t.setModo(1);
                t.ocuta(true);
                foco = t;
                repaint();
            	// System.out.println("Está em "+t.getNome());
            };
        }

    }


}
