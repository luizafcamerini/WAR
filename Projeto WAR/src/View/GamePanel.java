package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.font.*;

import Observer.ObservadoIF;
import Observer.ObservadorIF;

class GamePanel extends JPanel implements MouseListener, ObservadorIF {
	private final int ALTURA_TELA = 700;
	private final int LARGURA_TELA = 1200;
	private final boolean DEBUG = false;
	private final Color[] cores = { Color.YELLOW, Color.BLUE, Color.WHITE, Color.BLACK, Color.GREEN, Color.RED };
	private final int I2_TERRITORIO = -1; // get(2) do id dos territorios do mapa
	private final int I2_TEMP1 = 101; // get(2) do id do territorio temporario 1
	private final int I2_TEMP2 = 102; // get(2) do id do territorio temporario 2
	private final int I2_INFOP = 100; // get(2) do id do infopainel
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
	private final int I2_B_OLHO = 12; // get(2) do id de botao dde visualização de nomes de territórios
	private final int POS_OLHO_X = LARGURA_TELA - 65;
	private final int POS_OLHO_Y = ALTURA_TELA - 125;
	private final Font fonte = new Font("Arial", Font.PLAIN, 24);

	private Territorio[] territorios;
	private InfoPainel iP;
	private Images images = Images.getInstance();
	private ViewAPI view = ViewAPI.getInstance();
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
	private boolean olho_ativado = false;
	private String atacante;
	private String defensor;

	private long ultimoClique = 0;
	private Territorio temp1, temp2;

	private JComboBox<Integer> cbDados[]; // ComboBox para os dados.
	private JTextField tfNomes[]; // Lista de TextFields do nome dos novos jogadores.

	// Botoes do jogo:
	private Botao bManual; // Ataque manual
	private Botao bAuto; // Ataque automativo
	private Botao bAtaque; // Ataque
	private Botao bAtaqueN; // Atacar novamente
	private Botao bSalvar; // Salvar o jogo
	private Botao bIniciar; // Iniciar novo jogo
	private Botao bCarregar; // Carregar jogo
	private Botao bCarregarAuto; // Carregar ultimo jogo
	private Botao bConfirmaNovoJogo; // Confirmar novo jogo
	private Botao bConfirmaTroca; // Confirmar troca de cartas
	private Botao bAtivaOlho; // Ativa o olho para exibir os nomes de todos os territorios

	private Botao[] bCartas; // Lista de botoes da troca de cartas (em cima das proprias cartas)
	private boolean[] cartasSelecionadas; // Lista das cartas selecionadas (indice compativel com bCartas)

	Image tabuleiro = images.getImage("war_tabuleiro2.png");
	Image imgOlho = images.getImage("eye.png");
	Image imgOlhoRiscado = images.getImage("eye-hide.png");

	public GamePanel(InfoPainel iP) {
		/** Construtor que cria e configura todos os componentes do GamePanel. */
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
			bCartas[i].setIDComplementar(i);
			bCartas[i].setColor(0, new Color(0, 0, 0, 0));
			bCartas[i].setColor(1, new Color(128, 128, 128, 64));
			cartasSelecionadas[i] = false;
		}

		// Criacao de todos os botoes com seus textos:
		bManual = new Botao("Modo Manual");
		bAuto = new Botao("Modo automático");
		bAtaque = new Botao("ATACAR");
		bAtaqueN = new Botao("ATACAR NOVAMENTE");
		bSalvar = new Botao("SALVAR O JOGO");
		bIniciar = new Botao("INICIAR NOVO JOGO");
		bCarregar = new Botao("CARREGAR JOGO");
		bCarregarAuto = new Botao("CONTINUAR ÚLTIMO JOGO");
		bConfirmaNovoJogo = new Botao("CONFIRMAR");
		bConfirmaTroca = new Botao("CONFIRMAR");
		bAtivaOlho = new Botao("");

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
		configBotao(bAtivaOlho, I2_B_OLHO);

		bAtivaOlho.setBounds(POS_OLHO_X, POS_OLHO_Y, 50, 50);
		bAtivaOlho.setColor(0, new Color(0, 0, 0, 0));
		bAtivaOlho.setColor(1, new Color(255, 255, 255, 64));
		bAtivaOlho.setColor(2, new Color(0, 0, 0, 0));

		iP.setIDObservador(I2_INFOP);
	}

	private void configBotao(Botao b, int i2) {
		/**
		 * Metodo que configura um botao com seu ID e como nao clicavel, adiciona um
		 * observador, eventos de mouse.
		 */
		b.setIDBotao(i2);
		b.addObservador(this);
		addMouseListener(b);
		addMouseMotionListener(b);
		b.setClivael(false);
	}

	public void notify(ObservadoIF o) {
		/** Metodo que recebe uma notificacao dos observados. */
		int i1 = o.get(1); // i1 = 0: mouse clicou em algo
		int i2 = o.get(2); // Pega o ID do observado.

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
					view.proxEtapa();
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
					view.ataque(atacante, defensor);
				} else {
					view.ataque(atacante, defensor, view.getListaDados());
				}
			}

			// Ação do botao "bAtaqueN"
			else if (i2 == I2_B_ATAQUE_N) {
				view.ataca(atacante, defensor);
				fora = true;
			}

			// Ação do botao "bSalvar"
			else if (i2 == I2_B_SALVAR) {
				if (DEBUG)
					System.out.println("*******etapa: " + Integer.toString(view.getEtapa()));

				String path = view.salvaFile();
				if (path != null) {
					view.saveState(path);
					view.click(null); // gasta um click automatico
				}
				fora = true;
			}

			// Ação do botão "bIniciar"
			else if (i2 == I2_B_INICIAR) {
				exibeMenuInicial = false;
				exibeNovoJogo = true;
				if (janelaExibida) {
					limpaJanela();
				}
				bConfirmaNovoJogo.setClivael(true);
				fora = true;
			}

			// Ação do botao "bCarregar"
			else if (i2 == I2_B_CARREGAR) {
				String path = view.selecionaFile();
				int load = view.loadGame(path);
				if (load == 0) {
					exibeMenuInicial = false;
					view.click(null); // gasta um click automatico
					if (janelaExibida) {
						limpaJanela();
						inicio = false;
					}
				}
				fora = true;
			}

			// Ação do botao "bCarregarAuto"
			else if (i2 == I2_B_CARREGAR_AUTO) {
				// carrega o ultimo jogo que foi fechado e que foi salvo automaticamente
				int load = view.loadGameAuto();
				if (load == 0) {
					exibeMenuInicial = false;
					view.click(null); // gasta um click automatico
					if (janelaExibida) {
						limpaJanela();
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
						view.novoJogo(nomes);
						// bConfirmaNovoJogo.setClivael(false);
						exibeNovoJogo = false;
						limpaJanela();
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

				if (count == 3 && view.verificaTrocaCartas(cartasSelecionadas)) {
					bConfirmaTroca.setClivael(true);
				} else {
					bConfirmaTroca.setClivael(false);
				}
			}

			// Ação do botão "bConfirmaTroca"
			else if (i2 == I2_B_CONFIRMA_TROCA) {
				view.confirmaTroca(cartasSelecionadas);
				limpaJanela();
				obrigaExibeCartas = false;
				fora = true;
			}

			// Ação do botão "bAtivaOlho"
			else if (i2 == I2_B_OLHO) {
				olho_ativado = !olho_ativado;
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
		/** Metodo que pinta o GamePanel. */
		super.paintComponent(g);
		Point mousePosition = getMousePosition();
		try {
			xM = (int) mousePosition.getX();
			yM = (int) mousePosition.getY();
		} catch (Exception e) {
			xM = -1;
			yM = -1;
		}

		g.drawImage(tabuleiro, 0, 0, getWidth(), getHeight(), null);

		for (Territorio t : territorios) {
			t.draw(g);
			if (olho_ativado)
				t.draw_name(g);
		}

		iP.draw(g);

		bSalvar.setPos(g, 110, 450);
		if (bSalvar.atualiza(g, xM, yM)) {
			fora = false;
		}
		bSalvar.draw(g);

		if (bAtivaOlho.atualiza(g, xM, yM)) {
			fora = false;
		}
		bAtivaOlho.draw(g);

		g.setFont(fonte);

		g.drawImage(olho_ativado ? imgOlhoRiscado : imgOlho, POS_OLHO_X, POS_OLHO_Y, 50, 50, null);

		// Desenha (ou nao) as janelas do jogo:
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
		/** Metodo que trata o click do mouse. */
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
		/** Metodo que define que a tela de ataque será exibida. */
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
		/**
		 * Metodo que limpa a janela e exibe define a tela de resultado de ataque como
		 * visivel de novo.
		 */
		if (DEBUG)
			System.out.println("Exibe resultado ataque");
		limpaJanela();
		exibeResultadoAtaque = true;
	}

	public void conquista() {
		/** Metodo que define e configura a tela de conquista de um territorio. */
		limpaJanela();
		exibeConquista = true;

		int y = getHeight() * 15 / 100;
		int y_inferior = getHeight() - y;
		int x_centro = getWidth() / 2;
		int space = 50;

		int n, cor;

		// Mostra territorio "temporário" do atacante
		temp1 = new Territorio(atacante, x_centro - space, y_inferior - 50, -90);
		n = view.getQtdExercitos(atacante);
		temp1.setNum(n);
		cor = view.getCor(atacante);
		temp1.setCor(cores[cor]);
		temp1.setClicavel(true);
		temp1.setMarcado(true);

		temp1.addObservador(this);
		view.registra(temp1.getNome(), temp1);
		addMouseListener(temp1);
		addMouseMotionListener(temp1);

		// Mostra territorio "temporário" do antigo defensor
		temp2 = new Territorio(defensor, x_centro + space, y_inferior - 50, -90);

		n = view.getQtdExercitos(defensor);
		temp2.setNum(n);
		cor = view.getCor(defensor);
		temp2.setCor(cores[cor]);
		temp2.setClicavel(true);
		temp2.setMarcado(true);

		temp2.addObservador(this);
		view.registra(temp2.getNome(), temp2);
		addMouseListener(temp2);
		addMouseMotionListener(temp2);

		temp1.setIDTerritorio(I2_TEMP1);
		temp2.setIDTerritorio(I2_TEMP2);
	}

	public void setExibeCartas(boolean b) {
		/** Metodo que define a exibicao das cartas. */
		exibeCartas = b;
		repaint();
	}

	public void setExibeTabelas(boolean b) {
		/** Metodo que define a exibicao das tabelas de conversao. */
		exibeTabelas = b;
		repaint();
	}

	public void setExibeObjetivo(boolean b) {
		/** Metodo que define a exibicao do objetivo. */
		exibeObjetivo = b;
		repaint();
	}

	public void exibeNovoJogoNovamente(String[] nomesJogadores) {
		/** Metodo que preenche os TextFields com os nomes dos novos jogadores. */
		for (int i = 0; i < tfNomes.length; i++) {
			tfNomes[i].setText(nomesJogadores[i] != null ? nomesJogadores[i] : "");
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
		/** Metodo que define a obrigacao de troca de cartas. */
		obrigaExibeCartas = true;
	}

	/*------------------------------------------------------------------------------------------------------------------- */
	/* Métodos auxiliares para o desenho da tela: */

	private void exibeJanela(Graphics g) {
		/**
		 * Metodo que exibe uma janela genérica, usada para a exibicao de outros
		 * componentes do jogo.
		 */
		janelaExibida = true;
		bSalvar.setClivael(false);
		bAtivaOlho.setClivael(false);
		iP.setClivael(false);

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

		if (!(exibeMenuInicial || exibeNovoJogo || obrigaExibeCartas))
			drawStr(g, "Clique em quase qualquer lugar para fechar.", getWidth() / 2, y + fAlt * 2);
	}

	private void limpaJanela() {
		/** Metodo que limpa as janelas do GamePanel. */
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
		bConfirmaNovoJogo.setClivael(false);
		bConfirmaTroca.setClivael(false);

		bSalvar.setClivael(true);
		bAtivaOlho.setClivael(true);
		iP.setClivael(true);

		if (exibeConquista) {
			view.desregistra(temp1.getNome(), temp1);
			view.desregistra(temp2.getNome(), temp2);

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
		for (int i = 0; i < 5; i++) {
			bCartas[i].setColor(0, new Color(0, 0, 0, 0));
			bCartas[i].setColor(1, new Color(128, 128, 128, 64));
			cartasSelecionadas[i] = false;
			bCartas[i].setClivael(false);
		}
	}

	private void drawStr(Graphics g, String text, int xc, int yc) {
		/** Metodo que escreve um texto na tela dado sua coordenada de centro. */
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(fonte);
		FontRenderContext frc = g2d.getFontRenderContext();
		int x = xc - (int) fonte.getStringBounds(text, frc).getWidth() / 2;
		int y = yc - (int) fonte.getStringBounds(text, frc).getHeight() / 2;
		g.setColor(Color.BLACK);
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				g.drawString(text, x + i, y + j);
			}
		}
		g.setColor(Color.WHITE);
		g.drawString(text, x, y);
	}

	private void drawStr(Graphics g, String text, int xc, int yc, Color corTexto, Color corBorda) {
		/**
		 * Metodo que escreve um texto na tela dado sua coordenada de centro, cor de
		 * borda e cor do texto.
		 */
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(fonte);
		FontRenderContext frc = g2d.getFontRenderContext();
		int x = xc - (int) fonte.getStringBounds(text, frc).getWidth() / 2;
		int y = yc - (int) fonte.getStringBounds(text, frc).getHeight() / 2;
		g.setColor(corBorda);
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				g.drawString(text, x + i, y + j);
			}
		}
		g.setColor(corTexto);
		g.drawString(text, x, y);
	}

	private void drawStr(Graphics g, String text, int xc, int yc, Font font) {
		/**
		 * Metodo que escreve um texto na tela dado sua coordenada de centro e sua
		 * fonte.
		 */
		Graphics2D g2d = (Graphics2D) g;
		g2d.setFont(font);
		FontRenderContext frc = g2d.getFontRenderContext();

		int x = xc - (int) font.getStringBounds(text, frc).getWidth() / 2;
		int y = yc - (int) font.getStringBounds(text, frc).getHeight() / 2;

		g.setColor(Color.BLACK);
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				g.drawString(text, x + i, y + j);
			}
		}
		g.setColor(Color.WHITE);

		g.drawString(text, x, y);
	}

	private void exibeDados(Graphics g) {
		/** Metodo que exibe/desenha os dados de ataque e defesa na tela. */
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
		/** Metodo que exibe/desenha a tela de ataque. */
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

		int corA = view.getCor(atacante);
		int corD = view.getCor(defensor);
		Color corAtacante = cores[corA];
		Color corDefensor = cores[corD];
		String nomeAtacante = view.getNomeJogador(corA);
		String nomeDefensor = view.getNomeJogador(corD);

		drawStr(g2d, String.format("%s (%d) %s", atacante, view.getQtdExercitos(atacante), nomeAtacante), x_centro,
				y_inferior - fAlt * 4, corAtacante, Color.BLACK);
		drawStr(g2d, "x", x_centro, y_inferior - fAlt * 3);
		drawStr(g2d, String.format("%s (%d) %s", defensor, view.getQtdExercitos(defensor), nomeDefensor), x_centro,
				y_inferior - fAlt * 2, corDefensor, Color.BLACK);
	}

	private void exibeTelaResultadoAtaque(Graphics g) {
		/** Metodo que desenha a tela de resultados de ataque. */
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

		if (view.getQtdExercitos(atacante) > 1) {
			bAtaqueN.setPos(g, x_centro, y_inferior - fAlt);
			bAtaqueN.setClivael(true);
			if (bAtaqueN.atualiza(g, xM, yM)) {
				fora = false;
			}
			bAtaqueN.draw(g);
		}

		int minDados = dados[0].length < dados[1].length ? dados[0].length : dados[1].length;

		int perdaAtacante = 0;
		int perdaDefensor = 0;
		for (int i = 0; i < minDados; i++) {
			if (dados[0][i] > dados[1][i]) {
				perdaDefensor++;
			} else {
				perdaAtacante++;
			}
		}

		drawStr(g2d, String.format("Atacante perdeu %d exército(s)", perdaAtacante), x_centro, y_inferior - fAlt * 4,
				Color.RED,
				Color.BLACK);

		drawStr(g2d, String.format("Defensor perdeu %d exército(s)", perdaDefensor), x_centro,
				y_inferior - fAlt * 5 / 2, Color.YELLOW, Color.BLACK);
	}

	private void exibeTelaConquista(Graphics g) {
		/** Metodo que desenha a tela de conquista de territorio. */
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
		/** Metodo que exibe/desenha as cartas do jogador. */
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

		String[] nomesCartas = view.getCartasJogador();
		// Conectar o nome das cartas com as imagens
		for (int i = 0; i < nomesCartas.length; i++) {
			imagemCarta = images.getImage(Territorio.getImgTerritorio(nomesCartas[i]));
			if (DEBUG) {
				System.out.println("CARTA " + nomesCartas[i]);
				System.out.println("CARTA " + Territorio.getImgTerritorio(nomesCartas[i]));
			}
			g2d.drawImage(imagemCarta, x_centro + (i - 2) * marginLeft - imagemCarta.getWidth(null) / 2,
					y_centro - imagemCarta.getHeight(null) / 2, null);

			if (view.podeTrocar()) {
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

		drawStr(g, String.format("Próxima troca receberá %d exércitos.", view.getExeAdCartas()), x_centro,
				y_inferior - 2 * fAlt);

		if (view.podeTrocar()) {
			bConfirmaTroca.setPos(g2d, x_centro, y_inferior - fAlt);
			if (bConfirmaTroca.atualiza(g, xM, yM)) {
				fora = false;
			}
			bConfirmaTroca.draw(g2d);
		}

	}

	private void exibeTabelas(Graphics g) {
		/** Metodo que exibe a janela que mostra as tabelas de bônus e de trocas */
		if (!exibeTabelas)
			return;

		Graphics2D g2d = (Graphics2D) g;
		Image imagemTabelaTroca = images.getImage("tabela_troca_cartas.png");
		Image imagemTabelaBonus = images.getImage("tabela_bonus_continentes.png");
		exibeJanela(g);

		int y_centro = getHeight() / 2;

		int largImgTroca = getWidth() / 5;
		int altImgTroca = largImgTroca * imagemTabelaTroca.getHeight(null) / imagemTabelaTroca.getWidth(null);

		int largImgBonus = getWidth() / 5;
		int altImgBonus = largImgBonus * imagemTabelaBonus.getHeight(null) / imagemTabelaBonus.getWidth(null);

		g2d.drawImage(imagemTabelaTroca, getWidth() / 3 - largImgTroca / 2, y_centro - altImgTroca / 2, largImgTroca,
				altImgTroca, null);
		g2d.drawImage(imagemTabelaBonus, 2 * getWidth() / 3 - largImgBonus / 2, y_centro - altImgBonus / 2,
				largImgBonus, altImgBonus, null);
	}

	private void exibeObjetivo(Graphics g) {
		if (!exibeObjetivo || view.getCorAtual() == -1)
			return;
		Graphics2D g2d = (Graphics2D) g;
		int objetivoNum = Integer.parseInt(view.getImgNameObjetivo().replaceAll("\\D+", ""));
		Image imagemObjetivo = images.getImage(view.getImgNameObjetivo());
		FontRenderContext frc = g2d.getFontRenderContext();
		int x_centro = getWidth() / 2;
		int y_centro = getHeight() / 2;
		int fAlt = (int) fonte.getStringBounds("", frc).getHeight(); // altura da fonte
		int y_superior = getHeight() * 15 / 100; // Altura da borda superior da tela
		int y_inferior = getHeight() - y_superior; // Altura da borda inferior da tela
		int pos_x = x_centro;
		int pos_y = y_inferior - 3 * fAlt;

		exibeJanela(g);
		g2d.drawImage(imagemObjetivo, x_centro - imagemObjetivo.getWidth(null) / 2,
				y_centro - imagemObjetivo.getHeight(null) / 2 - fAlt, null);

		String[] descObjetivo = view.getDescricaoObjetivo().split("\n");
		if (DEBUG)
			System.out.println("***Objetivo: " + objetivoNum);
		/*
		 * Reduz o tamanho da fonte atual para desenhar a string depois volta para a
		 * fonte q tava antes
		 */
		Font fonte_atual = g2d.getFont();
		Font font_nova = fonte_atual.deriveFont(18f);
		for (String obj : descObjetivo) {
			drawStr(g2d, obj, pos_x, pos_y, font_nova);
			pos_y += fAlt;
		}
	}

	private void exibeTelaMenuInicial(Graphics g) {
		/** Metodo que exibe/desenha a tela de menu inicial. */
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
		/** Metodo que exibe a tela de começo de um novo jogo. */
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
