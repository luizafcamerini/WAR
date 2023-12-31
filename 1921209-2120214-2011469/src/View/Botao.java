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

class Botao implements ObservadoIF, MouseListener, MouseMotionListener {
	private int x, y, alt, larg;
	private String text;
	private boolean clicavel = true;
	private Color cores[];
	/**
	 * cores[0] = cor do botao ativado
	 * cores[1] = cor do botao com o mouse em cima
	 * cores[2] = cor do botao desativado
	 */
	private int cor = 0; // Indice na lista de cores do botao
	private boolean estavaEm = false;
	private int acaoBotao, idBotao, idComplementarBotao;
	private final int CLICK_BOTAO = 0;
	private final int ENTROU_BOTAO = 1;
	private final int SAIU_BOTAO = 2;

	// Lista de seus observadores:
	private List<ObservadorIF> lst = new ArrayList<ObservadorIF>();

	public void addObservador(ObservadorIF o) {
		/** Metodo que adiciona um observador. */
		lst.add(o);
	}

	public void removeObservador(ObservadorIF o) {
		/** Metodo que remove um observador. */
		lst.remove(o);
	}

	public int get(int i) {
		/**
		 * Metodo que retorna a acao do botao (1), id do botao (2) ou ID complementar do
		 * botao (3).
		 */
		if (i == 1)
			return acaoBotao;
		else if (i == 2)
			return idBotao;
		else if (i == 3)
			return idComplementarBotao;
		return 0;
	}

	private void notificaObservadores() {
		/** Metodo que notifica todos os observadores da lista de observadores. */
		for (ObservadorIF o : lst) {
			o.notify(this);
		}
	}

	{
		/** Bloco estatico que define uma lista de cores a serem usadas. */
		cores = new Color[3];
		cores[0] = Color.WHITE;
		cores[1] = Color.GRAY;
		cores[2] = new Color(64, 64, 64);
	}

	public Botao(int _x, int _y, int _larg, int _alt, String _text) {
		/** Metodo que define posicao, tamanho e texto do botao. */
		x = _x;
		y = _y;
		larg = _larg;
		alt = _alt;
		text = _text;
	}

	public Botao(String text) {
		/** Metodo que define o texto do botao. */
		this.text = text;
	}

	public void setBounds(int x, int y, int larg, int alt) {
		/** Metodo que define tamanho e posicao do botao. */
		this.x = x;
		this.y = y;
		this.larg = larg;
		this.alt = alt;
	}

	public void setIDBotao(int idBotao) {
		/** Metodo que define o ID do botao. */
		this.idBotao = idBotao;
	}

	public void setIDComplementar(int idComplementarBotao) {
		/** Metodo que define o ID complementar do botao. */
		this.idComplementarBotao = idComplementarBotao;
	}

	public void setColor(int i, Color color) {
		/** Metodo que define a cor do botao em um certo estado. */
		cores[i] = color;
	}

	private void drawStr(Graphics g, String text, int x, int y) {
		/** Metodo que escreve um texto em uma posicao. */
		g.setColor(Color.BLACK);
		FontMetrics metrics = g.getFontMetrics(g.getFont());
		x = x - metrics.stringWidth(text) / 2;
		y = y - metrics.getHeight() / 2 + metrics.getAscent();
		g.drawString(text, x, y);
	}

	public void draw(Graphics g) {
		/** Metodo que desenha o botao. */
		Graphics2D g2d = (Graphics2D) g;
		Rectangle2D rect = new Rectangle2D.Double(x, y, larg, alt);
		g2d.setColor(cores[cor]);
		g2d.fill(rect);
		drawStr(g, text, x + larg / 2, y + alt / 2);
	}

	public void setPos(Graphics g, int xc, int yc) {
		/**
		 * Metodo que define o tamanho e posicao do botao a partir de uma posicao
		 * central. O tamanho definido em setBounds() permanece o mesmo.
		 */
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
		/** Metodo que retorna se o mouse esta em cima do botao. */
		// Caso o botao nao esta clicavel, a acao é ignorada.
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
		/** Metodo que define se o botao e clicavel ou nao. */
		clicavel = b;
		if (!b) {
			cor = 2;
			estavaEm = false;
		} else {
			cor = 0;
		}
	}

	public void mouseClicked(MouseEvent e) {
		/** Metodo que notifica se o botao foi clicado. */
		int x = e.getX();
		int y = e.getY();
		if (estaEm(x, y)) {
			acaoBotao = CLICK_BOTAO;
			notificaObservadores();
		}
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		/**
		 * Metodo que muda as cores do botao em relacao a posicao do mouse dentro ou
		 * fora.
		 */
		int x = e.getX();
		int y = e.getY();
		boolean dentro = estaEm(x, y);
		if (dentro && !estavaEm) {
			cor = 1;
			acaoBotao = ENTROU_BOTAO;
			notificaObservadores();
			estavaEm = dentro;

		} else if (!dentro && estavaEm) {
			cor = 0;
			acaoBotao = SAIU_BOTAO;
			notificaObservadores();
			estavaEm = dentro;
		}
	}

	public boolean atualiza(Graphics g, int _x, int _y) {
		/**
		 * Metodo que recebe as coordenadas da posição do mouse, atualiza a cor do botão
		 * e retorna se o mouse está em cima ou nao.
		 */
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
