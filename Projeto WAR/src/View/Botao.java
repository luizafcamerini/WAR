package View;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import Observer.ObservadoIF;
import Observer.ObservadorIF;

import java.awt.font.*;
import java.awt.Font;

/*
 * i2 = ID do botão
 * 
 * i1 = 0 // Mouse clicou no botao
 * i1 = 1 // Mouse "entrou" no botao
 * i1 = 2 // Mouse "saiu" no botao
 * 
 * i3 = ID do botão caso i2 não seja único
 * 
 */

class Botao implements ObservadoIF, MouseListener, MouseMotionListener {
	private int x, y, alt, larg;
	private String text;
	private boolean clicavel = true;
	private Color cores[];
	private int cor = 0;
	private boolean estavaEm = false;

	// i1 = Acao do mouse: clicou, entrou, saiu
	// i2 = id do botao
	// i3 = id do botao caso i2 nao seja unico
	private int i1, i2, i3;

	// Lista de seus observadores:
	private List<ObservadorIF> lst = new ArrayList<ObservadorIF>();

	public void addObservador(ObservadorIF o) {
		/** Funcao que */
		lst.add(o);
	}

	public void removeObservador(ObservadorIF o) {
		lst.remove(o);
	}

	public int get(int i) {
		if (i == 1)
			return i1; // retorna a acao do
		else if (i == 2)
			return i2; // retorna o id do botao
		else if (i == 3)
			return i3; // retorna o "id alternativo" do botao
		return 0;
	}

	private void notificaObservadores() {
		for (ObservadorIF o : lst) {
			o.notify(this);
		}
	}

	{
		cores = new Color[3];
		cores[0] = Color.WHITE;
		cores[1] = Color.GRAY;
		cores[2] = new Color(64, 64, 64);
	}

	public Botao(int _x, int _y, int _larg, int _alt, String _text) {
		x = _x;
		y = _y;
		larg = _larg;
		alt = _alt;
		text = _text;
	}

	public Botao(String _text) {
		text = _text;
	}

	public void setBounds(int _x, int _y, int _larg, int _alt) {
		x = _x;
		y = _y;
		larg = _larg;
		alt = _alt;
	}

	public void setI2(int _i2) {
		i2 = _i2;
	}

	public void setI3(int _i3) {
		i3 = _i3;
	}

	public void setColor(int i, Color color) {
		cores[i] = color;
	}

	private void drawStr(Graphics g, String text, int x, int y) {
		g.setColor(Color.BLACK);

		FontMetrics metrics = g.getFontMetrics(g.getFont());
		x = x - metrics.stringWidth(text) / 2;
		y = y - metrics.getHeight() / 2 + metrics.getAscent();

		g.drawString(text, x, y);
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		Rectangle2D rect = new Rectangle2D.Double(x, y, larg, alt);
		g2d.setColor(cores[cor]);
		g2d.fill(rect);
		drawStr(g, text, x + larg / 2, y + alt / 2);
	}

	public void setPos(Graphics g, int xc, int yc) {
		Graphics2D g2d = (Graphics2D) g;
		FontRenderContext frc = g2d.getFontRenderContext();
		Font font = g2d.getFont();
		LineMetrics lm = font.getLineMetrics(text, frc);
		int margemX = 5;
		int margemY = 2;

		alt = (int) lm.getHeight() + margemY * 2; // altura da fonte
		larg = (int) font.getStringBounds(text, frc).getWidth() + margemX * 2; // largura
		x = xc - larg / 2 - margemX;
		y = yc - alt / 2 - margemY;
	}

	public boolean estaEm(int _x, int _y) {
		if (!clicavel)
			return false;
		int dx = _x - x;
		int dy = _y - y;

		if (dx > 0 && dx < larg && dy > 0 && dy < alt) {
			return true;
		}
		return false;
	}

	public void setClivael(boolean b) {
		clicavel = b;
		if (!b) {
			cor = 2;
			estavaEm = false;
		} else {
			cor = 0;
		}
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (estaEm(x, y)) {
			i1 = 0;
			notificaObservadores();
		}
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

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		boolean dentro = estaEm(x, y);

		if (dentro && !estavaEm) {
			cor = 1;
			i1 = 1;
			notificaObservadores();
			estavaEm = dentro;
		} else if (!dentro && estavaEm) {
			cor = 0;
			i1 = 2;
			notificaObservadores();
			estavaEm = dentro;
		}
	}

	public boolean atualiza(Graphics g, int _x, int _y) {
		/** Funcao que retorna se o mouse esta em cima ou nao, e muda a cor. */
		int dx = _x - x;
		int dy = _y - y;

		if (dx > 0 && dx < larg && dy > 0 && dy < alt) {
			cor = 1;
			estavaEm = true;
		} else if (clicavel) {
			cor = 0;
			estavaEm = false;
		}
		return estavaEm;
	}

}
