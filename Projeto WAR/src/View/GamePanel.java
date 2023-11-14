package View;

import javax.swing.*;

import Model.ModelAPI;
import java.awt.*;
import java.util.ArrayList;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import javax.imageio.ImageIO;

class GamePanel extends JPanel implements MouseListener, ObservadorIF {
	private Territorio[] territorios;
	// private Image tabuleiro;
	// private Territorio foco;
	private InfoPainel iP;
	private Images images = Images.getInstance();
	private GamePanel instance;
    private ViewAPI view = ViewAPI.getInstance();
	private ModelAPI model = ModelAPI.getInstance();
	private boolean fora = true;
	private boolean exibeCartas;
	private boolean exibeTabelas;
	private boolean exibeObjetivo;
	private boolean janelaExibida = false;


	private int i = 0;

	public GamePanel(InfoPainel iP) {
		// tabuleiro = img;
		this.addMouseListener(this);
		// this.addMouseMotionListener(this);
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

	public void notify(ObservadoIF o){
		if (janelaExibida) return;
		if (o instanceof Territorio){
			// Territorio t = (Territorio) o;
			fora = (o.get(1) == 0);
		}

		repaint();
		System.out.println("GamePanel: notificado");
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
		exibeTabelas(g);
		exibeObjetivo(g);
	}

	public void mouseClicked(MouseEvent e) {
		if (janelaExibida){
			exibeObjetivo = false;
			exibeCartas = false;
			exibeTabelas = false;
			janelaExibida = false;
			repaint();
//			return;
		}

		if (fora) {
			ViewAPI.getInstance().click(null);
			repaint();
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


	private void exibeJanela(Graphics g) {
		janelaExibida = true;
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
        if (!exibeCartas) return;
		// janelaExibida = true;
		Graphics2D g2d = (Graphics2D) g;
		Image imagemCarta;
		int larg = getWidth() * 80 / 100;
		int alt = getHeight() * 70 / 100;
		int x = getWidth() * 10 / 100;
		int y = getHeight() * 15 / 100;
        
        int pos_x_ini = x + 30;
		int pos_y_ini = y + alt/3;
		int marginLeft = larg/5;

        exibeJanela(g);

		String [] nomesCartas = model.getCartasJogador();
		//conectar o nome das carteas com as imagens
		for (int i = 0; i < nomesCartas.length; i++) {
			imagemCarta = images.getImage(Territorio.getImgTerritorio(nomesCartas[i]));
			g2d.drawImage(imagemCarta, pos_x_ini + i*marginLeft, pos_y_ini, null);
		}
	}

	public void exibeTabelas(Graphics g){
		if(!exibeTabelas) return;
		// janelaExibida = true;
		Graphics2D g2d = (Graphics2D) g;
		Image imagemTabelaExe = images.getImage("war_tabela_troca.png");
		Image imagemTabelaBonusCont = images.getImage("war_tabela_bonus_continente.png");
		int larg = getWidth() * 80 / 100;
		// int alt = getHeight() * 70 / 100;
		int x = getWidth() * 35 / 100;
		int y = getHeight() * 40 / 100;
		
		int pos_x_ini = x;
		int pos_y_ini = y;
		int marginLeft = larg/5;
		exibeJanela(g);

		g2d.drawImage(imagemTabelaExe, pos_x_ini, pos_y_ini, null);
		g2d.drawImage(imagemTabelaBonusCont, pos_x_ini + marginLeft, pos_y_ini, null);
	}

	public void exibeObjetivo(Graphics g){
		if(!exibeObjetivo) return;
		// janelaExibida = true;
		Graphics2D g2d = (Graphics2D) g;
		Image imagemObjetivo = images.getImage(model.getImgNameObjetivo());

		int x = getWidth() * 40 / 100 + 35;
		int y = getHeight() * 35 / 100;
		int pos_x_ini = x;
		int pos_y_ini = y;

		exibeJanela(g);
		g2d.drawImage(imagemObjetivo, pos_x_ini, pos_y_ini, null);
	}

	public void setExibeCartas(boolean b){
		exibeCartas = b;
		repaint();
	}

	public void setExibeTabelas(boolean b){
		exibeTabelas = b;
		repaint();
	}

	public void setExibeObjetivo(boolean b){
		exibeObjetivo = b;
		repaint();
	}

}


