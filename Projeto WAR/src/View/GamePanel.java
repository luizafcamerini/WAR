package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;

class GamePanel  extends JPanel implements MouseListener, MouseMotionListener {
    private Territorio[] territorios;
    private Image tabuleiro;
    private Territorio foco;
    private InfoPainel iP;

    


    private int i = 0;
    
    public GamePanel(Image img, InfoPainel iP) {
    	tabuleiro = img;
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.iP = iP;

        territorios = Territorio.getTerritorios();
//        Territorio t1 = territorios[i];
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.drawImage(tabuleiro, 0, 0, getWidth(), getHeight(), null);

        for(Territorio t: territorios){
            t.draw(g);
        }
        
        iP.draw(g);
    }
    


    public void mouseClicked(MouseEvent e) {
        SoundEffect.play("src/View/sounds/attack.wav");
        boolean fora = true;
        for(Territorio t: territorios){

            if (t.estaEm(e.getX(),e.getY())) {
//                t.setMarcado(true);
                ViewAPI.getInstance().click(t.getNome());
                fora = false;
            	// System.out.println("Está em "+t.getNome());
            };
        }
        if (fora)
        	ViewAPI.getInstance().click(null);
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
            foco.setOcuto(false);
            repaint();

            foco = null;
        }
        for(Territorio t: territorios){

            if (t.estaEm(e.getX(),e.getY())) {
                t.setOcuto(true);
                foco = t;
                repaint();
            	// System.out.println("Está em "+t.getNome());
            }
        }

    }


}
