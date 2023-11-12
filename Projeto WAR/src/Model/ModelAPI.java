package Model;

import java.util.ArrayList;
import java.io.*;


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
		Jogador j = jogo.getJogador(cores[cor]);
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

		// inicializa o escritor do arquivo
		try{
			writer = new BufferedWriter(new FileWriter(file, false)); // esse construtor faz com que o arquivo seja aberto para escrita sobrescrevendo o que já existe
			
			// loop que escreve os dados no arquivo para cara jogador
			do {
				// salva os dados do jogador no arquivo txt
				corJogador = jogador.getCor().toString();
				objetivoJogador = jogador.getImgNameObjetivo();
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
				jogador = jogo.getProxJogador();
			} while(jogador != jAtual);

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

	// public void loadGame() throws IOException{
	// 	File file = new File("src/gameState.txt");
	// 	if (!file.exists()) {
	// 		System.out.println("Arquivo de salvamento não existe.");
	// 		return;
	// 	}
	// 	String nomeJogador;
	// 	Cores corJogador;


	// 	BufferedReader reader = null;
	// 	try{
	// 		reader = new BufferedReader(new FileReader(file));
	// 		String line;
	// 		while((line = reader.readLine()) != null){
	// 			while(!line.contains(";")){
	// 				String[] info = line.split(",");
	// 				nomeJogador = info[0];
	// 				corJogador = Cores.valueOf(info[1]);
	// 				adicionaJogador(nomeJogador, corJogador.ordinal()); // ordinal retorna o indice do enum
	// 				line = reader.readLine();

	// 			}
	// 		}

	// 	}
	// 	finally{
	// 		if (reader != null){
	// 			reader.close();
	// 		}
	// 	}	

	// }
}

