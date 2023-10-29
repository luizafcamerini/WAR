package View;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.awt.geom.*;
import java.awt.event.*;

public class Territorio implements MouseListener {
    private String nome;
    private int x, y;
    private Color cor;

    public void mouseClicked(MouseEvent e) {
        System.out.println("Mouse Clicked at X: " + e.getX() + " Y: " + e.getY());
    }

    public void mouseEntered(MouseEvent e) {
        // System.out.println("Mouse Entered");
    }


    public void mouseExited(MouseEvent e) {
        // System.out.println("Mouse Exited");
    }


    public void mousePressed(MouseEvent e) {
        // System.out.println("Mouse Pressed");
    }

    public void mouseReleased(MouseEvent e) {
        // System.out.println("Mouse Released");
    }

    public Territorio(String _nome, int _x, int _y){
        nome = _nome;
        x = _x;
        y = _y;
    }

    public void setCor(Color _cor){
        cor = _cor;
    }

    public void draw(Graphics g){
        Graphics2D g2d=(Graphics2D) g;
        Ellipse2D circ= new Ellipse2D.Double(x,y,25,25);
        g2d.setPaint(cor);
        g2d.fill(circ);
    }

}
