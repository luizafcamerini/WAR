package View;

import javax.swing.*;

import Model.ModelAPI;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import javax.imageio.ImageIO;

class GamePanel extends JPanel implements MouseListener, MouseMotionListener {
	private Territorio[] territorios;
	// private Image tabuleiro;
	private Territorio foco;
	private InfoPainel iP;
	private Images images = Images.getInstance();
	private GamePanel instance;
    private ViewAPI view = ViewAPI.getInstance();
	private ModelAPI model = ModelAPI.getInstance();

	private int i = 0;

	public GamePanel(InfoPainel iP) {
		// tabuleiro = img;
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		this.iP = iP;

		territorios = Territorio.getTerritorios();
		// Territorio t1 = territorios[i];
	}

	public GamePanel getInstance() {
		if (instance == null) {
			instance = new GamePanel(iP);
		}
		return instance;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image tabuleiro = images.getImage("war_tabuleiro2.png");
		g.drawImage(tabuleiro, 0, 0, getWidth(), getHeight(), null);

		for (Territorio t : territorios) {
			t.draw(g);
		}

		iP.draw(g);
        exibeDadosAtaque(g);
        exibeCartas(g);
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		int botao = iP.mouseClick(x, y);

		if (botao != -1) {
			ViewAPI.getInstance().clickBotao(botao);
            repaint();
			return;
		}

		// SoundEffect.play("src/View/sounds/attack.wav");
		boolean fora = true;
		for (Territorio t : territorios) {

			if (t.estaEm(x, y)) {
				// t.setMarcado(true);
				ViewAPI.getInstance().click(t.getNome());
				fora = false;
				// System.out.println("Está em "+t.getNome());
			}
			;
		}
		if (fora)
			ViewAPI.getInstance().click(null);
		repaint();
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
		if (foco != null) {
			foco.setOculto(false);
			repaint();
			foco = null;
		}
		if (iP.mouseMove(x, y))
			repaint();

		for (Territorio t : territorios) {
			if (t.estaEm(x, y)) {
				t.setOculto(true);
				foco = t;
				repaint();
				// System.out.println("Está em "+t.getNome());
			}
		}

	}

	private void exibeJanela(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		int larg = getWidth() * 80 / 100;
		int alt = getHeight() * 70 / 100;
		int x = getWidth() * 10 / 100;
		int y = getHeight() * 15 / 100;
		Rectangle2D rt = new Rectangle2D.Double(x, y, larg, alt);
		g2d.setColor(Color.CYAN);
		g2d.setStroke(new BasicStroke(30));
		g2d.draw(rt);

		Image fundo = images.getImage("war_tabuleiro_fundo.png");
		g.drawImage(fundo, x, y, larg, alt, null);

		g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
		g2d.drawString("Clique em qualquer lugar para fechar.",x+250,y+25);
	}

	public void exibeDadosAtaque(Graphics g) {
        int[][] dados = view.getListaDados();
        if (dados == null) return;
		Graphics2D g2d = (Graphics2D) g;
		Image imagemDado;
		String imagemDadoStr;
        
		int larg = getWidth() * 80 / 100;
		int alt = getHeight() * 70 / 100;
		int x = getWidth() * 10 / 100;
		int y = getHeight() * 15 / 100;
        int pos_x_ini = x + larg/4;
		int pos_y_ini = y + alt/3;
		int marginLeft = larg/4;
		int marginTop = alt/4;

		exibeJanela(g);

		for (int i = 0; i < dados[0].length; i++) { // dados de ataque
			imagemDadoStr = "dado_ataque_" + dados[0][i] + ".png";
			imagemDado = images.getImage(imagemDadoStr);
			g2d.drawImage(imagemDado, pos_x_ini + marginLeft*i, pos_y_ini, null);
			// g2d.drawImage(imagemDado, pos_x_ini + marginLeft, pos_y_ini + marginLeft, imagemDado.getWidth(), imagemDado.getHeight(null), null);
		}
		pos_y_ini = marginTop * 2;
		for (int i = 0; i < dados[1].length; i++) { // dados de defesa
			imagemDadoStr = "dado_defesa_" + dados[1][i] + ".png";
			imagemDado = images.getImage(imagemDadoStr);
			// g2d.drawImage(imagemDado, pos_x_ini + marginLeft, pos_y_ini, getWidth(), getHeight(), null);
			g2d.drawImage(imagemDado, pos_x_ini + marginLeft*i, pos_y_ini + marginTop, null);
		}
	}

	public void exibeCartas(Graphics g){
        if (!view.getExibeCartas()) return;
		Graphics2D g2d = (Graphics2D) g;
		Image imagemCarta;
		int larg = getWidth() * 80 / 100;
		int alt = getHeight() * 70 / 100;
		int x = getWidth() * 10 / 100;
		int y = getHeight() * 15 / 100;

        exibeJanela(g);

		String [] nomesCartas = model.getCartasJogador();
		//conectar o nome das carteas com as imagens
		for (int i = 0; i < nomesCartas.length; i++) {
			imagemCarta = images.getImage(Territorio.getImgTerritorio(nomesCartas[i]));
			g2d.drawImage(imagemCarta, x + larg/2 + i*imagemCarta.getWidth(null), y + alt/2, null);
		}
	}

}
