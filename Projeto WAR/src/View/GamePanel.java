package View;

import javax.swing.*;

import Controller.ControllerAPI;
import Model.ModelAPI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.font.*;

class GamePanel extends JPanel implements MouseListener, ObservadorIF {
	private final Color[] cores = { Color.YELLOW, Color.BLUE, Color.WHITE, Color.BLACK, Color.GREEN, Color.RED };
	private final String[] coresStr = { "AMARELO", "AZUL", "BRANCO", "PRETO", "VERDE", "VERMELHO" };

	private Territorio[] territorios;
	private InfoPainel iP;
	private Images images = Images.getInstance();
	private GamePanel instance;
	private ViewAPI view = ViewAPI.getInstance();
	private ModelAPI model = ModelAPI.getInstance();
	private ControllerAPI control = ControllerAPI.getInstance();
	private boolean fora = true;
	private boolean exibeCartas;
	private boolean exibeTabelas;
	private boolean exibeObjetivo;
	private boolean janelaExibida = false;
	private boolean manual = false;

	private boolean exibeAtaque = false;
	private boolean exibeResultadoAtaque = false;
	private boolean exibeConquista = false;
	private String atacante;
	private String defensor;

	private long ultimoClique = 0;
	Territorio temp1, temp2;

	JComboBox cbDados[];
	// JButton bManual, bAuto, bAtaque, bAtaqueN;
	Botao bManual, bAuto, bAtaque, bAtaqueN;

	private void configBotao(Botao b, int i2){
		b.setI2(i2);
		b.addObservador(this);
		addMouseListener(b);
		addMouseMotionListener(b);
	}

	public GamePanel(InfoPainel iP) {
		this.addMouseListener(this);
		this.iP = iP;

		territorios = Territorio.getTerritorios();

		String[] vm = { "1", "2", "3", "4", "5", "6" };
		cbDados = new JComboBox[6];
		for (int i = 0; i < 6; i++) {
			final int index = i;
			cbDados[i] = new JComboBox(vm);
			add(cbDados[i]);
			cbDados[i].setEditable(false);
			cbDados[i].setVisible(false);
			cbDados[i].setSelectedIndex(0);
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

		bManual = new Botao("Modo Manual");
		bAuto = new Botao("Modo automático");
		bAtaque = new Botao("ATACAR");
		bAtaqueN = new Botao("ATACAR NOVAMENTE");

		configBotao(bManual, 1);
		configBotao(bAuto, 2);
		configBotao(bAtaque, 3);
		configBotao(bAtaqueN, 4);


		bManual.setClivael(false);
		bAuto.setClivael(false);
		bAtaque.setClivael(false);
		bAtaqueN.setClivael(false);

	}

	public GamePanel getInstance() {
		if (instance == null) {
			instance = new GamePanel(iP);
		}
		return instance;
	}

	public void notify(ObservadoIF o) {
		int i1 = o.get(1);
		int i2 = o.get(2);
		System.out.printf("i1 = %d, i2 = %d\n",i1, i2);

		if (janelaExibida && (i2 < 1))
			return;
		
		
		// Mouse clicou em algo
		if (i1 == 0){
			// Realiza "debounce" do clique
			long tempoAtual = System.currentTimeMillis();
			System.out.printf("DELTA = %d\n",tempoAtual - ultimoClique);
			if (tempoAtual - ultimoClique < 100)
				return;
			ultimoClique = tempoAtual;


			// Clicou em um território
			if (i2 == -1 || i2 == 5 || i2 == 6){
				Territorio t = (Territorio) o;
				view.click(t.getNome());
			}
			
			// Clique em botão do infoPanel
			if (i2 == -2){
				int i3 = o.get(3);
				System.out.printf("i3 = %d\n",i3);


				if (i3 == 0){
					exibeObjetivo = true;
					fora = true;
				}
				if (i3 == 1){
					exibeCartas = true;
					fora = true;
				}
				if (i3 == 2){
					exibeTabelas = true;
					fora = true;
				}
				if (i3 == 3){
					control.proxEtapa();
				}
			}


			// Ação do botao "bManual"
			else if (i2 == 1){
				manual = true;
				for (int i = 0; i < 6; i++) {
					view.setDado(i, Integer.parseInt((String) cbDados[i].getSelectedItem()));
				}
			}

			// Ação do botao "bAuto"
			else if (i2==2){
				manual = false;
				for (int i = 0; i < 6; i++) {
					view.setDado(i, 0);
				}
			}

			// Ação do botao "bAtaque"
			else if (i2==3){
				fora = true;
				if (!manual) {
					control.ataque(atacante, defensor);
					repaint();
				} else {
					control.ataque(atacante, defensor, view.getListaDados());
				}
			}

			// Ação do botao "bAtaqueN"
			else if (i2==4){
				control.ataca(atacante, defensor);
				fora = true;
			}

		}

		
		
		
		// Mouse "entrou" em algo
		if (i1 == 1) {
			fora = false;
		}

		// Mouse "saiu" de algo
		else if (i1 == 2) {
			fora = true;
		}

		System.out.printf("Fora = %s\n", fora ? "true" : "false");
			
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Image tabuleiro = images.getImage("war_tabuleiro2.png");
		g.drawImage(tabuleiro, 0, 0, getWidth(), getHeight(), null);

		for (Territorio t : territorios) {
			t.draw(g);
		}

		iP.draw(g);
		
		exibeTelaAtaque(g);
		exibeTelaResultadoAtaque(g);
		exibeTelaConquista(g);
		exibeCartas(g);
		exibeTabelas(g);
		exibeObjetivo(g);
	}

	public void mouseClicked(MouseEvent e) {
		if (fora) {
			// "debounce" do clique
			long tempoAtual = System.currentTimeMillis();
			System.out.printf("DELTA = %d\n",tempoAtual - ultimoClique);
			if (tempoAtual - ultimoClique < 100)
				return;
			ultimoClique = tempoAtual;

			// Clique fora ocorrido
			view.click(null);
			if (janelaExibida) {				
				limpaJanela();
			}

			// Verifica se mouse "está em cima" de algo
			MouseMotionListener[] listeners = getMouseMotionListeners();
			for (MouseMotionListener listener : listeners) {
				if (listener instanceof Botao){
					Botao b = (Botao) listener;
					if (b.estaEm(e.getX(), e.getY()))
					fora = false;
				}
				else if (listener instanceof Territorio){
					Territorio t = (Territorio) listener;
					if (t.estaEm(e.getX(), e.getY()))
					fora = false;
				}
				if (!fora)
					break;
			}
			repaint();
		}

		System.out.printf("Fora = %s\n", fora ? "true" : "false");

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
		g2d.drawString("Clique em quase qualquer lugar para fechar.", x + 250, y + 25);

	}

	private void limpaJanela() {
		exibeObjetivo = false;
		exibeCartas = false;
		exibeTabelas = false;
		janelaExibida = false;
		exibeAtaque = false;
		exibeResultadoAtaque = false;

		bManual.setClivael(false);
		bAuto.setClivael(false);
		bAtaque.setClivael(false);
		bAtaqueN.setClivael(false);

		if (exibeConquista) {
			model.desregistra(temp1.getNome(), temp1);
			model.desregistra(temp2.getNome(), temp2);

			temp1.removeObservador(this);
			temp2.removeObservador(this);

			removeMouseListener(temp1);
			removeMouseListener(temp2);

			removeMouseMotionListener(temp1);
			removeMouseMotionListener(temp2);

			temp1 = null;
			temp2 = null;

			exibeConquista = false;
		}


		for (JComboBox cb : cbDados) {
			cb.setVisible(false);
		}
	}

	public void ataque(String atacante, String defensor) {
		limpaJanela();
		fora = true;

		if (manual) {
			for (int i = 0; i < 6; i++) {
				view.setDado(i, Integer.parseInt((String) cbDados[i].getSelectedItem()));
			}
		}

		exibeAtaque = true;
		this.atacante = atacante;
		this.defensor = defensor;
		repaint();
	}

	public void resultadoAtaque() {

		System.out.println("Exibe resultado ataque");
		limpaJanela();
		exibeResultadoAtaque = true;

	}

	public void conquista() {

		limpaJanela();
		exibeConquista = true;

		int y = getHeight() * 15 / 100;
		int y_inferior = getHeight() - y;
		int x_centro = getWidth() / 2;
		int space = 50;

		exibeConquista = true;
		int n, cor;

		// Mostra territorio "temporário" do atacante
		temp1 = new Territorio(atacante, x_centro - space, y_inferior - 50);
		n = model.getQtdExercitos(atacante);
		temp1.setNum(n);
		cor = model.getCor(atacante);
		temp1.setCor(cores[cor]);
		temp1.setClicavel(true);
		temp1.setMarcado(true);

		temp1.addObservador(this);
		model.registra(temp1.getNome(), temp1);
		addMouseListener(temp1);
		addMouseMotionListener(temp1);

		// Mostra territorio "temporário" do antigo defensor
		temp2 = new Territorio(defensor, x_centro + space, y_inferior - 50);

		n = model.getQtdExercitos(defensor);
		temp2.setNum(n);
		cor = model.getCor(defensor);
		temp2.setCor(cores[cor]);
		temp2.setClicavel(true);
		temp2.setMarcado(true);

		temp2.addObservador(this);
		model.registra(temp2.getNome(), temp2);
		addMouseListener(temp2);
		addMouseMotionListener(temp2);

		temp1.setI2(5);
		temp2.setI2(6);

	}

	public void conquista(int n1, int n2) {
		if (temp1 != null) {
			temp1.setNum(n1);
		}
		if (temp2 != null) {
			temp2.setNum(n2);
		}
	}

	private void drawStr(Graphics g, String text, int x, int y) {

		FontMetrics metrics = g.getFontMetrics(g.getFont());
		x = x - metrics.stringWidth(text) / 2;
		y = y - metrics.getHeight() / 2 + metrics.getAscent();

		g.drawString(text, x, y);
	}

	private void exibeDados(Graphics g) {
		int[][] dados = view.getListaDados();
		if (dados == null)
			return;

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.PLAIN, 24));

		Image imagemDado;
		String imagemDadoStr;

		int larg = getWidth() * 80 / 100;
		int alt = getHeight() * 70 / 100;
		int x = getWidth() * 10 / 100;
		int y = getHeight() * 15 / 100;
		int pos_x_ini = x + larg / 4;
		int pos_y_ini = y + alt / 3;
		int marginLeft = larg / 4;
		int marginTop = alt / 4;
		int space = 40;

		for (int i = 0; i < dados[0].length; i++) { // dados de ataque
			imagemDadoStr = "dado_ataque_" + dados[0][i] + ".png";
			imagemDado = images.getImage(imagemDadoStr);
			g2d.drawImage(imagemDado, pos_x_ini + marginLeft * i, pos_y_ini, null);
			cbDados[i].setBounds(pos_x_ini + marginLeft * i + space, pos_y_ini, cbDados[i].getPreferredSize().width,
					cbDados[i].getPreferredSize().height);
			// g2d.drawImage(imagemDado, pos_x_ini + marginLeft, pos_y_ini + marginLeft,
			// imagemDado.getWidth(), imagemDado.getHeight(null), null);
		}
		
		for (int i = 0; i < dados[1].length; i++) { // dados de defesa
			imagemDadoStr = "dado_defesa_" + dados[1][i] + ".png";
			imagemDado = images.getImage(imagemDadoStr);
			// g2d.drawImage(imagemDado, pos_x_ini + marginLeft, pos_y_ini, getWidth(),
			// getHeight(), null);
			g2d.drawImage(imagemDado, pos_x_ini + marginLeft * i, pos_y_ini + marginTop, null);
			cbDados[i + 3].setBounds(pos_x_ini + marginLeft * i + space, pos_y_ini + marginTop,
					cbDados[i + 3].getPreferredSize().width, cbDados[i + 3].getPreferredSize().height);
		}
	}

	public void exibeTelaAtaque(Graphics g) {
		if (!exibeAtaque)
			return;
		int[][] dados = view.getListaDados();
		if (dados == null)
			return;

			
		Font fonte = new Font("Arial", Font.PLAIN, 24);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.setFont(fonte);
		FontRenderContext frc = g2d.getFontRenderContext();
		LineMetrics lm = fonte.getLineMetrics("", frc);


		int y_superior = getHeight() * 15 / 100; // Altura da borda superior da tela
		int y_inferior = getHeight() - y_superior; // Altura da borda inferior da tela
		int fAlt = (int)lm.getHeight(); // altura da fonte

		int x_centro = getWidth() / 2;


		

		
		FontMetrics m = g.getFontMetrics();

		exibeJanela(g);
		// fora = true;

		exibeDados(g);

		for (int i = 0; i < 6; i++) {
			cbDados[i].setVisible(manual && (i % 3) < dados[i / 3].length);
		}


		bManual.setClivael(true);
		bAuto.setClivael(true);
		bAtaque.setClivael(true);

		bManual.setPos(g, getWidth() / 3, y_superior + fAlt*3);
		bAuto.setPos(g, 2 * getWidth() / 3, y_superior + fAlt*3);
		bAtaque.setPos(g, getWidth() / 2, y_inferior - fAlt);

		Point mousePosition = getMousePosition();
		int x = (int) mousePosition.getX();
		int y = (int) mousePosition.getY();

		if(bManual.atualiza(g,x,y) || bAuto.atualiza(g,x,y) || bAtaque.atualiza(g,x,y)){
			fora = false;
		}

		bManual.draw(g);
		bAuto.draw(g);
		bAtaque.draw(g);


		drawStr(g2d, String.format("%s (%d) atacando %s (%d)", atacante, model.getQtdExercitos(atacante), defensor,
				model.getQtdExercitos(defensor)), x_centro, y_inferior - m.getHeight() * 3);
		drawStr(g2d, String.format("%d X %d", dados[0].length, dados[1].length), x_centro,
				y_inferior - m.getHeight() * 2);

	}

	public void exibeTelaResultadoAtaque(Graphics g) {
		if (!exibeResultadoAtaque)
			return;
		int[][] dados = view.getListaDados();
		if (dados == null)
			return;

				
		Font fonte = new Font("Arial", Font.PLAIN, 24);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.setFont(fonte);
		FontRenderContext frc = g2d.getFontRenderContext();
		LineMetrics lm = fonte.getLineMetrics("", frc);


		int y_superior = getHeight() * 15 / 100; // Altura da borda superior da tela
		int y_inferior = getHeight() - y_superior; // Altura da borda inferior da tela
		int fAlt = (int)lm.getHeight(); // altura da fonte

		// Graphics2D g2d = (Graphics2D) g;
		// g2d.setColor(Color.WHITE);
		// g2d.setFont(new Font("Arial", Font.PLAIN, 24));

		// int y = getHeight() * 15 / 100;
		int x_centro = getWidth() / 2;

		// int y_inferior = getHeight() - y;
		FontMetrics m = g.getFontMetrics();

		exibeJanela(g);
		// fora = true;

		exibeDados(g);

		if (model.getQtdExercitos(atacante) > 1){
			Point mousePosition = getMousePosition();

			int x = (int) mousePosition.getX();
			int y = (int) mousePosition.getY();

			bAtaqueN.setPos(g, getWidth()/2, y_inferior - fAlt);
			bAtaqueN.setClivael(true);
			// bAtaqueN.atualiza(x,y);
			if(bAtaqueN.atualiza(g,x,y)){
				fora = false;
			}
			bAtaqueN.draw(g);
		}
			// bAtaqueN.setVisible(true);

		// bAtaqueN.setBounds((getWidth() - bAtaqueN.getPreferredSize().width) / 2, y_inferior - m.getHeight(),
		// 		bAtaqueN.getPreferredSize().width, bAtaqueN.getPreferredSize().height);

		drawStr(g2d, String.format("%s (%d) atacando %s (%d)", atacante, model.getQtdExercitos(atacante), defensor,
				model.getQtdExercitos(defensor)), x_centro, y_inferior - m.getHeight() * 3);
		drawStr(g2d, String.format("%d X %d", dados[0].length, dados[1].length), x_centro,
				y_inferior - m.getHeight() * 2);

	}

	public void exibeTelaConquista(Graphics g) {
		if (!exibeConquista)
			return;
		int[][] dados = view.getListaDados();
		if (dados == null)
			return;

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.setFont(new Font("Arial", Font.PLAIN, 24));

		int y = getHeight() * 15 / 100;
		int x_centro = getWidth() / 2;

		int y_inferior = getHeight() - y;
		FontMetrics m = g.getFontMetrics();

		exibeJanela(g);
		// fora = true;

		exibeDados(g);

		drawStr(g2d, String.format("Você conquistou %s", defensor), x_centro, y_inferior - m.getHeight() * 4);
		drawStr(g2d, "Escolha quantos exércitos deseja mover", x_centro, y_inferior - m.getHeight() * 3);
		// drawStr(g2d, String.format("%d X %d", dados[0].length,
		// dados[1].length),x_centro,y_inferior - m.getHeight()*2);
		temp1.draw(g);
		temp2.draw(g);
	}

	public void exibeCartas(Graphics g) {
		if (!exibeCartas)
			return;
		// janelaExibida = true;
		Graphics2D g2d = (Graphics2D) g;
		Image imagemCarta;
		int larg = getWidth() * 80 / 100;
		int alt = getHeight() * 70 / 100;
		int x = getWidth() * 10 / 100;
		int y = getHeight() * 15 / 100;

		int pos_x_ini = x + 30;
		int pos_y_ini = y + alt / 3;
		int marginLeft = larg / 5;

		exibeJanela(g);
		// fora = true;

		String[] nomesCartas = model.getCartasJogador();
		// conectar o nome das cartas com as imagens
		for (int i = 0; i < nomesCartas.length; i++) {
			imagemCarta = images.getImage(Territorio.getImgTerritorio(nomesCartas[i]));
			g2d.drawImage(imagemCarta, pos_x_ini + i * marginLeft, pos_y_ini, null);
		}
	}

	public void exibeTabelas(Graphics g) {
		if (!exibeTabelas)
			return;
		// janelaExibida = true;
		Graphics2D g2d = (Graphics2D) g;
		Image imagemTabelaExe = images.getImage("war_tabela_troca.png");
		Image imagemTabelaBonusCont = images.getImage("war_tabela_bonus_continente.png");
		int larg = getWidth() * 80 / 100;
		int x = getWidth() * 35 / 100;
		int y = getHeight() * 40 / 100;

		int pos_x_ini = x;
		int pos_y_ini = y;
		int marginLeft = larg / 5;
		exibeJanela(g);
		// fora = true;

		g2d.drawImage(imagemTabelaExe, pos_x_ini, pos_y_ini, null);
		g2d.drawImage(imagemTabelaBonusCont, pos_x_ini + marginLeft, pos_y_ini, null);
	}

	public void exibeObjetivo(Graphics g) {
		if (!exibeObjetivo)
			return;
		// janelaExibida = true;
		Graphics2D g2d = (Graphics2D) g;
		Image imagemObjetivo = images.getImage(model.getImgNameObjetivo());

		int x = getWidth() * 40 / 100 + 35;
		int y = getHeight() * 35 / 100;
		int pos_x_ini = x;
		int pos_y_ini = y;

		exibeJanela(g);
		// fora = true;
		g2d.drawImage(imagemObjetivo, pos_x_ini, pos_y_ini, null);
	}

	public void setExibeCartas(boolean b) {
		exibeCartas = b;
		repaint();
	}

	public void setExibeTabelas(boolean b) {
		exibeTabelas = b;
		repaint();
	}

	public void setExibeObjetivo(boolean b) {
		exibeObjetivo = b;
		repaint();
	}

	public boolean exibeVencedor(String corVencedor) {
		JOptionPane.showMessageDialog(null, "O jogador " + corVencedor + " venceu!", "Fim de jogo!",
				JOptionPane.INFORMATION_MESSAGE);
		int resposta = JOptionPane.showConfirmDialog(null, "Jogar novamente?", "Continuar?", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE);
		if (resposta == JOptionPane.YES_OPTION)
			return true;
		else
			return false;
	}

}
