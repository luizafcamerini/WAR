package View;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;

import Observer.ObservadoIF;
import Observer.ObservadorIF;

/*
 * idObservador = ID do observador
 * 
 * acaoMouse = -1 // ocorreu mundança na mensagem exibida
 * acaoMouse = 0 // Mouse clicou em algum botao
 * acaoMouse = 1 // Mouse "entrou" em algum botao
 * acaoMouse = 2 // Mouse "saiu" de algum botao
 * 
 * indexBotaoClicado = 0 // Botao clicado foi o 0
 * indexBotaoClicado = 1 // Botao clicado foi o 1
 * indexBotaoClicado = 2 // Botao clicado foi o 2
 * indexBotaoClicado = 3 // Botao clicado foi o 3
 * 
 */

class InfoPainel implements ObservadoIF, ObservadorIF {
	private String msg;
	private int x, y, alt, larg;
	private Botao botoes[];
	private List<ObservadorIF> lst = new ArrayList<ObservadorIF>();
	private int acaoMouse, idObservador, indexBotaoClicado;
	private final int MUDANCA_MSG = -1;
	private final int CLICK_BOTAO = 0;
	private final int ENTROU_BOTAO = 1;
	private final int SAIU_BOTAO = 2;

	public InfoPainel(int x, int y, int largura, int altura) {
		/** Construtor que cria os botoes do InfoPanel e os configura. */
		this.x = x;
		this.y = y;
		larg = largura;
		alt = altura;
		botoes = new Botao[4];
		botoes[0] = new Botao(x + 5, y + alt / 2 + 2, (larg / 2) - 10, alt / 4 - 4, "OBJETIVO");
		botoes[1] = new Botao(x + (larg / 2) + 5, y + alt / 2 + 2, (larg / 2) - 10, alt / 4 - 4, "CARTAS");
		botoes[2] = new Botao(x + 5, y + 3 * alt / 4 + 2, (larg / 2) - 10, alt / 4 - 4, "TABELAS");
		botoes[3] = new Botao(x + (larg / 2) + 5, y + 3 * alt / 4 + 2, (larg / 2) - 10, alt / 4 - 4, "PRÓX. ETAPA");

		for (int i = 0; i < botoes.length; i++) {
			botoes[i].addObservador(this);
			botoes[i].setIDBotao(i);
			botoes[i].setClivael(false);
		}

	}

	public void atualizaListeners(GamePanel gP) {
		/** Metodo que atualiza os listeners de acao dos mouses do InfoPanel. */
		for (Botao b : botoes) {
			gP.addMouseListener(b);
			gP.addMouseMotionListener(b);
		}
	}

	public void addObservador(ObservadorIF o) {
		/** Metodo que adiciona um observador. */
		lst.add(o);
	}

	public void removeObservador(ObservadorIF o) {
		/** Metodo que remove um observador. */
		lst.remove(o);
	}

	public int get(int i) {
		/** Metodo que retorna acao do mouse ou observador ou botao clicado. */
		if (i == 1)
			return acaoMouse;
		else if (i == 2)
			return idObservador;
		else if (i == 3)
			return indexBotaoClicado;
		return -2;
	}

	public void notify(ObservadoIF o) {
		/** Metodo que notifica se um botao foi clicado. */
		acaoMouse = o.get(1);
		if (acaoMouse == CLICK_BOTAO) {
			indexBotaoClicado = o.get(2);
		}
		notificaObservadores();
	}

	private void notificaObservadores() {
		/** Metodo que notifica seus observadores. */
		for (ObservadorIF o : lst) {
			o.notify(this);
		}
	}

	public void setInfo(String mensagem) {
		/** Metodo que muda a mensagem do InfoPainel. */
		msg = mensagem;
		acaoMouse = MUDANCA_MSG;
		notificaObservadores();
	}

	public void setClivael(boolean b){
		/** Metodo define se os botoes do InfoPainel estao clicaveis ou nao. */
		for(Botao botao : botoes){
			botao.setClivael(b);
		}
	}

	public void setIDObservador(int idObservador) {
		/** Metodo que define o id do observador. */
		this.idObservador = idObservador;
	}

	private void drawStringMultiLine(Graphics g, String text, int lineWidth, int x, int y) {
		/** Metodo que escreve o texto do InfoPainel com multiplas linhas. */
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
		/** Metodo que desenha o InfoPainel. */
		Graphics2D g2d = (Graphics2D) g;

		Rectangle2D rt = new Rectangle2D.Double(x, y, larg, alt);
		g2d.setColor(Color.BLACK);
		g2d.fill(rt);
		g.setFont(new Font(null));
		g.setColor(Color.WHITE);
		drawStringMultiLine(g, msg, 10, x + 5, y + 20);

		for (Botao b : botoes)
			b.draw(g);
	}

}
