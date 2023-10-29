package View;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class InfoPainel{
	private String msg;
	int x, y, alt, larg;

    public InfoPainel(int _x, int _y, int largura, int altura) {
        x = _x;
        y = _y;
        larg = largura;
        alt = altura;
    }
    
    public void setInfo(int etapa, String cor, int qtdExe) {
    	
    	switch (etapa) {
    	case 0:
    		msg = String.format("Jogador: %s\nEtapa: Posicionamento\nQtd exércitos: %d",cor,qtdExe);
    		
    	
    	}
    }
    
    public void drawStringMultiLine(Graphics g, String text, int lineWidth, int x, int y) {
    	if (text == null) return;
        FontMetrics m = g.getFontMetrics();
        if(m.stringWidth(text) < lineWidth) {
            g.drawString(text, x, y);
        } else {
            String[] words = text.split("\n");
            String currentLine = words[0];
            for(int i = 1; i < words.length; i++) {
                if(m.stringWidth(currentLine+words[i]) < lineWidth) {
                    currentLine += " "+words[i];
                } else {
                    g.drawString(currentLine, x, y);
                    y += m.getHeight();
                    currentLine = words[i];
                }
            }
            if(currentLine.trim().length() > 0) {
                g.drawString(currentLine, x, y);
            }
        }
    }


    public void draw(Graphics g) {
        Graphics2D g2d=(Graphics2D) g;
        
     // Desenha retângulo
        Rectangle2D rt=new Rectangle2D.Double(x,y,alt,larg);
        
        g2d.setColor(Color.BLACK);
        g2d.fill(rt);
        
        g.setColor(Color.WHITE);
        drawStringMultiLine(g,msg, 10,x+5,y+20);
    }
	

}
