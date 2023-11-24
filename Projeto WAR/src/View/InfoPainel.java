package View;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

/*
 * i2 = -2 // Fixo
 * 
 * i1 = -1 // ocorreu mundança na mensagem exibida
 * i1 = 0 // Mouse clicou em algum botao
 * i1 = 1 // Mouse "entrou" em algum botao
 * i1 = 2 // Mouse "saiu" de algum botao
 * 
 * i3 = 0 // Botao clicado foi o 0
 * i3 = 1 // Botao clicado foi o 1
 * i3 = 2 // Botao clicado foi o 2
 * i3 = 3 // Botao clicado foi o 3
 * 
 */


public class InfoPainel implements ObservadoIF, ObservadorIF {
	private String msg;
	private int x, y, alt, larg;
	private Botao botoes[];
	private List<ObservadorIF> lst=new ArrayList<ObservadorIF>();
	private int i1, i3;

	public InfoPainel(int _x, int _y, int largura, int altura) {
		x = _x;
		y = _y;
		larg = largura;
		alt = altura;
		botoes = new Botao[4];
		botoes[0] = new Botao(x + 5, y + alt / 2 + 2, (larg / 2) - 10, alt / 4 - 4, "OBJETIVO");
		botoes[1] = new Botao(x + (larg / 2) + 5, y + alt / 2 + 2, (larg / 2) - 10, alt / 4 - 4,"CARTAS");
		botoes[2] = new Botao(x + 5, y + 3 * alt / 4 + 2, (larg / 2) - 10, alt / 4 - 4, "TABELAS");
		botoes[3] = new Botao(x + (larg / 2) + 5, y + 3 * alt / 4 + 2, (larg / 2) - 10, alt / 4 - 4,"PRÓX. ETAPA");

		for (int i = 0; i < 4; i++) {
			botoes[i].addObservador(this);
			botoes[i].setI2(i);
		}

	}
	
	public void atializaListeners(GamePanel gP) {
		for(Botao b:botoes) {
			
			gP.addMouseListener(b);
			gP.addMouseMotionListener(b);
		}
	}

	public void addObservador(ObservadorIF o) {
		lst.add(o);
	}

	public void removeObservador(ObservadorIF o) {
		lst.remove(o);
	}

	public int get(int i) {
		if (i == 1)
			return i1;
		else if (i == 3)
			return i3;
		return -2;
	}
	
	public void notify(ObservadoIF o) {
		i1 = o.get(1);
		
		if(i1 == 0) {
			i3 = o.get(2);
		}
		notificaObservadores();
	}

	public void setInfo(String mensagem){
		msg = mensagem;
		i1 = -1;
		notificaObservadores();
	}

	public void drawStringMultiLine(Graphics g, String text, int lineWidth, int x, int y) {
		if (text == null)
			return;
		FontMetrics m = g.getFontMetrics();
		if (m.stringWidth(text) < lineWidth) {
			g.drawString(text, x, y);
		} else {
			String[] words = text.split("\n");
			String currentLine = words[0];
			for (int i = 1; i < words.length; i++) {
				if (m.stringWidth(currentLine + words[i]) < lineWidth) {
					currentLine += " " + words[i];
				} else {
					g.drawString(currentLine, x, y);
					y += m.getHeight();
					currentLine = words[i];
				}
			}
			if (currentLine.trim().length() > 0) {
				g.drawString(currentLine, x, y);
			}
		}
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		Rectangle2D rt = new Rectangle2D.Double(x, y, larg, alt);
		g2d.setColor(Color.BLACK);
		g2d.fill(rt);
		g.setFont(new Font(null));
		g.setColor(Color.WHITE);
		drawStringMultiLine(g, msg, 10, x + 5, y + 20);

		for(Botao b:botoes)
			b.draw(g);
	}


	private void notificaObservadores(){
		for(ObservadorIF o: lst){
			o.notify(this);
		}
	}
	

}
