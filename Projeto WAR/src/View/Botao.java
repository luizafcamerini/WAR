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

class Botao implements ObservadoIF, MouseListener, MouseMotionListener {
	private int x, y, alt, larg;
	private String text;
	private boolean clicavel = true;
	
	protected Color cores[];
	protected int i1, i2;
	protected boolean estavaEm = false;
	protected boolean ativado = true;
	
	private List<ObservadorIF> lst = new ArrayList<ObservadorIF>();

	public void addObservador(ObservadorIF o) {
		lst.add(o);
	}

	public void removeObservador(ObservadorIF o) {
		lst.remove(o);
	}

	public int get(int i) {
		if (i == 1)
			return i1;
		else if (i == 2) {
			return i2;
		}
		return 0;
	}
	
	private void notificaObservadores(){
		for(ObservadorIF o: lst){
			o.notify(this);
		}
	}
	
	public Botao(int _x, int _y, int _larg, int _alt, String _text) {
		x = _x;
		y = _y;
		larg = _larg;
		alt = _alt;
		text = _text;
		cores = new Color[4];
		cores[0] = Color.WHITE; //cores[0] = cor de preenchimento
	}
	
	public Botao(int _x, int _y, int _larg, int _alt, String _text, boolean flag) {
		x = _x;
		y = _y;
		larg = _larg;
		alt = _alt;
		text = _text;
		cores = new Color[4];
		if (flag) {
			cores[0] = Color.WHITE; //cores[0] = cor de preenchimento
		}
		else{
			cores[0] = Color.GRAY;
		}
		ativado = flag;
	}
	
	public void setI2(int _i2) {
		i2 = _i2;
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
		
		Rectangle2D botaoObjetivo = new Rectangle2D.Double(x, y, larg, alt);
		g2d.setColor(cores[0]);
		g2d.fill(botaoObjetivo);
		drawStr(g, text, x + larg / 2, y + alt / 2);

	}
	
	protected boolean estaEm(int _x, int _y) {
		if (!clicavel)
			return false;
		int dx = _x - x;
		int dy = _y - y;

		if (dx > 0 && dx < larg && dy > 0 && dy < alt) {
			return true;
		}
		return false;
	}
	
	
	public void setOculto(boolean flag) {
		ativado = flag;
		notificaObservadores();
	}
	
	
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (ativado) {
			if (estaEm(x,y)) {
				i1 = 0;
				notificaObservadores();
			}
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
		if (ativado) {
			boolean dentro = estaEm(x,y);
		
			if (dentro && !estavaEm){
				cores[0] = Color.GRAY;
				i1 = 1;
				notificaObservadores();
				estavaEm = dentro;
			}
			else if (!dentro && estavaEm){
				cores[0] = Color.WHITE;
				i1 = 2;
				notificaObservadores();
				estavaEm = dentro;
			}
		}else {
			cores[0] = Color.GRAY;
			notificaObservadores();
		}
		
		
		
	}
	
	
}
