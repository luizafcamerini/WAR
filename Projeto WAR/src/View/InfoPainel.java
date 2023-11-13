package View;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class InfoPainel implements ObservadoIF, MouseListener, MouseMotionListener {
	private String msg;
	int x, y, alt, larg;
	Color cores[];
	boolean selecionados[];
	private List<ObservadorIF> lst=new ArrayList<ObservadorIF>();

	public InfoPainel(int _x, int _y, int largura, int altura) {
		x = _x;
		y = _y;
		larg = largura;
		alt = altura;
		cores = new Color[4];
		selecionados = new boolean[4];
		for (int i = 0; i < 4; i++) {
			cores[i] = Color.WHITE;
			selecionados[i] = false;
		}

	}

	public void addObservador(ObservadorIF o) {
		lst.add(o);
	}

	public void removeObservador(ObservadorIF o) {
		lst.remove(o);
	}

	public int get(int i){
		return 0;
	}

	public void setInfo(int etapa, String cor, int qtdExe) {
		switch (etapa) {
			case 0:
				msg = String.format("Jogador: %s\nEtapa: Posicionamento\nQtd exércitos: %d", cor, qtdExe);
                break;
			case 10:
				msg = String.format("Jogador: %s\nEtapa: Ataque\n", cor);
                break;
			case 20:
				msg = String.format("Jogador: %s\nEtapa: Deslocamento", cor);
                break;
            case 30:
                msg  = String.format("Jogador: %s\nEtapa: Recebimento de carta", cor);
                break;
            default:
                msg = null;
		}
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

	private void drawStr(Graphics g, String text, int x, int y) {
		g.setColor(Color.BLACK);

		FontMetrics metrics = g.getFontMetrics(g.getFont());
		x = x - metrics.stringWidth(text) / 2;
		y = y - metrics.getHeight() / 2 + metrics.getAscent();

		g.drawString(text, x, y);
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		Rectangle2D rt = new Rectangle2D.Double(x, y, larg, alt);
		g2d.setColor(Color.BLACK);
		g2d.fill(rt);
		g.setColor(Color.WHITE);
		drawStringMultiLine(g, msg, 10, x + 5, y + 20);

		Rectangle2D botaoObjetivo = new Rectangle2D.Double(x + 5, y + alt / 2 + 2, (larg / 2) - 10, alt / 4 - 4);
		g2d.setColor(cores[0]);
		g2d.fill(botaoObjetivo);
		drawStr(g, "OBJETIVO", x + larg / 4, y + 5 * alt / 8);

		Rectangle2D botaoCartas = new Rectangle2D.Double(x + (larg / 2) + 5, y + alt / 2 + 2, (larg / 2) - 10,
				alt / 4 - 4);
		g2d.setColor(cores[1]);
		g2d.fill(botaoCartas);
		drawStr(g, "CARTAS", x + 3 * larg / 4, y + 5 * alt / 8);

		Rectangle2D botaoTabela = new Rectangle2D.Double(x + 5, y + 3 * alt / 4 + 2, (larg / 2) - 10, alt / 4 - 4);
		g2d.setColor(cores[2]);
		g2d.fill(botaoTabela);
		drawStr(g, "TABELAS", x + larg / 4, y + 7 * alt / 8);

		Rectangle2D botaoEtapa = new Rectangle2D.Double(x + (larg / 2) + 5, y + 3 * alt / 4 + 2, (larg / 2) - 10,
				alt / 4 - 4);
		g2d.setColor(cores[3]);
		g2d.fill(botaoEtapa);
		drawStr(g, "PRÓX. ETAPA", x + 3 * larg / 4, y + 7 * alt / 8);
	}

	public int mouseClick(int _x, int _y) {
		int _larg = (larg / 2) - 10;
		int _alt = alt / 4 - 4;
		int dx, dy;

		dx = _x - (x + 5);
		dy = _y - (y + alt / 2 + 2);

		if (dx > 0 && dx < _larg && dy > 0 && dy < _alt) {
			return 0;
		}

		dx = _x - (x + (larg / 2) + 5);
		dy = _y - (y + alt / 2 + 2);

		if (dx > 0 && dx < _larg && dy > 0 && dy < _alt) {
			return 1;
		}

		dx = _x - (x + 5);
		dy = _y - (y + 3 * alt / 4 + 2);

		if (dx > 0 && dx < _larg && dy > 0 && dy < _alt) {
			return 2;
		}

		dx = _x - (x + (larg / 2) + 5);
		dy = _y - (y + 3 * alt / 4 + 2);

		if (dx > 0 && dx < _larg && dy > 0 && dy < _alt) {
			return 3;
		}

		return -1;
	}

	public boolean mouseMove(int _x, int _y) {
		int _larg = (larg / 2) - 10;
		int _alt = alt / 4 - 4;
		int dx, dy;
		boolean flag = false;

		dx = _x - (x + 5);
		dy = _y - (y + alt / 2 + 2);

		if (dx > 0 && dx < _larg && dy > 0 && dy < _alt) {
			cores[0] = Color.GRAY;
			if (!selecionados[0])
				flag = true;
			selecionados[0] = true;
		} else {
			cores[0] = Color.WHITE;
			if (selecionados[0])
				flag = true;
			selecionados[0] = false;
		}

		dx = _x - (x + (larg / 2) + 5);
		dy = _y - (y + alt / 2 + 2);

		if (dx > 0 && dx < _larg && dy > 0 && dy < _alt) {
			cores[1] = Color.GRAY;
			if (!selecionados[1])
				flag = true;
			selecionados[1] = true;
		} else {
			cores[1] = Color.WHITE;
			if (selecionados[1])
				flag = true;
			selecionados[1] = false;
		}

		dx = _x - (x + 5);
		dy = _y - (y + 3 * alt / 4 + 2);

		if (dx > 0 && dx < _larg && dy > 0 && dy < _alt) {
			cores[2] = Color.GRAY;
			if (!selecionados[2])
				flag = true;
			selecionados[2] = true;
		} else {
			cores[2] = Color.WHITE;
			if (selecionados[2])
				flag = true;
			selecionados[2] = false;
		}

		dx = _x - (x + (larg / 2) + 5);
		dy = _y - (y + 3 * alt / 4 + 2);

		if (dx > 0 && dx < _larg && dy > 0 && dy < _alt) {
			cores[3] = Color.GRAY;
			if (!selecionados[3])
				flag = true;
			selecionados[3] = true;
		} else {
			cores[3] = Color.WHITE;
			if (selecionados[3])
				flag = true;
			selecionados[3] = false;
		}

		return flag;
	}

	private void notificaObservadores(){
		for(ObservadorIF o: lst){
			o.notify(this);
		}
	}
	
	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int botao = mouseClick(x, y);
		if (botao != -1){
			// notificaObservadores();
			ViewAPI.getInstance().clickBotao(botao);
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

		if (mouseMove(x, y)){
			// notificaObservadores();
		}
	}

}
