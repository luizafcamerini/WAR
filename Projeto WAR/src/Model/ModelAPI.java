package Model;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import java.util.ArrayList;
import View.ObservadorIF;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class ModelAPI {
	private static ModelAPI instance;
	private Jogo jogo = new Jogo();;
	private Jogador jAtual;
	private int[][] listaDados;
	private boolean conquista = false;
	private File file;

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
		Cores c = jAtual.getCor();

		for (int i = 0; i < cores.length; i++) {
			if (cores[i] == c)
				return i;
		}
		return -1;
	}

	public String getJogadorAtual() {
		return jAtual.getNome();
	}

	public int getProxCor() {
		jAtual = jogo.getProxJogador();
		conquista = false;
		return getCorAtual();
	}

	public String[] getCartasJogador() {
		ArrayList<Carta> cartas = jAtual.getCartas();
		String[] nome_cartas = new String[cartas.size()];
		for (int i = 0; i < cartas.size(); i++) {
			if (cartas.get(i).getTerritorio() != null) {
				nome_cartas[i] = cartas.get(i).getTerritorio().getNome();
			}
		}
		return nome_cartas;
	}

	// Sobrescrita do metodo getCartasJogador para ser usado no saveState
	public String[] getCartasJogador(Jogador jogador) {
		ArrayList<Carta> cartas = jogador.getCartas();
		String[] nome_cartas = new String[cartas.size()];
		for (int i = 0; i < cartas.size(); i++) {
			if (cartas.get(i).getTerritorio() != null) {
				nome_cartas[i] = cartas.get(i).getTerritorio().getNome();
			}
		}
		return nome_cartas;
	}

	public void inicializaJogo() {
		jogo.inicializa();
		jAtual = jogo.getProxJogador();
		conquista = false;
	}

	public void conquistou() {
		conquista = true;
	}

	public boolean getConquista() {
		return conquista;
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

	public String[] getContinentes(){
		Continente[] conts = Continente.getContinentes();
		String[] strConts = new String[conts.length];
		for(int i = 0;i< conts.length;i++){
			strConts[i] = conts[i].getNome();
		}
		return strConts;
	}

	public String[] getTerritoriosContinente(String continente){
		Continente cont = Continente.getContinente(continente);
		Territorio[] territorios = cont.getTerritorios();
		String[] strTerritorios = new String[territorios.length];
		for (int i = 0; i<territorios.length;i++){
			strTerritorios[i] = territorios[i].getNome();
		}
		return strTerritorios;
	}

	public int getExeAdContinente(String continente){
		 System.out.println(jAtual.getNome());
		Continente cont = Continente.getContinente(continente);
		if (cont.pertence(jAtual)){
			System.out.println(cont.getNome()+ jAtual.getNome());

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
	
	private String salvaFile(){
		/** Funcao que retorna o path absoluto de salvamento do jogo por escolha do usuário. */
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory()); //cria um novo selecionador de arq
		int returnValue = jfc.showSaveDialog(jfc); //abre a janela do selecionador e retorna se ele salvou ou não
		String path;

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
			// System.out.println(selectedFile.getAbsolutePath());
			path = selectedFile.getAbsolutePath();
			return path;
		}
		return null;
	}

	public void saveState() {
		/** Funcao que salva o estado do jogo em um arquivo txt. */

		Jogador jogador = jAtual; // começa salvando pelo jogador da vez
		String nomeJogador;
		Territorio[] territorios;
		String nomeTerritorio;
		String objetivoJogador;
		int qtdExercitos;
		String corJogador;
		String[] cartasJogador;
		BufferedWriter writer = null;
		int iterador = jogo.getIterador();
		int i = 0;
		// recebe o arquivo de salvamento do jogo, se não existir, cria um novo
//		File file = new File("src/gameState.txt");
//		File file = this.file;
		if (this.file == null) {
			file = new File(salvaFile());
			if (file.exists()) {
				System.out.println("Arquivo criado: " + this.file.getName());
			} else {
//				System.out.println("Arquivo " + this.file.getName() + " já existe");
				System.out.println("Erro na criação do arquivo de salvamento.");
			}
		}
		else {
			System.out.println("Arquivo de salvamento " + this.file.getName() + " já existe.");
		}
		jogo.exibeJogadores();
		// inicializa o escritor do arquivo
		try {
			// esse construtor faz com que o arquivo seja aberto para escrita sobrescrevendo
			// o que já existe
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.file), StandardCharsets.UTF_8));

			// loop que escreve os dados no arquivo para cada jogador
			do {
				jogador = jogo.getJogador((iterador + i) % jogo.getQtdJogadores()); // pega o proximo jogador
				// salva os dados do jogador no arquivo txt
				corJogador = jogador.getCor().toString();
				objetivoJogador = jogador.getImgNameObjetivo().replaceAll("\\D+", "");
				cartasJogador = getCartasJogador(jogador);
				nomeJogador = jogador.getNome();

				writer.write(nomeJogador + ',' + corJogador); // Escreve o nome e a cor do jogador
				writer.newLine();
				writer.write(objetivoJogador); // Escreve o objetivo do jogador
				writer.newLine();
				if (cartasJogador.length != 0) {
					for (String c : cartasJogador) { // Escreve as cartas do jogador
						writer.write(c + ',');
					}
				} else {
					writer.write(',');
				}
				writer.newLine();

				territorios = jogador.getTerritorios();
				for (Territorio t : territorios) {
					nomeTerritorio = t.getNome();
					qtdExercitos = t.getQntdExercitos();
					writer.write(nomeTerritorio + "," + qtdExercitos);
					writer.newLine();
				}

				writer.write(";"); // separa os dados de cada jogador com um ponto e virgula
				writer.newLine();

				i++;
			} while (i < jogo.getQtdJogadores());

		} catch (IOException e) {
			System.out.println("Erro ao escrever no arquivo de salvamento do jogo.");
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					System.out.println("Erro ao fechar o escritor do arquivo.");
					e.printStackTrace();
				}
			}
		}

	}

	public int loadGame(String path) {
		/** Funcao que carrega um jogo já existente através da leitura de um txt */
//		File file = new File("src/gameState.txt");
		if (path == null) {
			System.out.println("Arquivo de salvamento não foi selecionado.");
			return -1;
		}
		this.file = new File(path);
		System.out.println("JOGO JÁ EXISTENTE ABERTO COM SUCESSO.");
		String nomeJogador;
		int corJogador;
		int[] numObjetivos = new int[6];
		int i = 0;
		jogo.adicionaCoringas(); // adiciona coringas no baralho
		BufferedReader reader = null;

		// jogo.limpa();

		try {
			reader = new BufferedReader(new FileReader(file));
			String line;
			while ((line = reader.readLine()) != null) {
				while (!line.contains(";")) {
					System.out.println("Lendo linha: " + line + "\nReferente ao jogador " + Integer.toString(i));
					String[] info = line.split(",");
					nomeJogador = info[0];
					corJogador = getIndiceCor(info[1]);
					System.out.println("Adicionando jogador " + nomeJogador + " " + corJogador);
					adicionaJogador(nomeJogador, corJogador); // ordinal retorna o indice do enum
					System.out.println("Adicionando jogador " + nomeJogador + " " + corJogador);

					line = reader.readLine(); // Lê o objetivo do jogador
					numObjetivos[i] = Integer.parseInt(line);
					System.out.println("Adicionando o objetivo do jogador: " + nomeJogador + " | Objetivo "
							+ Integer.toString(numObjetivos[i]) + ": "
							+ Objetivo.getObjetivo(numObjetivos[i] - 1).getDescricao());
					jogo.getJogador(i).setObjetivo(Objetivo.getObjetivo(numObjetivos[i] - 1));

					line = reader.readLine(); // Lê as cartas do jogador
					String[] cartas = line.split(",");
					if (cartas.length != 0) {
						for (String c : cartas) {
							jogo.entregaCarta(jogo.getJogador(i), c);
						}
					}
					while (!(line = reader.readLine()).contains(";")) { // Lê os territorios do jogador
						String[] infoTerritorio = line.split(",");
						String nomeTerritorio = infoTerritorio[0];
						int qtdExercitos = Integer.parseInt(infoTerritorio[1]);
						jogo.getJogador(i).addTerritorio(Territorio.getTerritorio(nomeTerritorio), qtdExercitos - 1);
						Territorio.getTerritorio(nomeTerritorio).setDono(jogo.getJogador(i));
						System.out.println("Adicionando territorio " + nomeTerritorio + " ao jogador " + nomeJogador
								+ " com " + Integer.toString(qtdExercitos) + " exercitos.");
					}
					i++;
				}
			}
			jAtual = jogo.getJogador((i) % jogo.getQtdJogadores());
			System.out.println("Inicializando jogo pelo jogador " + jAtual.getNome());
			jogo.continuaJogo(jAtual);
			jAtual = jogo.getProxJogador();
			// System.out.println(jAtual.getNome());

		} 
		catch (IOException e){
			return -1;
		}
		finally {
			try {
				reader.close();
			} catch (Exception e) {}
		}
		return 0;

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

	public void registra(String territorio, ObservadorIF o){
		Territorio t = Territorio.getTerritorio(territorio);
		t.addObservador(o);
	}

	public void desregistra(String territorio, ObservadorIF o){
		Territorio t = Territorio.getTerritorio(territorio);
		t.removeObservador(o);
	}

	
	public void novoJogo(String[] nomes){
		Territorio[] territorios = Territorio.getTerritorios();
		for(Territorio t: territorios){
			t.trocaDono(null);
		}
		jogo = new Jogo();
		for (int i = 0; i< nomes.length;i++){
			if(nomes[i]!=null){
				Cores c = cores[i];
				jogo.adicionaJogador(new Jogador(c,nomes[i]));
			}
		}
		jogo.inicializa();
		jAtual = jogo.getProxJogador();
	}
}
