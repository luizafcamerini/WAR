package Model;

import java.util.ArrayList;
import java.io.*;
import java.nio.charset.StandardCharsets;


public class ModelAPI {
	private static ModelAPI instance;
	private Jogo jogo = new Jogo();;
	private Jogador jAtual;
	private int[][] listaDados;
	private boolean conquista = false;
	
	private final Cores [] cores = {Cores.AMARELO, Cores.AZUL, Cores.BRANCO, Cores.PRETO, Cores.VERDE, Cores.VERMELHO};
	

	private ModelAPI() {}

	public static ModelAPI getInstance() {
		if (instance == null) {
			instance = new ModelAPI();
		}
		return instance;
	}

	public void adicionaJogador(String nome, int cor){
		jogo.adicionaJogador(new Jogador(cores[cor], nome));
	}
	
	
	public int getQtdExercitos(String territorio){
		return Territorio.getTerritorio(territorio).getQntdExercitos();
	}

	public int getCor(String territorio){
		Jogador dono = Territorio.getTerritorio(territorio).getDono();
		if (dono == null) return -1;
		Cores c =  dono.getCor();
		for(int i = 0; i < cores.length; i++){
			if (cores[i] == c)
				return i;
		}
		return -1;
	}
	
	public int getCorAtual() {
		Cores c = jAtual.getCor();
		
		for(int i = 0; i < cores.length; i++){
			if (cores[i] == c)
				return i;
		}
		return -1;
	}

	public int getProxCor() {
		jAtual = jogo.getProxJogador();
		conquista = false;
		return getCorAtual();
	}

	public String[] getCartasJogador(){
		ArrayList<Carta> cartas = jAtual.getCartas();
		String[] nome_cartas = new String[cartas.size()];
		for (int i = 0; i < cartas.size(); i++) {
			if (cartas.get(i).getTerritorio() != null){
				nome_cartas[i] = cartas.get(i).getTerritorio().getNome();
			}
		}
		return nome_cartas;
	}

	// Sobrescrita do metodo getCartasJogador para ser usado no saveState
	public String[] getCartasJogador(Jogador jogador){
		ArrayList<Carta> cartas = jogador.getCartas();
		String[] nome_cartas = new String[cartas.size()];
		for (int i = 0; i < cartas.size(); i++) {
			if (cartas.get(i).getTerritorio() != null){
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
	

	public void conquistou(){
		conquista = true;
	}

	public boolean getConquista(){
		return conquista;
	}
	
	public String []getTerritorios(int cor) {
		Jogador j = jogo.getJogadorCor(cores[cor]);
		Territorio []territorios = j.getTerritorios();
		String []lst = new String[territorios.length];
		for (int i = 0; i< territorios.length; i++) {
			lst[i] = territorios[i].getNome();
		}
		return lst;
		
	}

	public String []getVizinhos(String territorio) {
		Territorio []viz = Territorio.getTerritorio(territorio).getVizinhos();
		String []lst = new String[viz.length];
		for (int i = 0; i< viz.length; i++) {
			lst[i] = viz[i].getNome();	
		}
		return lst;
	}
	
	public int[][] ataca(String atacante, String defensor) {
    	Territorio atac = Territorio.getTerritorio(atacante);
    	Territorio def = Territorio.getTerritorio(defensor);
		listaDados = atac.atacar(def);
    	return listaDados;
    	
    }

	public int getExeAd() {
		return jAtual.getExeAd();
	}
	
	public void addExe(String territorio, int n) {
		Territorio.getTerritorio(territorio).acrescentaExe(n);
	}

	public void reduzExe(String territorio, int n){
		Territorio.getTerritorio(territorio).reduzExe(n);
	}

	public void entregaCarta(){
		jogo.entregaCarta(jAtual);
	}
	// public int[][] getListaDados() {
	// 	return listaDados;
	// }
	
	public String getImgNameObjetivo(){
		return jAtual.getImgNameObjetivo();
	}

	public void saveState(){
		/** Funcao que salva o estado do jogo em um arquivo txt.*/

		Jogador jogador = jAtual; // começa salvando pelo jogador da vez
		String nomeJogador;
		Territorio []territorios;
		String nomeTerritorio;
		String objetivoJogador;
		int qtdExercitos;
		String corJogador;
		String[] cartasJogador;
		BufferedWriter writer = null;
		int iterador = jogo.getIterador();
		int i = 0;
		// recebe o arquivo de salvamento do jogo, se não existir, cria um novo
		File file = new File("src/gameState.txt");
		if (!file.exists()) {
			try {
				if (file.createNewFile()) {
					System.out.println("Arquivo criado: " + file.getName());
				} else {
					System.out.println("Arquivo " + file.getName() + " já existe");
				}
			} catch (IOException e) {
				System.out.println("Erro no salvamento do jogo.");
				e.printStackTrace();
			}
		}
		jogo.exibeJogadores();
		// inicializa o escritor do arquivo
		try{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)); // esse construtor faz com que o arquivo seja aberto para escrita sobrescrevendo o que já existe
			
			// loop que escreve os dados no arquivo para cada jogador
			do {
				jogador = jogo.getJogador((iterador + i) % jogo.getQtdJogadores()); // pega o proximo jogador
				// salva os dados do jogador no arquivo txt
				corJogador = jogador.getCor().toString();
				objetivoJogador = jogador.getImgNameObjetivo().replaceAll("\\D+","");
				cartasJogador = getCartasJogador(jogador);
				nomeJogador = jogador.getNome();

				writer.write(nomeJogador + ',' + corJogador); // Escreve o nome e a cor do jogador
				writer.newLine(); 
				writer.write(objetivoJogador); // Escreve o objetivo do jogador
				writer.newLine();
				if(cartasJogador.length != 0){
					for (String c : cartasJogador) { // Escreve as cartas do jogador
					writer.write(c + ',');
					}
				}
				else{
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
			} while(i < jogo.getQtdJogadores());

		} catch (IOException e){
			System.out.println("Erro ao escrever no arquivo de salvamento do jogo.");
			e.printStackTrace();
		}
		finally {
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



	public void loadGame() throws IOException{
		/** Funcao que carrega um jogo já existente através da leitura de um txt */
		File file = new File("src/gameState.txt");
		if (!file.exists()) {
			System.out.println("Arquivo de salvamento não existe.");
			return;
		}
		String nomeJogador;
		int corJogador;
		int[] numObjetivos = new int[6]; 
		int i = 0;
		jogo.adicionaCoringas(); // adiciona coringas no baralho
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(file));
			String line;
			while((line = reader.readLine()) != null){
				while(!line.contains(";")){
					System.out.println("Lendo linha: " + line + "\nReferente ao jogador " + Integer.toString(i));
					String[] info = line.split(",");
					nomeJogador = info[0];
					corJogador = getIndiceCor(info[1]);
					System.out.println("Adicionando jogador " + nomeJogador + " " + corJogador);
					adicionaJogador(nomeJogador, corJogador); // ordinal retorna o indice do enum
					System.out.println("Adicionando jogador " + nomeJogador + " " + corJogador); 

					line = reader.readLine(); // Lê o objetivo do jogador
					numObjetivos[i] = Integer.parseInt(line);
					System.out.println("Adicionando o objetivo do jogador: " + nomeJogador + " | Objetivo " + Integer.toString(numObjetivos[i]) + ": " + Objetivo.getObjetivo(numObjetivos[i] - 1).getDescricao());
					jogo.getJogador(i).setObjetivo(Objetivo.getObjetivo(numObjetivos[i] - 1));

					line = reader.readLine(); // Lê as cartas do jogador
					String[] cartas = line.split(",");
					if(cartas.length != 0){
						for (String c : cartas) {
							jogo.entregaCarta(jogo.getJogador(i), c);
						}
					}
					while(!(line = reader.readLine()).contains(";")){ // Lê os territorios do jogador
						String[] infoTerritorio = line.split(",");
						String nomeTerritorio = infoTerritorio[0];
						int qtdExercitos = Integer.parseInt(infoTerritorio[1]);
						jogo.getJogador(i).addTerritorio(Territorio.getTerritorio(nomeTerritorio), qtdExercitos - 1);
						Territorio.getTerritorio(nomeTerritorio).setDono(jogo.getJogador(i));
						System.out.println("Adicionando territorio " + nomeTerritorio + " ao jogador " + nomeJogador + " com " + Integer.toString(qtdExercitos) + " exercitos.");
					}
					i++;
				}
			}
			jAtual = jogo.getJogador((i) % jogo.getQtdJogadores());
			System.out.println("Inicializando jogo pelo jogador " + jAtual.getNome());
			jogo.continuaJogo(jAtual);
			jAtual = jogo.getProxJogador();

		}
		finally{
			if (reader != null){
				reader.close();
			}
		}	

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

	public boolean verificaObjetivo(){
	/** Funcao que verifica o objetivo do jogador. */
		return jAtual.verificaObjetivo();
	}

}




