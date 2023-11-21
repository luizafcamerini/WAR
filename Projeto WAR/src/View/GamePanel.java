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
	private boolean manual = false;
	
	private boolean exibeAtaque = false;
	private String atacante;
	private String defensor;
	private int nAtaque;
	private int nDefesa;
	
	JComboBox cbDados[];
	JButton bManual, bAuto, bAtaque;
	


	private int i = 0;

	public GamePanel(InfoPainel iP) {
		// tabuleiro = img;
		this.addMouseListener(this);
		// this.addMouseMotionListener(this);
		this.iP = iP;

		territorios = Territorio.getTerritorios();
		// Territorio t1 = territorios[i];
		
		
		String[] vm={"1","2","3","4","5","6"};
		cbDados = new JComboBox[6];
		for (int i = 0; i < 6; i++) {
			final int index = i;
			cbDados[i]=new JComboBox(vm);
			add(cbDados[i]);
			cbDados[i].setEditable(false);
			cbDados[i].setVisible(false);
			cbDados[i].setSelectedIndex(-1);
			cbDados[i].addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						int valor = Integer.parseInt((String) cbDados[index].getSelectedItem());
						view.setDado(index, valor);
						repaint();
					}
				}
			});
		}
		
		
		bManual = new JButton("Modo Manual");
		bAuto = new JButton("Modo automático");
		bAtaque = new JButton("ATACAR");
		
        bManual.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manual = true;
                
                repaint();
            }
        });
        
        bAuto.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manual = false;
                
            	for (int i = 0; i < 6; i++) {
            		cbDados[i].setSelectedIndex(-1);
            		view.setDado(i, 0);
            	}
                repaint();
            }
        });
        
        bAtaque.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                repaint();
            }
        });
        
        bManual.setVisible(false);
        bAuto.setVisible(false);
        bAtaque.setVisible(false);
        add(bManual);
        add(bAuto);
        add(bAtaque);
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
			fora = (o.get(1) == 0);
		}

		repaint();
//		System.out.println("GamePanel: notificado");
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image tabuleiro = images.getImage("war_tabuleiro2.png");
		g.drawImage(tabuleiro, 0, 0, getWidth(), getHeight(), null);

		for (Territorio t : territorios) {
			t.draw(g);
		}

		iP.draw(g);
//        exibeDadosAtaque(g);
        exibeTelaAtaque(g);
        exibeCartas(g);
		exibeTabelas(g);
		exibeObjetivo(g);
	}

	public void mouseClicked(MouseEvent e) {
		if (fora) {
			ViewAPI.getInstance().click(null);
			repaint();
		}
		
		if (janelaExibida){
			exibeObjetivo = false;
			exibeCartas = false;
			exibeTabelas = false;
			janelaExibida = false;
			exibeAtaque = false;
			
			bAtaque.setVisible(false);
			bManual.setVisible(false);
			bAuto.setVisible(false);
			for (JComboBox cb: cbDados) {
				cb.setVisible(false);
			}
			
			repaint();
//			return;
		}

		System.out.printf("Fora = %s\n", fora?"true":"false");
		
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
		fora = true;
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
		g2d.drawString("Clique em quase qualquer lugar para fechar.",x+250,y+25);
		
	}
	
	public void ataque(String atacante, String defensor, int nAtaque, int nDefesa) {
		exibeAtaque = true;
		this.atacante = atacante;
		this.defensor = defensor;
		this.nAtaque = nAtaque;
		this.nDefesa = nDefesa;
		repaint();
		
	}
	
	private void drawStr(Graphics g, String text, int x, int y) {

		FontMetrics metrics = g.getFontMetrics(g.getFont());
		x = x - metrics.stringWidth(text) / 2;
		y = y - metrics.getHeight() / 2 + metrics.getAscent();

		g.drawString(text, x, y);
	}
	
	public void exibeTelaAtaque(Graphics g) {
		if (!exibeAtaque) return;
		int[][] dados = view.getListaDados();
        if (dados == null) return;
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 24));
        
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
		int space = 40;
		int x_centro = getWidth()/2;

		int y_inferior = getHeight() - y;
		FontMetrics m = g.getFontMetrics();

		exibeJanela(g);
		
		
		

		for (int i = 0; i < dados[0].length; i++) { // dados de ataque
			imagemDadoStr = "dado_ataque_" + dados[0][i] + ".png";
			imagemDado = images.getImage(imagemDadoStr);
			g2d.drawImage(imagemDado, pos_x_ini + marginLeft*i, pos_y_ini, null);
			cbDados[i].setBounds(pos_x_ini + marginLeft*i + space, pos_y_ini, cbDados[i].getPreferredSize().width, cbDados[i].getPreferredSize().height);
			cbDados[i].setVisible(manual);
			// g2d.drawImage(imagemDado, pos_x_ini + marginLeft, pos_y_ini + marginLeft, imagemDado.getWidth(), imagemDado.getHeight(null), null);
		}
		pos_y_ini = marginTop * 2;
		for (int i = 0; i < dados[1].length; i++) { // dados de defesa
			imagemDadoStr = "dado_defesa_" + dados[1][i] + ".png";
			imagemDado = images.getImage(imagemDadoStr);
			// g2d.drawImage(imagemDado, pos_x_ini + marginLeft, pos_y_ini, getWidth(), getHeight(), null);
			g2d.drawImage(imagemDado, pos_x_ini + marginLeft*i , pos_y_ini + marginTop, null);
			cbDados[i+3].setBounds(pos_x_ini + marginLeft*i + space, pos_y_ini + marginTop, cbDados[i+3].getPreferredSize().width, cbDados[i+3].getPreferredSize().height);
			cbDados[i+3].setVisible(manual);
		}
		
		bAtaque.setVisible(true);
		bManual.setVisible(true);
		bAuto.setVisible(true);
		
		
		bManual.setBounds(getWidth()/3-bManual.getPreferredSize().width/2, y + m.getHeight()*2, bManual.getPreferredSize().width, bManual.getPreferredSize().height);
		bAuto.setBounds(2*getWidth()/3-bAuto.getPreferredSize().width/2, y + m.getHeight()*2, bAuto.getPreferredSize().width, bAuto.getPreferredSize().height);
		bAtaque.setBounds((getWidth()-bAtaque.getPreferredSize().width)/2, y_inferior - m.getHeight(), bAtaque.getPreferredSize().width, bAtaque.getPreferredSize().height);
		
		drawStr(g2d, String.format("%s atacando %s",atacante, defensor),x_centro,y_inferior - m.getHeight()*3);
		drawStr(g2d, String.format("%d X %d", dados[0].length, dados[1].length),x_centro,y_inferior - m.getHeight()*2);
	
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

	public boolean exibeVencedor(String corVencedor){
		JOptionPane.showMessageDialog(null,"O jogador " + corVencedor + " venceu!", "Fim de jogo!", JOptionPane.INFORMATION_MESSAGE);
		int resposta = JOptionPane.showConfirmDialog(null, "Jogar novamente?", "Continuar?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (resposta == JOptionPane.YES_OPTION) return true;
		else return false;
	}

}


