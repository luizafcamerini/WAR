package Model;

// import javax.swing.JFileChooser;
// import javax.swing.filechooser.FileSystemView;

import Controller.ControllerAPI;

// import java.util.ArrayList;
import View.ObservadorIF;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ModelAPI {
	private final boolean DEBUG = true;
	private static ModelAPI instance;
	private Jogo jogo = new Jogo();;
	private Jogador jAtual;
	private int[][] listaDados;
	// private File file;

	private final Cores[] cores = { Cores.AMARELO, Cores.AZUL, Cores.BRANCO, Cores.PRETO, Cores.VERDE, Cores.VERMELHO };

	private ModelAPI() {
	}

	int color2int(Cores cor) {
		for (int i = 0; i < cores.length; i++) {
			if (cores[i] == cor)
				return i;
		}
		return -1;
	}

	public static ModelAPI getInstance() {
		if (instance == null) {
			instance = new ModelAPI();
		}
		return instance;
	}

	public void adicionaJogador(String nome, int cor) {
		jogo.adicionaJogador(new Jogador(cores[cor], nome));
	}

	public int getQtdExercitos(String territorio) {
		return Territorio.getTerritorio(territorio).getQntdExercitos();
	}

	public int getCor(String territorio) {
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
		return jAtual.getNome();
	}

	public String getNomeJogador(int cor) {
		if (jogo.getJogadorCor(cores[cor]) == null){
			return null;
		}
		return jogo.getJogadorCor(cores[cor]).getNome();
	}

	public int getProxCor() {
		jAtual = jogo.getProxJogador();
		return getCorAtual();
	}

	public String[] getCartasJogador() {
		return getCartasJogador(jAtual);
	}

	// Sobrescrita do metodo getCartasJogador para ser usado no saveState
	String[] getCartasJogador(Jogador jogador) {
		Carta[] cartas = jogador.getCartas();
		String[] nome_cartas = new String[cartas.length];
		for (int i = 0; i < cartas.length; i++) {
			if (cartas[i].getTerritorio() != null) {
				nome_cartas[i] = cartas[i].getTerritorio().getNome();
			}
		}
		return nome_cartas;
	}

	public void inicializaJogo() {
		jogo.inicializa();
		jAtual = jogo.getProxJogador();
	}

	public String[] getTerritorios(int cor) {
		Jogador j = jogo.getJogadorCor(cores[cor]);
		Territorio[] territorios = j.getTerritorios();
		String[] lst = new String[territorios.length];
		for (int i = 0; i < territorios.length; i++) {
			lst[i] = territorios[i].getNome();
		}
		return lst;

	}

	public String[] getVizinhos(String territorio) {
		Territorio[] viz = Territorio.getTerritorio(territorio).getVizinhos();
		String[] lst = new String[viz.length];
		for (int i = 0; i < viz.length; i++) {
			lst[i] = viz[i].getNome();
		}
		return lst;
	}

	public boolean verificaCondicoesAtaque(String atacante, String defensor) {
		Territorio atac = Territorio.getTerritorio(atacante);
		Territorio def = Territorio.getTerritorio(defensor);
		return atac.verificaCondicoesAtaque(def);
	}

	public int[][] ataca(String atacante, String defensor) {
		Territorio atac = Territorio.getTerritorio(atacante);
		Territorio def = Territorio.getTerritorio(defensor);
		listaDados = atac.atacar(def);
		return listaDados;

	}

	public void ataca(String atacante, String defensor, int[][] dados) {
		Territorio atac = Territorio.getTerritorio(atacante);
		Territorio def = Territorio.getTerritorio(defensor);
		atac.atacar(def, dados);
		// return listaDados;

	}

	public String[] getContinentes() {
		Continente[] conts = Continente.getContinentes();
		String[] strConts = new String[conts.length];
		for (int i = 0; i < conts.length; i++) {
			strConts[i] = conts[i].getNome();
		}
		return strConts;
	}

	public String[] getTerritoriosContinente(String continente) {
		Continente cont = Continente.getContinente(continente);
		Territorio[] territorios = cont.getTerritorios();
		String[] strTerritorios = new String[territorios.length];
		for (int i = 0; i < territorios.length; i++) {
			strTerritorios[i] = territorios[i].getNome();
		}
		return strTerritorios;
	}

	public int getExeAdContinente(String continente) {
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
		return jAtual.getExeAd();
	}

	public void addExe(String territorio, int n) {
		Territorio.getTerritorio(territorio).acrescentaExe(n);
	}

	public void reduzExe(String territorio, int n) {
		Territorio.getTerritorio(territorio).reduzExe(n);
	}

	public void entregaCarta() {
		jogo.entregaCarta(jAtual);
	}

	public String getImgNameObjetivo() {
		return jAtual.getImgNameObjetivo();
	}

	private int getIndiceCor(String cor) {
		/** funcao auxiliar para loadGame() */
		for (int i = 0; i < cores.length; i++) {
			if (cores[i].name().equals(cor)) {
				return i;
			}
		}
		return -1; // retorna -1 se a cor não for encontrada
	}

	public boolean verificaObjetivo() {
		/** Funcao que verifica o objetivo do jogador. */
		return jAtual.verificaObjetivo();
	}

	public void registra(String territorio, ObservadorIF o) {
		Territorio t = Territorio.getTerritorio(territorio);
		t.addObservador(o);
	}

	public void desregistra(String territorio, ObservadorIF o) {
		Territorio t = Territorio.getTerritorio(territorio);
		t.removeObservador(o);
	}

	public void novoJogo(String[] nomes) {
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
		/** Funcao que salva o estado do jogo em um arquivo txt. */

		BufferedWriter writer = null;

		File file = new File(path);
		if (file.exists()) {
			if (DEBUG)
				System.out.println("Arquivo sobrescrito: " + file.getName());
		} else {
			if (DEBUG)
				System.out.println("Arquivo criado: "+ file.getName());
		}

		if (DEBUG)
			jogo.exibeJogadores();

		jogo.getEstadoStr();

		// inicializa o escritor do arquivo
		try {
			// esse construtor faz com que o arquivo seja aberto para escrita sobrescrevendo
			// o que já existe
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
		/** Funcao que carrega um jogo já existente através da leitura de um txt */
		// File file = new File("src/gameState.txt");
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
			// reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
			// "UTF-8"));
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

					// jogo.adicionaCoringas();
					// adicionaJogador(nomeJogador, corJogador); // ordinal retorna o indice do enum
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

				if(DEBUG){
					for (int j = 0; j < i; j++) { // percorrendo os assassinos
						Jogador assassino = jogadores[j].getAssassino();
						if(assassino != null)
							System.out.printf("Jogador %s foi morto por %s :(\n",jogadores[j].getNome(),assassino.getNome());
						else
							System.out.printf("Jogador %s está vivo :)\n",jogadores[j].getNome());
					}
				}
			}

			jAtual = jogo.getProxJogador();
			if (DEBUG)
				System.out.println("Inicializando jogo pelo jogador " + jAtual.getNome());
			// jogo.continuaJogo(jAtual);
			if (DEBUG)
				System.out.println(jAtual.getNome());

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

}
