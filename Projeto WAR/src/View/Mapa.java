package View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.geom.*;

class Mapa extends JPanel {
    private ArrayList<Territorio> territorios = new ArrayList<Territorio>();

    public Mapa() {
    }

    public void add(Territorio t){
        territorios.add(t);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d=(Graphics2D) g;
        Ellipse2D circ= new Ellipse2D.Double(50,100,30,30);
        g2d.setPaint(Color.BLUE);
        g2d.fill(circ);
    }
}
