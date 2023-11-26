package Model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import Controller.ControllerAPI;
import Observer.ObservadorIF;

public class ModelAPI {
	// Flag que desativa o print no terminal:
	private final boolean DEBUG = false;
	private final Cores[] cores = { Cores.AMARELO, Cores.AZUL, Cores.BRANCO, Cores.PRETO, Cores.VERDE, Cores.VERMELHO };

	private static ModelAPI instance;
	private Jogo jogo = new Jogo();
	private Jogador jAtual;

	private ModelAPI() {
	}

	public static ModelAPI getInstance() {
		/** Funcao que pega a unica instancia de ModelAPI. */
		if (instance == null) {
			instance = new ModelAPI();
		}
		return instance;
	}

	public void adicionaJogador(String nome, int cor) {
		/** Funcao que cria um novo jogador e o adiciona no jogo. */
		jogo.adicionaJogador(new Jogador(cores[cor], nome));
	}

	public int getQtdExercitos(String territorio) {
		/**
		 * Funcao que retorna a quantidade de exercitos em um territorio dado seu nome.
		 */
		return Territorio.getTerritorio(territorio).getQntdExercitos();
	}

	public int getCor(String territorio) {
		/**
		 * Funcao que retorna o indice da cor do dono de um territorio na lista de
		 * cores.
		 */
		Jogador dono = Territorio.getTerritorio(territorio).getDono();
		if (dono == null)
			return -1;
		Cores c = dono.getCor();
		for (int i = 0; i < cores.length; i++) {
			if (cores[i] == c)
				return i;
		}
		return -1;
	}

	public int getCorAtual() {
		/** Funcao que retorna o indice da cor do jogador atual. */
		if (jAtual == null)
			return -1;
		Cores c = jAtual.getCor();
		for (int i = 0; i < cores.length; i++) {
			if (cores[i] == c)
				return i;
		}
		return -1;
	}

	public String getNomeJogadorAtual() {
		/** Funcao que retorna o nome do jogador atual. */
		return jAtual.getNome();
	}

	public String getNomeJogador(int cor) {
		/** Funcao que retorna o nome de um jogador dada sua cor. */
		if (jogo.getJogadorCor(cores[cor]) == null) {
			return null;
		}
		return jogo.getJogadorCor(cores[cor]).getNome();
	}

	public int getProxCor() {
		/** Funcao que passa a vez para o proximo jogador e retorna a cor dele. */
		jAtual = jogo.getProxJogador();
		return getCorAtual();
	}

	public String[] getCartasJogador() {
		/** Funcao que retorna o nome das cartas do jogador atual */
		return getCartasJogador(jAtual);
	}

	String[] getCartasJogador(Jogador jogador) {
		/**
		 * Funcao que retorna uma string com o nome das cartas de um jogador.
		 * Sobrescrita usada no saveState().
		 */
		Carta[] cartas = jogador.getCartas();
		String[] nome_cartas = new String[cartas.length];
		for (int i = 0; i < cartas.length; i++) {
			if (cartas[i].getTerritorio() != null) {
				nome_cartas[i] = cartas[i].getTerritorio().getNome();
			}
		}
		return nome_cartas;
	}

	public String[] getTerritorios(int cor) {
		/**
		 * Funcao que retorna uma lista dos nomes dos territorios de um jogador dada a
		 * sua cor.
		 */
		Jogador j = jogo.getJogadorCor(cores[cor]);
		Territorio[] territorios = j.getTerritorios();
		String[] lst = new String[territorios.length];
		for (int i = 0; i < territorios.length; i++) {
			lst[i] = territorios[i].getNome();
		}
		return lst;
	}

	public String[] getVizinhos(String territorio) {
		/** Funcao que retorna os nomes dos vizinhos de um dado territorio. */
		Territorio[] viz = Territorio.getTerritorio(territorio).getVizinhos();
		String[] lst = new String[viz.length];
		for (int i = 0; i < viz.length; i++) {
			lst[i] = viz[i].getNome();
		}
		return lst;
	}

	public boolean verificaCondicoesAtaque(String atacante, String defensor) {
		/**
		 * Funcao que verifica as condicoes de ataque dado os nomes dos atacante e
		 * defensor.
		 */
		Territorio atac = Territorio.getTerritorio(atacante);
		Territorio def = Territorio.getTerritorio(defensor);
		return atac.verificaCondicoesAtaque(def);
	}

	public int[][] ataque(String atacante, String defensor) {
		/**
		 * Funcao que retorna uma matriz dos dados de um ataque automativo. Usado no
		 * ControllerAPI.
		 */
		Territorio atac = Territorio.getTerritorio(atacante);
		Territorio def = Territorio.getTerritorio(defensor);
		int[][] listaDados;
		listaDados = atac.atacar(def);
		return listaDados;
	}

	public void ataque(String atacante, String defensor, int[][] dados) {
		/**
		 * Funcao que recebe uma matriz dos dados de um ataque manual. Usado no
		 * ControllerAPI.
		 */
		Territorio atac = Territorio.getTerritorio(atacante);
		Territorio def = Territorio.getTerritorio(defensor);
		atac.atacar(def, dados);
	}

	public String[] getContinentes() {
		/** Funcao que retorna uma lista dos nomes dos continentes. */
		Continente[] conts = Continente.getContinentes();
		String[] strConts = new String[conts.length];
		for (int i = 0; i < conts.length; i++) {
			strConts[i] = conts[i].getNome();
		}
		return strConts;
	}

	public String[] getTerritoriosContinente(String continente) {
		/** Funcao que retorna o nome dos territorios de um continente dado seu nome. */
		Continente cont = Continente.getContinente(continente);
		Territorio[] territorios = cont.getTerritorios();
		String[] strTerritorios = new String[territorios.length];
		for (int i = 0; i < territorios.length; i++) {
			strTerritorios[i] = territorios[i].getNome();
		}
		return strTerritorios;
	}

	public boolean verificaTrocaCartas(boolean[] cartasSelecionadas) {
		/**
		 * Funcao que verifica as condicoes para a troca das cartas selecionadas. Usada
		 * no ControllerAPI.
		 */
		Carta[] cartas = jAtual.getCartas();
		Carta[] cartasRespectivas = new Carta[3];

		int j = 0;
		for (int i = 0; i < cartas.length; i++) {
			if (cartasSelecionadas[i]) {
				cartasRespectivas[j++] = cartas[i];
			}
		}
		if (j != 3) {
			if (DEBUG)
				System.out.println("Quantidade de cartas selecionadas inválida!");
			return false;
		}
		return Carta.verificaTroca(cartasRespectivas);
	}

	public int trocaCartas(boolean[] cartasSelecionadas) {
		/** Funcao que realiza a troca de cartas. Usada no ControllerAPI. */
		Carta[] cartasRespectivas = new Carta[3];

		int j = 0;
		for (int i = cartasSelecionadas.length - 1; i >= 0; i--) {
			if (cartasSelecionadas[i]) {
				cartasRespectivas[j++] = jAtual.removeCarta(i);
			}
		}
		return jogo.trocaCartas(jAtual, cartasRespectivas);
	}

	public int getExeAdContinente(String continente) {
		/**
		 * Funcao que retorna a quantidade de exercitos adicionais de um continente caso
		 * o jogador atual seja seu dono.
		 */
		if (DEBUG)
			System.out.println(jAtual.getNome());
		Continente cont = Continente.getContinente(continente);
		if (cont.pertence(jAtual)) {
			if (DEBUG)
				System.out.println(cont.getNome() + jAtual.getNome());

			return cont.getNumExeAdicionais();
		}
		return 0;
	}

	public int getExeAd() {
		/** Funcao que retorna a quantidade de exercitos adicionais do jogador atual. */
		return jAtual.getExeAd();
	}

	public void addExe(String territorio, int n) {
		/** Funcao que acrescenta n exercitos em um territorio dado seu nome. */
		Territorio.getTerritorio(territorio).acrescentaExe(n);
	}

	public void reduzExe(String territorio, int n) {
		/** Funcao que reduz n exercitos em um territorio dado seu nome. */
		Territorio.getTerritorio(territorio).reduzExe(n);
	}

	public void entregaCarta() {
		/** Funcao que entrega uma carta ao jogador atual. */
		jogo.entregaCarta(jAtual);
	}

	public String getImgNameObjetivo() {
		/** Funcao que retorna o nome da imagem do objetivo do jogador atual. */
		return jAtual.getImgNameObjetivo();
	}

	public String getDescricaoObjetivo() {
		/** Funcao que retorna a descricao do objetivo do jogador atual. */
		return jAtual.getDescricaoObjetivo();
	}

	private int getIndiceCor(String cor) {
		/** Funcao que retorna o indice da cor dada seu nome. Uasada no loadGame(). */
		for (int i = 0; i < cores.length; i++) {
			if (cores[i].name().equals(cor)) {
				return i;
			}
		}
		return -1;
	}

	public boolean verificaObjetivo() {
		/** Funcao que verifica o objetivo do jogador. */
		return jAtual.verificaObjetivo();
	}

	public void registra(String territorio, ObservadorIF o) {
		/** Funcao que registra o observador de um territorio dado seu nome. */
		Territorio t = Territorio.getTerritorio(territorio);
		t.addObservador(o);
	}

	public void desregistra(String territorio, ObservadorIF o) {
		/** Funcao que desregistra um observador de um territorio dado seu nome. */
		Territorio t = Territorio.getTerritorio(territorio);
		t.removeObservador(o);
	}

	public void novoJogo(String[] nomes) {
		/**
		 * Funcao que comeca um novo jogo dados os nomes dos jogadores. Usada no
		 * ControllerAPI.
		 */
		Territorio[] territorios = Territorio.getTerritorios();
		for (Territorio t : territorios) {
			t.trocaDono(null);
		}
		jogo = new Jogo();
		for (int i = 0; i < nomes.length; i++) {
			if (nomes[i] != null) {
				Cores c = cores[i];
				jogo.adicionaJogador(new Jogador(c, nomes[i]));
			}
		}
		jogo.inicializa();
		jAtual = jogo.getProxJogador();
	}

	public void saveState(String path) {
		/**
		 * Funcao que salva o estado do jogo em um arquivo txt. Usado no ControllerAPI.
		 */
		BufferedWriter writer = null;
		File file = new File(path);
		if (file.exists()) {
			if (DEBUG)
				System.out.println("Arquivo sobrescrito: " + file.getName());
		} else {
			if (DEBUG)
				System.out.println("Arquivo criado: " + file.getName());
		}
		if (DEBUG)
			jogo.exibeJogadores();

		jogo.getEstadoStr();

		// Inicializa o escritor do arquivo:
		try {
			// Esse construtor faz com que o arquivo seja aberto para escrita sobrescrevendo
			// o que já existe:
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8));

			writer.write(ControllerAPI.getInstance().getEstadoStr());
			writer.newLine();
			writer.write(jogo.getEstadoStr());

		} catch (IOException e) {
			if (DEBUG)
				System.out.println("Erro ao escrever no arquivo de salvamento do jogo.");
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					if (DEBUG)
						System.out.println("Erro ao fechar o escritor do arquivo.");
					e.printStackTrace();
				}
			}
		}
	}

	public int loadGame(String path) {
		/** Funcao que carrega um jogo já existente através da leitura de um txt. */
		if (path == null) {
			if (DEBUG)
				System.out.println("Arquivo de salvamento não foi selecionado.");
			return -1;
		}
		File file = new File(path);
		if (file.exists()) {
			if (DEBUG)
				System.out.printf("Arquivo %s aberto.\n", file.getName());
		} else {
			if (DEBUG)
				System.out.println("Erro ao abrir arquivo.");
			return -1;
		}

		Territorio[] territorios = Territorio.getTerritorios();
		for (Territorio t : territorios) {
			t.trocaDono(null);
		}
		jogo = new Jogo();

		if (DEBUG)
			System.out.println("JOGO JÁ EXISTENTE ABERTO COM SUCESSO.");
		BufferedReader reader = null;
		String nomeJogador;
		String[] assassinos = new String[6];
		Jogador[] jogadores = new Jogador[6];
		int corJogador;
		int numObjetivo;
		int i = 0;
		Carta carta;
		String[] cartas;
		String[] info;
		Baralho<Carta> monte;
		Territorio territorio;

		try {
			reader = new BufferedReader(new FileReader(file));
			String line;

			if (DEBUG)
				System.out.println("Lendo estado do Controle");
			line = reader.readLine();
			ControllerAPI.getInstance().setEstado(line);

			if (DEBUG)
				System.out.println("Lendo o contador de trocas e iterador");

			line = reader.readLine();
			info = line.split(",");
			jogo.setContadorTroca(Integer.parseInt(info[0]));
			jogo.setIterador(Integer.parseInt(info[1]) - 1);

			if (DEBUG)
				System.out.println("Contador de trocas = " + line);

			line = reader.readLine(); // Lê as cartas do monte
			cartas = line.split(",");

			monte = new Baralho<Carta>();

			if (DEBUG)
				System.out.println("Lendo cartas do monte");

			for (String c : cartas) {
				if (c.equals("CORINGA")) {
					carta = new Carta(null, Simbolos.CORINGA);
				} else
					carta = Territorio.getCarta(c);
				if (carta == null) {
					if (DEBUG)
						System.out.printf("Carta '%s' inválida\n", c);
				}
				monte.adiciona(carta);
			}
			jogo.setCartas(monte);

			line = reader.readLine(); // Lê as cartas do monte de usadas
			cartas = line.split(",");

			monte = new Baralho<Carta>();

			if (DEBUG)
				System.out.println("Lendo cartas do monte usado");

			for (String c : cartas) {
				if ("CORINGA".equals(c))
					carta = new Carta(null, Simbolos.CORINGA);
				else
					carta = Territorio.getCarta(c);
				if (carta == null) {
					if (DEBUG)
						System.out.printf("Carta '%s' inválida\n", c);
				}
				monte.adiciona(carta);
			}
			jogo.setCartasUsadas(monte);

			while ((line = reader.readLine()) != null) {
				while (!line.contains(";")) {
					if (DEBUG)
						System.out.println("Lendo linha: " + line + "\nReferente ao jogador " + Integer.toString(i));
					info = line.split(",");
					nomeJogador = info[0];
					corJogador = getIndiceCor(info[1]);
					assassinos[i] = info[2];

					if (DEBUG)
						System.out.println("Adicionando jogador " + nomeJogador + " " + corJogador);
					jogadores[i] = new Jogador(cores[corJogador], nomeJogador);

					jogo.adicionaJogador(jogadores[i]);

					if (DEBUG)
						System.out.println("Adicionando jogador " + nomeJogador + " " + corJogador);

					line = reader.readLine(); // Lê o objetivo do jogador
					numObjetivo = Integer.parseInt(line);
					if (DEBUG)
						System.out.println("Adicionando o objetivo do jogador: " + nomeJogador + " | Objetivo "
								+ Integer.toString(numObjetivo) + ": "
								+ Objetivo.getObjetivo(numObjetivo - 1).getDescricao());

					jogadores[i].setObjetivo(Objetivo.getObjetivo(numObjetivo - 1));

					line = reader.readLine(); // Lê as cartas do jogador
					cartas = line.split(",");
					if (cartas.length != 0) {
						for (String c : cartas) {
							if (c.equals("CORINGA"))
								carta = new Carta(null, Simbolos.CORINGA);
							else
								carta = Territorio.getCarta(c);
							if (carta == null) {
								if (DEBUG)
									System.out.printf("Carta '%s' inválida\n", c);
							}
							jogadores[i].recebeCarta(carta);

						}
					}
					while (!(line = reader.readLine()).contains(";")) { // Lê os territorios do jogador
						String[] infoTerritorio = line.split(",");
						String nomeTerritorio = infoTerritorio[0];
						int qtdExercitos = Integer.parseInt(infoTerritorio[1]);

						territorio = Territorio.getTerritorio(nomeTerritorio);

						territorio.trocaDono(jogadores[i]);
						territorio.setQtdExercitos(qtdExercitos);

						if (DEBUG)
							System.out.println("Adicionando territorio " + nomeTerritorio + " ao jogador " + nomeJogador
									+ " com " + Integer.toString(qtdExercitos) + " exercitos.");
					}
					i++;
				}
				for (int j = 0; j < i; j++) { // percorrendo os assassinos
					if (!assassinos[j].equals("null")) {
						for (int k = 0; k < i; k++) { // percorrendo os jogadores
							if (jogadores[k].getCor().toString().equals(assassinos[j])) {
								jogadores[j].setAssassino(jogadores[k]);
							}
						}
					}
				}

				if (DEBUG) {
					for (int j = 0; j < i; j++) { // percorrendo os assassinos
						Jogador assassino = jogadores[j].getAssassino();
						if (assassino != null)
							System.out.printf("Jogador %s foi morto por %s :(\n", jogadores[j].getNome(),
									assassino.getNome());
						else
							System.out.printf("Jogador %s está vivo :)\n", jogadores[j].getNome());
					}
				}
			}

			jAtual = jogo.getProxJogador();
			if (DEBUG)
				System.out.printf("Inicializando jogo pelo jogador %s (%s)\n", jAtual.getNome(),
						jAtual.getCor().toString());

		} catch (IOException e) {
			return -1;
		} finally {
			try {
				reader.close();
			} catch (Exception e) {
			}
		}
		return 0;

	}

	void entregaCartaAssassino(Jogador morto) {
		/**
		 * Funcao que entrega as cartas do jogador morto para o assassino (jogador
		 * atual)
		 */
		jogo.entregaCartaAssassino(jAtual, morto);
	}

	int color2int(Cores cor) {
		/** Funcao que retorna o indice de uma cor na lista de cores. */
		for (int i = 0; i < cores.length; i++) {
			if (cores[i] == cor)
				return i;
		}
		return -1;
	}

	void exibeJogadorMorto(Jogador morto, Jogador assassino) {
		// ViewAPI.getInstance().exibeJogadorMorto(color2int(morto.getCor()),
		// color2int(assassino.getCor()));
	}

}
