package View;

import javax.swing.*;

import Controller.ControllerAPI;
import Model.ModelAPI;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.awt.font.*;

class GamePanel extends JPanel implements MouseListener, ObservadorIF {
	private final Color[] cores = { Color.YELLOW, Color.BLUE, Color.WHITE, Color.BLACK, Color.GREEN, Color.RED };
	// private final String[] coresStr = { "AMARELO", "AZUL", "BRANCO", "PRETO", "VERDE", "VERMELHO" };
	private final int I2_TERRITORIO = -1; // get(2) do id dos territorios do mapa
	private final int I2_TEMP1 = 11; // get(2) do id do territorio temporario 1
	private final int I2_TEMP2 = 12; // get(2) do id do territorio temporario 2
	private final int I2_INFOP = -2; // get(2) do id do infopainel
	private final int I2_B_MANUAL = 1; // get(2) do id de botao Ataque Manual
	private final int I2_B_AUTO = 2; // get(2) do id de botao Ataque Automatico
	private final int I2_B_ATAQUE = 3; // get(2) do id de botao Ataque 
	private final int I2_B_ATAQUE_N = 4; // get(2) do id de botao Ataque Novamente
	private final int I2_B_SALVAR = 5; // get(2) do id de botao Salvar

	private final int I2_B_INICIAR = 6; // get(2) do id de botao Iniciar
	private final int I2_B_CARREGAR = 7; // get(2) do id de botao Carregar
	private final int I2_B_CARREGAR_AUTO = 8; // get(2) do id de botao Carregar Último Jogo
	private final String pathAuto = "src/gameState.txt";

	private Territorio[] territorios;
	private InfoPainel iP;
	private Images images = Images.getInstance();
	private GamePanel instance;
	private ViewAPI view = ViewAPI.getInstance();
	private ModelAPI model = ModelAPI.getInstance();
	private ControllerAPI control = ControllerAPI.getInstance();
	private int xM, yM; // Coordenadas do Mouse
	private boolean fora = true;
	private boolean exibeCartas = false;
	private boolean exibeTabelas = false;
	private boolean exibeObjetivo = false;
	private boolean janelaExibida = false;
	private boolean manual = false;
	private boolean exibeMenuInicial = true;

	private boolean exibeAtaque = false;
	private boolean exibeResultadoAtaque = false;
	private boolean exibeConquista = false;
	private String atacante;
	private String defensor;

	private long ultimoClique = 0;
	private Territorio temp1, temp2;

	private JComboBox<Integer> cbDados[];
	private Botao bManual;
	private Botao bAuto;
	private Botao bAtaque;
	private Botao bAtaqueN;
	private Botao bSalvar;
	private Botao bIniciar;
	private Botao bCarregar;
	private Botao bCarregarAuto;

	private void configBotao(Botao b, int i2) {
		b.setI2(i2);
		b.addObservador(this);
		addMouseListener(b);
		addMouseMotionListener(b);
		b.setClivael(false);
	}

	public GamePanel(InfoPainel iP) {
		this.addMouseListener(this);
		this.iP = iP;

		territorios = Territorio.getTerritorios();

		Integer[] valores_comboBox = { 1, 2, 3, 4, 5, 6 };
		cbDados = new JComboBox[6];
		for (int i = 0; i < 6; i++) {
			final int index = i;
			cbDados[i] = new JComboBox<Integer>(valores_comboBox);
			add(cbDados[i]);
			cbDados[i].setEditable(false);
			cbDados[i].setVisible(false);
			cbDados[i].setSelectedIndex(0);
			cbDados[i].addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						int valor = (int) cbDados[index].getSelectedItem();
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
		bSalvar = new Botao("SALVAR O JOGO");
		bIniciar = new Botao("INICIAR JOGO");
		bCarregar = new Botao("CARREGAR JOGO");
		bCarregarAuto = new Botao("CONTINUAR ÚLTIMO JOGO");

		configBotao(bManual, I2_B_MANUAL);
		configBotao(bAuto, I2_B_AUTO);
		configBotao(bAtaque, I2_B_ATAQUE);
		configBotao(bAtaqueN, I2_B_ATAQUE_N);
		configBotao(bSalvar, I2_B_SALVAR);
		configBotao(bIniciar, I2_B_INICIAR);
		configBotao(bCarregar, I2_B_CARREGAR);
		configBotao(bCarregarAuto, I2_B_CARREGAR_AUTO);

	}

	public GamePanel getInstance() {
		if (instance == null) {
			instance = new GamePanel(iP);
		}
		return instance;
	}

	public void notify(ObservadoIF o) {
		int i1 = o.get(1); // i1 = 0: mouse clicou em algo
		int i2 = o.get(2); // pega o id do botao
		
		System.out.printf("i1 = %d, i2 = %d\n", i1, i2);

		if (janelaExibida && (i2 < 1))
			return;

		// Mouse clicou em algo
		if (i1 == 0) {
			System.out.println("Mouse clicou em algo");
			// Realiza "debounce" do clique
			long tempoAtual = System.currentTimeMillis();
			// System.out.printf("DELTA = %d\n", tempoAtual - ultimoClique);
			if (tempoAtual - ultimoClique < 100)
				return;
			ultimoClique = tempoAtual;

			// Clicou em um território
			if (i2 == I2_TERRITORIO || i2 == I2_TEMP1 || i2 == I2_TEMP2) { // 5 e 6 são aqueles territórios temporarios da conquista do territorio dentro do painel no ataque
				Territorio t = (Territorio) o;
				view.click(t.getNome());
			}

			// Clique em botão do infoPanel
			if (i2 == I2_INFOP) {
				int i3 = o.get(3); // o botao clicado
				System.out.printf("i3 = %d\n", i3);

				if (i3 == 0) {
					System.out.println("exibe objetivo");
					exibeObjetivo = true;
					fora = true;
				}
				else if (i3 == 1) {
					System.out.println("exibe cartas");
					exibeCartas = true;
					fora = true;
				}
				else if (i3 == 2) {
					System.out.println("exibe tabelas");
					exibeTabelas = true;
					fora = true;
				}
				else if (i3 == 3) {
					System.out.println("proxima etapa");
					control.proxEtapa();
				}
			}

			// Ação do botao "bManual"
			else if (i2 == I2_B_MANUAL) {
				manual = true;
				for (int i = 0; i < 6; i++) {
					view.setDado(i, (int) cbDados[i].getSelectedItem());
				}
			}

			// Ação do botao "bAuto"
			else if (i2 == I2_B_AUTO) {
				manual = false;
				for (int i = 0; i < 6; i++) {
					view.setDado(i, 0);
				}
			}

			// Ação do botao "bAtaque"
			else if (i2 == I2_B_ATAQUE) {
				fora = true;
				if (!manual) {
					control.ataque(atacante, defensor);
					repaint();
				} else {
					control.ataque(atacante, defensor, view.getListaDados());
				}
			}

			// Ação do botao "bAtaqueN"
			else if (i2 == I2_B_ATAQUE_N) {
				control.ataca(atacante, defensor);
				fora = true;
			}

			// Ação do botao "bSalvar"
			else if (i2 == I2_B_SALVAR){
				System.out.println("*******etapa: " + Integer.toString(control.getEtapa()));
				if (control.getEtapa() == 40){
					System.out.println("GAMEPANEL: salva o jogo");
					control.botaoSalvaJogo();
				}
			}

			// Ação do botão "bIniciar"
			else if (i2 == I2_B_INICIAR) {
			}

			// Ação do botao "bCarregar"
			else if (i2 == I2_B_CARREGAR) {
				String path = view.selecionaFile();
				int load = model.loadGame(path);
				if (load == 0){
					exibeMenuInicial = false;
					view.click(null); //gasta um click automatico
					if (janelaExibida) {
						limpaJanela();
					}
				}
				fora = true;
			}

			// Ação do botao "bCarregarAuto"
			else if (i2 == I2_B_CARREGAR_AUTO) { //carrega o ultimo jogo que foi fechado e que foi salvo automaticamente
				int load = model.loadGame(pathAuto);
				if (load == 0){
					exibeMenuInicial = false;
					view.click(null); //gasta um click automatico
					if (janelaExibida) {
						limpaJanela();
					}
				}
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
//		System.out.printf("Fora = %s\n", fora ? "true" : "false");
		
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Point mousePosition = getMousePosition();
		try {
			xM = (int) mousePosition.getX();
			yM = (int) mousePosition.getY();
		}
		catch (Exception e) {
			xM = -1;
			yM = -1;
		}

		Image tabuleiro = images.getImage("war_tabuleiro2.png");
		g.drawImage(tabuleiro, 0, 0, getWidth(), getHeight(), null);

		for (Territorio t : territorios) {
			t.draw(g);
		}

		iP.draw(g);
		bSalvar.setPos(g, 110, 450);
		// if (bSalvar.atualiza(g, x, y)) {
		// 	fora = false;
		// }
		bSalvar.draw(g);

		exibeTelaAtaque(g);
		exibeTelaResultadoAtaque(g);
		exibeTelaConquista(g);
		exibeCartas(g);
		exibeTabelas(g);
		exibeObjetivo(g);
		exibeTelaMenuInicial(g);

	}

	public void mouseClicked(MouseEvent e) {
		if (fora) {
			// "debounce" do clique
			long tempoAtual = System.currentTimeMillis();
			// System.out.printf("DELTA = %d\n", tempoAtual - ultimoClique);
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
				if (listener instanceof Botao) {
					Botao b = (Botao) listener;
					if (b.estaEm(e.getX(), e.getY()))
						fora = false;
				} else if (listener instanceof Territorio) {
					Territorio t = (Territorio) listener;
					if (t.estaEm(e.getX(), e.getY()))
						fora = false;
				}
				if (!fora)
					break;
			}
			repaint();
		}

//		System.out.printf("Fora = %s\n", fora ? "true" : "false");

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

		for (JComboBox<Integer> cb : cbDados) {
			cb.setVisible(false);
		}
	}

	public void ataque(String atacante, String defensor) {
		limpaJanela();
		fora = true;

		if (manual) {
			for (int i = 0; i < 6; i++) {
				view.setDado(i, (int) cbDados[i].getSelectedItem());
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

		temp1.setI2(I2_TEMP1);
		temp2.setI2(I2_TEMP2);

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
		int fAlt = (int) lm.getHeight(); // altura da fonte

		int x_centro = getWidth() / 2;

		FontMetrics m = g.getFontMetrics();

		exibeJanela(g);
		exibeDados(g);

		for (int i = 0; i < 6; i++) {
			cbDados[i].setVisible(manual && (i % 3) < dados[i / 3].length);
		}

		bManual.setClivael(true);
		bAuto.setClivael(true);
		bAtaque.setClivael(true);

		bManual.setPos(g, getWidth() / 3, y_superior + fAlt * 3);
		bAuto.setPos(g, 2 * getWidth() / 3, y_superior + fAlt * 3);
		bAtaque.setPos(g, getWidth() / 2, y_inferior - fAlt);

//		Point mousePosition = getMousePosition();
//		int x = (int) mousePosition.getX();
//		int y = (int) mousePosition.getY();

		if (bManual.atualiza(g, xM, yM) || bAuto.atualiza(g, xM, yM) || bAtaque.atualiza(g, xM, yM)) {
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
		int fAlt = (int) lm.getHeight(); // altura da fonte

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

		if (model.getQtdExercitos(atacante) > 1) {
//			Point mousePosition = getMousePosition();
//
//			int x = (int) mousePosition.getX();
//			int y = (int) mousePosition.getY();

			bAtaqueN.setPos(g, getWidth() / 2, y_inferior - fAlt);
			bAtaqueN.setClivael(true);
			if (bAtaqueN.atualiza(g, xM, yM)) {
				fora = false;
			}
			bAtaqueN.draw(g);
		}

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

		exibeDados(g);

		drawStr(g2d, String.format("Você conquistou %s", defensor), x_centro, y_inferior - m.getHeight() * 4);
		drawStr(g2d, "Escolha quantos exércitos deseja mover", x_centro, y_inferior - m.getHeight() * 3);

		temp1.draw(g);
		temp2.draw(g);
	}

	public void exibeCartas(Graphics g) {
		if (!exibeCartas)
			return;
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

	public void setClicavelSalvar(boolean b) {
		bSalvar.setClivael(b);
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

	public void exibeTelaMenuInicial(Graphics g) {
		if (!exibeMenuInicial)
			return;
		
		Font fonte = new Font("Arial", Font.PLAIN, 24);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.WHITE);
		g2d.setFont(fonte);
		FontRenderContext frc = g2d.getFontRenderContext();
		LineMetrics lm = fonte.getLineMetrics("", frc);

		int y_superior = getHeight() * 15 / 100; // Altura da borda superior da tela
		int y_inferior = getHeight() - y_superior; // Altura da borda inferior da tela
		int y_centro = getHeight() /2;
		int x_centro = getWidth() / 2;
		int fAlt = (int) lm.getHeight(); // altura da fonte

		FontMetrics m = g.getFontMetrics();

		exibeJanela(g);

		bIniciar.setClivael(true); // inicia um jogo totalmente novo
		bCarregar.setClivael(true); // carrega o jogo de um txt escolhido
		bCarregarAuto.setClivael(true); // carrega o ultimo jogo carregado

		if (bIniciar.atualiza(g, xM, yM) || bCarregar.atualiza(g, xM, yM) || bCarregarAuto.atualiza(g, xM, yM)) {
			fora = false;
		}

		bIniciar.setPos(g,  x_centro, y_centro - fAlt * 2);
		bCarregar.setPos(g,  x_centro, y_centro);
		bCarregarAuto.setPos(g,  x_centro,  y_centro + fAlt * 2);

		bIniciar.draw(g);
		bCarregar.draw(g);
		bCarregarAuto.draw(g);

		// drawStr(g2d, String.format("%s (%d) atacando %s (%d)", atacante, model.getQtdExercitos(atacante), defensor,
		// 		model.getQtdExercitos(defensor)), x_centro, y_inferior - m.getHeight() * 3);
		// drawStr(g2d, String.format("%d X %d", dados[0].length, dados[1].length), x_centro,
		// 		y_inferior - m.getHeight() * 2);

	}

}
