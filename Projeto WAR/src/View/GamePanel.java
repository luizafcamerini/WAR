package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.font.*;

import Controller.ControllerAPI;
import Model.ModelAPI;
import Observer.ObservadoIF;
import Observer.ObservadorIF;

class GamePanel extends JPanel implements MouseListener, ObservadorIF {
	private final boolean DEBUG = false;
	private final Color[] cores = { Color.YELLOW, Color.BLUE, Color.WHITE, Color.BLACK, Color.GREEN, Color.RED };
	private final String[] coresStr = { "AMARELO", "AZUL", "BRANCO", "PRETO", "VERDE", "VERMELHO" };
	private final int I2_TERRITORIO = -1; // get(2) do id dos territorios do mapa
	private final int I2_TEMP1 = 101; // get(2) do id do territorio temporario 1
	private final int I2_TEMP2 = 102; // get(2) do id do territorio temporario 2
	private final int I2_INFOP = -2; // get(2) do id do infopainel
	private final int I2_B_MANUAL = 1; // get(2) do id de botao Ataque Manual
	private final int I2_B_AUTO = 2; // get(2) do id de botao Ataque Automatico
	private final int I2_B_ATAQUE = 3; // get(2) do id de botao Ataque
	private final int I2_B_ATAQUE_N = 4; // get(2) do id de botao Ataque Novamente
	private final int I2_B_SALVAR = 5; // get(2) do id de botao Salvar
	private final int I2_B_INICIAR = 6; // get(2) do id de botao Iniciar
	private final int I2_B_CARREGAR = 7; // get(2) do id de botao Carregar
	private final int I2_B_CARREGAR_AUTO = 8; // get(2) do id de botao Carregar Último Jogo
	private final int I2_B_CONFIRMA_NOVO_JOGO = 9; // get(2) do id de botao Carregar Último Jogo
	private final int I2_B_CARTAS = 10; // get(2) do id de botao das Cartas para a troca de cartas
	private final int I2_B_CONFIRMA_TROCA = 11; // get(2) do id do botao de confirmacao da escolha para a troca de
												// cartas
	private final Font fonte = new Font("Arial", Font.PLAIN, 24);

	private Territorio[] territorios;
	private InfoPainel iP;
	private Images images = Images.getInstance();
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
	private boolean exibeNovoJogo = false;
	private boolean inicio = true;
	private boolean obrigaExibeCartas = false;

	private boolean exibeAtaque = false;
	private boolean exibeResultadoAtaque = false;
	private boolean exibeConquista = false;
	private String atacante;
	private String defensor;

	private long ultimoClique = 0;
	private Territorio temp1, temp2;

	private JComboBox<Integer> cbDados[];
	private JTextField tfNomes[];

	private Botao bManual;
	private Botao bAuto;
	private Botao bAtaque;
	private Botao bAtaqueN;
	private Botao bSalvar;
	private Botao bIniciar;
	private Botao bCarregar;
	private Botao bCarregarAuto;
	private Botao bConfirmaNovoJogo;
	private Botao bConfirmaTroca;

	private Botao[] bCartas;
	private boolean[] cartasSelecionadas;

	private SoundEffect somAtaque = new SoundEffect("src/View/sounds/attack.wav");

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
		tfNomes = new JTextField[6];

		for (int i = 0; i < 6; i++) {
			tfNomes[i] = new JTextField();
			tfNomes[i].setVisible(false);
			add(tfNomes[i]);

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

		bCartas = new Botao[5];
		cartasSelecionadas = new boolean[5];

		for (int i = 0; i < bCartas.length; i++) {
			bCartas[i] = new Botao("");
			configBotao(bCartas[i], I2_B_CARTAS);
			bCartas[i].setI3(i);
			bCartas[i].setColor(0, new Color(128, 128, 128, 0));
			bCartas[i].setColor(1, new Color(128, 128, 128, 64));
			cartasSelecionadas[i] = false;
		}

		bManual = new Botao("Modo Manual");
		bAuto = new Botao("Modo automático");
		bAtaque = new Botao("ATACAR");
		bAtaqueN = new Botao("ATACAR NOVAMENTE");
		bSalvar = new Botao("SALVAR O JOGO");
		bIniciar = new Botao("INICIAR JOGO");
		bCarregar = new Botao("CARREGAR JOGO");
		bCarregarAuto = new Botao("CONTINUAR ÚLTIMO JOGO");
		bConfirmaNovoJogo = new Botao("CONFIRMAR");
		bConfirmaTroca = new Botao("CONFIRMAR");

		configBotao(bManual, I2_B_MANUAL);
		configBotao(bAuto, I2_B_AUTO);
		configBotao(bAtaque, I2_B_ATAQUE);
		configBotao(bAtaqueN, I2_B_ATAQUE_N);
		configBotao(bSalvar, I2_B_SALVAR);
		configBotao(bIniciar, I2_B_INICIAR);
		configBotao(bCarregar, I2_B_CARREGAR);
		configBotao(bCarregarAuto, I2_B_CARREGAR_AUTO);
		configBotao(bConfirmaNovoJogo, I2_B_CONFIRMA_NOVO_JOGO);
		configBotao(bConfirmaTroca, I2_B_CONFIRMA_TROCA);

	}

	public void notify(ObservadoIF o) {
		int i1 = o.get(1); // i1 = 0: mouse clicou em algo
		int i2 = o.get(2); // pega o "id" do observado

		if (DEBUG)
			System.out.printf("i1 = %d, i2 = %d\n", i1, i2);

		if (janelaExibida && (i2 < 1))
			return;

		// Mouse clicou em algo
		if (i1 == 0) {
			if (DEBUG)
				System.out.println("Mouse clicou em algo");

			// Realiza "debounce" do clique
			long tempoAtual = System.currentTimeMillis();
			if (tempoAtual - ultimoClique < 100)
				return;
			ultimoClique = tempoAtual;

			// Clicou em um território
			if (i2 == I2_TERRITORIO || i2 == I2_TEMP1 || i2 == I2_TEMP2) {
				// I2_TEMP1 e I2_TEMP2 são aqueles territórios temporarios da conquista do
				// territorio dentro do painel no ataque
				Territorio t = (Territorio) o;
				view.click(t.getNome());
			}

			// Clique em botão do infoPanel
			if (i2 == I2_INFOP) {
				int i3 = o.get(3); // o botao clicado
				if (DEBUG)
					System.out.printf("i3 = %d\n", i3);

				if (i3 == 0) {
					if (DEBUG)
						System.out.println("exibe objetivo");
					exibeObjetivo = true;
					fora = true;
				} else if (i3 == 1) {
					if (DEBUG)
						System.out.println("exibe cartas");
					exibeCartas = true;
					fora = true;
				} else if (i3 == 2) {
					if (DEBUG)
						System.out.println("exibe tabelas");
					exibeTabelas = true;
					fora = true;
				} else if (i3 == 3) {
					if (DEBUG)
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
				} else {
					control.ataque(atacante, defensor, view.getListaDados());
				}
				somAtaque.play();
			}

			// Ação do botao "bAtaqueN"
			else if (i2 == I2_B_ATAQUE_N) {
				control.ataca(atacante, defensor);
				fora = true;
			}

			// Ação do botao "bSalvar"
			else if (i2 == I2_B_SALVAR) {
				if (DEBUG)
					System.out.println("*******etapa: " + Integer.toString(control.getEtapa()));

				String path = view.salvaFile();
				if (path != null) {
					control.saveState(path);
					view.click(null); // gasta um click automatico
				}
				fora = true;
			}

			// Ação do botão "bIniciar"
			else if (i2 == I2_B_INICIAR) {
				exibeMenuInicial = false;
				exibeNovoJogo = true;
				bConfirmaNovoJogo.setClivael(true);
				if (janelaExibida) {
					limpaJanela();
				}
				fora = true;
			}

			// Ação do botao "bCarregar"
			else if (i2 == I2_B_CARREGAR) {
				String path = view.selecionaFile();
				int load = control.loadGame(path);
				if (load == 0) {
					exibeMenuInicial = false;
					view.click(null); // gasta um click automatico
					if (janelaExibida) {
						limpaJanela();
						bSalvar.setClivael(true);
						inicio = false;
					}
				}
				fora = true;
			}

			// Ação do botao "bCarregarAuto"
			else if (i2 == I2_B_CARREGAR_AUTO) {
				// carrega o ultimo jogo que foi fechado e que foi salvo automaticamente
				int load = control.loadGameAuto();
				if (load == 0) {
					exibeMenuInicial = false;
					view.click(null); // gasta um click automatico
					if (janelaExibida) {
						limpaJanela();
						bSalvar.setClivael(true);
						inicio = false;
					}
				}
				fora = true;
			}

			// Ação do botao "bConfirmaNovoJogo"
			else if (i2 == I2_B_CONFIRMA_NOVO_JOGO) { // confirma novos jogadores
				String[] nomes = new String[6];
				int count = 0;
				for (int i = 0; i < tfNomes.length; i++) {
					JTextField tf = tfNomes[i];
					String nome = tf.getText();
					if (nome.length() != 0) {
						count++;
						nomes[i] = nome;
					}
					if (count >= 3) {
						control.novoJogo(nomes);
						bConfirmaNovoJogo.setClivael(false);
						exibeNovoJogo = false;
						limpaJanela();
						bSalvar.setClivael(true);
						inicio = false;
						view.click(null);
					}
					if (DEBUG)
						System.out.printf("%d %s\n", nome.length(), nome);
				}
				fora = true;
			}

			// Ação dos botões "bCartas"
			else if (i2 == I2_B_CARTAS) {
				int i3 = o.get(3);
				if (DEBUG)
					System.out.printf("i3=%d\n", i3);

				int count = 0;
				for (boolean b : cartasSelecionadas) {
					if (b)
						count++;
				}

				if (cartasSelecionadas[i3]) {
					// Muda as cores com relacao a entrada e saida do mouse:
					bCartas[i3].setColor(0, new Color(0, 0, 0, 0));
					bCartas[i3].setColor(1, new Color(128, 128, 128, 64));
					// Muda para nao selecionado:
					cartasSelecionadas[i3] = false;
					count--;
				} else if (count < 3) {
					// Muda as cores com relacao a entrada e saida do mouse:
					bCartas[i3].setColor(0, new Color(255, 0, 0, 64));
					bCartas[i3].setColor(1, new Color(255, 0, 0, 64));
					// Muda para selecionado:
					cartasSelecionadas[i3] = true;
					count++;
				}

				if (count == 3 && model.verificaTrocaCartas(cartasSelecionadas)) {
					bConfirmaTroca.setClivael(true);
				} else {
					bConfirmaTroca.setClivael(false);
				}
			}

			// Ação do botão "bConfirmaTroca"
			else if (i2 == I2_B_CONFIRMA_TROCA) {
				control.confirmaTroca(cartasSelecionadas);

				for (int i = 0; i < bCartas.length; i++) {
					cartasSelecionadas[i] = false;
					bCartas[i].setClivael(DEBUG);
				}
				limpaJanela();
				obrigaExibeCartas = false;
				fora = true;
			}
		}

		// Mouse "entrou" em algo
		else if (i1 == 1) {
			fora = false;
		}

		// Mouse "saiu" de algo
		else if (i1 == 2) {
			fora = true;
		}
		if (DEBUG)
			System.out.printf("Fora = %s\n", fora ? "true" : "false");

		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Point mousePosition = getMousePosition();
		try {
			xM = (int) mousePosition.getX();
			yM = (int) mousePosition.getY();
		} catch (Exception e) {
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
		if (bSalvar.atualiza(g, xM, yM)) {
			fora = false;
		}
		bSalvar.draw(g);

		exibeTelaAtaque(g);
		exibeTelaResultadoAtaque(g);
		exibeTelaConquista(g);
		exibeCartas(g);
		exibeTabelas(g);
		exibeObjetivo(g);
		exibeTelaMenuInicial(g);
		exibeTelaNovoJogo(g);

	}

	public void mouseClicked(MouseEvent e) {
		if (fora) {
			// "debounce" do clique
			long tempoAtual = System.currentTimeMillis();
			if (tempoAtual - ultimoClique < 100)
				return;
			ultimoClique = tempoAtual;

			if (inicio) {
				return;
			}

			// Clique fora ocorrido
			view.click(null);
			if (janelaExibida) {
				limpaJanela();
				bSalvar.setClivael(true);

			}

			repaint();
		}

		if (DEBUG)
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

		if (DEBUG)
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

	

	public void exibeNovoJogoNovamente(String[] nomes) {
		for (int i = 0; i < tfNomes.length; i++) {
			tfNomes[i].setText(nomes[i] != null ? nomes[i] : "");
		}
		exibeMenuInicial = false;
		exibeNovoJogo = true;
		bConfirmaNovoJogo.setClivael(true);
		if (janelaExibida) {
			limpaJanela();
		}
		fora = true;
		exibeNovoJogo = true;
	}

	public void obrigaTroca() {
		obrigaExibeCartas = true;
	}

	/*------------------------------------------------------------------------------------------------------------------- */
	/* Métodos auxiliares para o desenho da tela */

	private void exibeJanela(Graphics g) {
		janelaExibida = true;
		bSalvar.setClivael(false);
		Graphics2D g2d = (Graphics2D) g;
		FontRenderContext frc = g2d.getFontRenderContext();

		int larg = getWidth() * 80 / 100;
		int alt = getHeight() * 70 / 100;
		int x = getWidth() * 10 / 100;
		int y = getHeight() * 15 / 100;
		int fAlt = (int) fonte.getStringBounds("", frc).getHeight();

		Rectangle2D rt = new Rectangle2D.Double(x, y, larg, alt);
		g2d.setColor(Color.CYAN);
		g2d.setStroke(new BasicStroke(30));
		g2d.draw(rt);

		Image fundo = images.getImage("war_tabuleiro_fundo.png");
		g.drawImage(fundo, x, y, larg, alt, null);

		g2d.setFont(fonte);
		g2d.setColor(Color.WHITE);
		drawStr(g, "Clique em quase qualquer lugar para fechar.", getWidth() / 2, y + fAlt * 2);

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
		bIniciar.setClivael(false);
		bCarregar.setClivael(false);
		bCarregarAuto.setClivael(false);

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
		for (JTextField tf : tfNomes) {
			tf.setVisible(false);
		}
	}

	private void drawStr(Graphics g, String text, int xc, int yc) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(fonte);
		FontRenderContext frc = g2d.getFontRenderContext();

		int x = xc - (int) fonte.getStringBounds(text, frc).getWidth() / 2;
		int y = yc - (int) fonte.getStringBounds(text, frc).getHeight() / 2;

		g.drawString(text, x, y);
	}

	private void exibeDados(Graphics g) {
		int[][] dados = view.getListaDados();
		if (dados == null)
			return;

		Graphics2D g2d = (Graphics2D) g;

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
		int lado = 50;

		for (int i = 0; i < dados[0].length; i++) { // dados de ataque
			imagemDadoStr = "dado_ataque_" + dados[0][i] + ".png";
			imagemDado = images.getImage(imagemDadoStr);
			g2d.drawImage(imagemDado, pos_x_ini + marginLeft * i - lado / 2, pos_y_ini - lado / 2, lado, lado, null);
			cbDados[i].setBounds(pos_x_ini + marginLeft * i + lado / 2, pos_y_ini - lado / 4,
					cbDados[i].getPreferredSize().width, lado / 2);
		}

		for (int i = 0; i < dados[1].length; i++) { // dados de defesa
			imagemDadoStr = "dado_defesa_" + dados[1][i] + ".png";
			imagemDado = images.getImage(imagemDadoStr);
			g2d.drawImage(imagemDado, pos_x_ini + marginLeft * i - lado / 2, pos_y_ini + marginTop - lado / 2, lado,
					lado, null);
			cbDados[i + 3].setBounds(pos_x_ini + marginLeft * i + lado / 2, pos_y_ini + marginTop - lado / 4,
					cbDados[i + 3].getPreferredSize().width, lado / 2);
		}

	}

	private void exibeTelaAtaque(Graphics g) {
		if (!exibeAtaque)
			return;
		int[][] dados = view.getListaDados();
		if (dados == null)
			return;

		Graphics2D g2d = (Graphics2D) g;
		FontRenderContext frc = g2d.getFontRenderContext();

		int y_superior = getHeight() * 15 / 100; // Altura da borda superior da tela
		int y_inferior = getHeight() - y_superior; // Altura da borda inferior da tela
		int x_centro = getWidth() / 2;
		int fAlt = (int) fonte.getStringBounds("", frc).getHeight(); // altura da fonte

		exibeJanela(g);

		exibeDados(g);
		for (int i = 0; i < 6; i++) {
			if (manual && (i % 3) < dados[i / 3].length) {
				cbDados[i].setVisible(true);
				cbDados[i].repaint();
			} else {
				cbDados[i].setVisible(false);
			}
		}

		bManual.setClivael(true);
		bAuto.setClivael(true);
		bAtaque.setClivael(true);

		bManual.setPos(g, getWidth() / 3, y_superior + fAlt * 3);
		bAuto.setPos(g, 2 * getWidth() / 3, y_superior + fAlt * 3);
		bAtaque.setPos(g, getWidth() / 2, y_inferior - fAlt);

		if (bManual.atualiza(g, xM, yM) || bAuto.atualiza(g, xM, yM) || bAtaque.atualiza(g, xM, yM)) {
			fora = false;
		}

		bManual.draw(g);
		bAuto.draw(g);
		bAtaque.draw(g);

		g2d.setColor(Color.WHITE);

		drawStr(g2d, String.format("%s (%d) atacando %s (%d)", atacante, model.getQtdExercitos(atacante), defensor,
				model.getQtdExercitos(defensor)), x_centro, y_inferior - fAlt * 3);
		drawStr(g2d, String.format("%d X %d", dados[0].length, dados[1].length), x_centro,
				y_inferior - fAlt * 2);

	}

	private void exibeTelaResultadoAtaque(Graphics g) {
		if (!exibeResultadoAtaque)
			return;
		int[][] dados = view.getListaDados();
		if (dados == null)
			return;

		Graphics2D g2d = (Graphics2D) g;
		FontRenderContext frc = g2d.getFontRenderContext();

		int y_superior = getHeight() * 15 / 100; // Altura da borda superior da tela
		int y_inferior = getHeight() - y_superior; // Altura da borda inferior da tela
		int x_centro = getWidth() / 2;
		int fAlt = (int) fonte.getStringBounds("", frc).getHeight(); // altura da fonte

		exibeJanela(g);

		exibeDados(g);

		if (model.getQtdExercitos(atacante) > 1) {
			bAtaqueN.setPos(g, x_centro, y_inferior - fAlt);
			bAtaqueN.setClivael(true);
			if (bAtaqueN.atualiza(g, xM, yM)) {
				fora = false;
			}
			bAtaqueN.draw(g);
		}

		g2d.setColor(Color.WHITE);
		drawStr(g2d, String.format("%s (%d) atacando %s (%d)", atacante, model.getQtdExercitos(atacante), defensor,
				model.getQtdExercitos(defensor)), x_centro, y_inferior - fAlt * 3);
		drawStr(g2d, String.format("%d X %d", dados[0].length, dados[1].length), x_centro,
				y_inferior - fAlt * 2);
	}

	private void exibeTelaConquista(Graphics g) {
		if (!exibeConquista)
			return;
		int[][] dados = view.getListaDados();
		if (dados == null)
			return;

		Graphics2D g2d = (Graphics2D) g;
		FontRenderContext frc = g2d.getFontRenderContext();

		int y_superior = getHeight() * 15 / 100; // Altura da borda superior da tela
		int y_inferior = getHeight() - y_superior; // Altura da borda inferior da tela
		int x_centro = getWidth() / 2;
		int fAlt = (int) fonte.getStringBounds("", frc).getHeight(); // altura da fonte

		exibeJanela(g);
		exibeDados(g);

		g2d.setColor(Color.WHITE);
		drawStr(g2d, String.format("Você conquistou %s", defensor), x_centro, y_inferior - fAlt * 4);
		drawStr(g2d, "Escolha quantos exércitos deseja mover", x_centro, y_inferior - fAlt * 3);

		temp1.draw(g);
		temp2.draw(g);
	}

	private void exibeCartas(Graphics g) {
		if (!(exibeCartas || obrigaExibeCartas))
			return;
		Graphics2D g2d = (Graphics2D) g;
		FontRenderContext frc = g2d.getFontRenderContext();
		Image imagemCarta = null;

		int larg = getWidth() * 80 / 100;
		int y_centro = getHeight() / 2;
		int x_centro = getWidth() / 2;
		int marginLeft = larg / 5;

		int y_superior = getHeight() * 15 / 100; // Altura da borda superior da tela
		int y_inferior = getHeight() - y_superior; // Altura da borda inferior da tela
		int fAlt = (int) fonte.getStringBounds("", frc).getHeight(); // altura da fonte

		exibeJanela(g);

		String[] nomesCartas = model.getCartasJogador();
		// conectar o nome das cartas com as imagens
		for (int i = 0; i < nomesCartas.length; i++) {
			imagemCarta = images.getImage(Territorio.getImgTerritorio(nomesCartas[i]));
			System.out.println("CARTA " + nomesCartas[i]);
			System.out.println("CARTA " + Territorio.getImgTerritorio(nomesCartas[i]));
			g2d.drawImage(imagemCarta, x_centro + (i - 2) * marginLeft - imagemCarta.getWidth(null) / 2,
					y_centro - imagemCarta.getHeight(null) / 2, null);

			if (control.podeTrocar()) {
				bCartas[i].setBounds(x_centro + (i - 2) * marginLeft - imagemCarta.getWidth(null) / 2,
						y_centro - imagemCarta.getHeight(null) / 2, imagemCarta.getWidth(null),
						imagemCarta.getHeight(null));
				bCartas[i].setClivael(true);
				if (bCartas[i].atualiza(g, xM, yM)) {
					fora = false;
				}
				bCartas[i].draw(g);
			}
		}

		if (control.podeTrocar()) {
			bConfirmaTroca.setPos(g2d, x_centro, y_inferior - fAlt);
			// bConfirmaTroca.setClivael(false);
			if (bConfirmaTroca.atualiza(g, xM, yM)) {
				fora = false;
			}
			bConfirmaTroca.draw(g2d);
		}

	}

	private void exibeTabelas(Graphics g) {
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

	private void exibeObjetivo(Graphics g) {
		if (!exibeObjetivo || model.getCorAtual() == -1)
			return;
		Graphics2D g2d = (Graphics2D) g;
		int objetivoNum = Integer.parseInt(model.getImgNameObjetivo().replaceAll("\\D+", ""));
		Image imagemObjetivo = images.getImage(model.getImgNameObjetivo());

		int x_centro = getWidth() / 2;
		int y_centro = getHeight() / 2;

		exibeJanela(g);
		g2d.drawImage(imagemObjetivo, x_centro - imagemObjetivo.getWidth(null) / 2,
				y_centro - imagemObjetivo.getHeight(null) / 2, null);

		if (objetivoNum >= 1 && objetivoNum <= 7) { // Se o objetivo for de eliminar um jogador, exibe o restante de sua
													// descrição
			String[] descObjetivoCompleta = model.getDescricaoObjetivo().split("\\.", 2);
			String[] descObjetivo = descObjetivoCompleta[1].split(",");
			if (DEBUG)
				System.out.println("***Objetivo: " + objetivoNum);
			int pos_x = x_centro - imagemObjetivo.getWidth(null) * 3;
			int pos_y = y_centro + imagemObjetivo.getHeight(null) / 2 + 40;
			/*
			 * Reduz o tamanho da fonte atual para desenhar a string depois volta para a
			 * fonte q tava antes
			 */
			Font fonte_atual = g2d.getFont();
			Font font_nova = fonte_atual.deriveFont(15f);
			g2d.setFont(font_nova);
			g2d.drawString(descObjetivo[0], pos_x, pos_y);
			pos_y += 20;
			g2d.drawString(descObjetivo[1], pos_x, pos_y);
			g2d.setFont(fonte_atual);
		}
	}

	private void exibeTelaMenuInicial(Graphics g) {
		if (!exibeMenuInicial)
			return;

		Graphics2D g2d = (Graphics2D) g;
		FontRenderContext frc = g2d.getFontRenderContext();

		int y_centro = getHeight() / 2;
		int x_centro = getWidth() / 2;
		int fAlt = (int) fonte.getStringBounds("", frc).getHeight(); // altura da fonte

		exibeJanela(g);

		bIniciar.setClivael(true); // inicia um jogo totalmente novo
		bCarregar.setClivael(true); // carrega o jogo de um txt escolhido
		bCarregarAuto.setClivael(true); // carrega o ultimo jogo carregado

		bIniciar.setPos(g, x_centro, y_centro - fAlt * 2);
		bCarregar.setPos(g, x_centro, y_centro);
		bCarregarAuto.setPos(g, x_centro, y_centro + fAlt * 2);

		if (bIniciar.atualiza(g, xM, yM) || bCarregar.atualiza(g, xM, yM) || bCarregarAuto.atualiza(g, xM, yM)) {
			fora = false;
		}

		bIniciar.draw(g);
		bCarregar.draw(g);
		bCarregarAuto.draw(g);

	}

	private void exibeTelaNovoJogo(Graphics g) {
		/** Funcao que exibe a tela de começo de um novo jogo. */
		if (!exibeNovoJogo)
			return;

		Graphics2D g2d = (Graphics2D) g;
		FontRenderContext frc = g2d.getFontRenderContext();

		int y_superior = getHeight() * 15 / 100; // Altura da borda superior da tela
		int y_inferior = getHeight() - y_superior; // Altura da borda inferior da tela
		int y_centro = getHeight() / 2;
		int x_centro = getWidth() / 2;
		int fAlt = (int) fonte.getStringBounds("", frc).getHeight(); // altura da fonte
		int larg = getWidth() / 6;

		exibeJanela(g);
		for (int i = 0; i < tfNomes.length; i++) { // textfields dos jogadores
			int x = x_centro + larg * ((i % 3) - 1) * 3 / 2 - larg / 2;
			int y = y_centro - fAlt * ((i / 3) * 6 - 3);
			Rectangle2D rt = new Rectangle2D.Double(x - fAlt, y - fAlt, larg + 2 * fAlt, fAlt * 3);
			g2d.setColor(cores[i]);
			g2d.fill(rt);

			tfNomes[i].setBounds(x, y, larg, fAlt);
			tfNomes[i].setVisible(true);
			tfNomes[i].repaint();
		}

		bConfirmaNovoJogo.setPos(g, x_centro, y_inferior - fAlt * 2);

		if (bConfirmaNovoJogo.atualiza(g, xM, yM)) {
			fora = false;
		}

		bConfirmaNovoJogo.draw(g);
	}

}
