package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

class Territorio {
	private static ArrayList<Carta> cartasTerritorio = new ArrayList<Carta>();
	private static Hashtable<String, Territorio> territorios = new Hashtable<String, Territorio>();

	private ArrayList<Territorio> vizinhos = new ArrayList<Territorio>();
	private Jogador dono; // Trocar para private
	private int qntdExercito = 1;
	private String nome; // Trocar para private
	// imagem do territorio

	public Territorio(String nome) {
		this.nome = nome;
	}

	public String getNome() {
		return nome;
	}

	public Jogador getDono() {
		return dono;
	}

	public void trocaDono(Jogador j, int qtdExe) {
		dono = j;
		qntdExercito = qtdExe;
	}

	public void acrescentaExe(int qtd) {
		qntdExercito += qtd;
	}

	public void reduzExe(int qtd) {
		qntdExercito -= qtd;
	}

	public static Baralho<Carta> montaBaralho() {
		Baralho<Carta> mapa = new Baralho<Carta>();

		for (Carta carta : cartasTerritorio) {
			mapa.adiciona(carta);
		}

		return mapa;
	}

	static {
		String txtCont = """
				    África,3
				    América do Norte,5
				    América do Sul,2
				    Ásia,7
				    Europa,5
				    Oceania,2
				""";

		String txtTerritorios = """
				Brasil,América do Sul,triangulo,Argentina,Peru,Venezuela,Nigéria
				Argentina,América do Sul,quadrado,Brasil,Peru
				Peru,América do Sul,triangulo,Brasil,Argentina,Venezuela
				Venezuela,América do Sul,triangulo,Peru,México,Brasil
				México,América do Norte,quadrado,Venezuela
				Nigéria,África,circulo,Brasil
				""";

		// Brasil,Argentina,Peru,Venezuela,Nigéria,México

		String nome;
		Territorio territorio;

		String[] linhasTerritorios = txtTerritorios.split("\n");
		String[] linhasCont = txtCont.split("\n");
		String[] strListTemp;

		String nomeCont;
		String nomeSimb;
		Simbolos simb = null;
		Territorio vizinho;

		int qtdExe;

		// Cria os continentes e adiciona à hashtable de continentes
		for (String linha : linhasCont) {
			linha = linha.trim();
			strListTemp = linha.split(",");
			if (strListTemp.length == 2) {
				nomeCont = strListTemp[0];
				qtdExe = Integer.parseInt(strListTemp[1]);
				new Continente(nomeCont, qtdExe);
			}
		}

		// Cria os territórios
		for (String linha : linhasTerritorios) {
			linha = linha.trim();
			strListTemp = linha.split(",");
			if (strListTemp.length > 3) { // Verifica que esta lendo uma linha com território
				nome = strListTemp[0];
				nomeCont = strListTemp[1];
				nomeSimb = strListTemp[2];
				territorio = new Territorio(nome);
				territorios.put(nome, territorio);
				Continente.getContinente(nomeCont).addTerritorio(territorio);

				if (nomeSimb.equals("triangulo"))
					simb = Simbolos.TRIANGULO;
				else if (nomeSimb.equals("quadrado"))
					simb = Simbolos.QUADRADO;
				else if (nomeSimb.equals("circulo"))
					simb = Simbolos.CIRCULO;

				cartasTerritorio.add(new Carta(territorio, simb));

			}
		}

		for (String linha : linhasTerritorios) {
			linha = linha.trim();
			strListTemp = linha.split(",");
			if (strListTemp.length > 3) {
				nome = strListTemp[0];
				territorio = territorios.get(nome);
				for (int i = 3; i < strListTemp.length; i++) {
					vizinho = territorios.get(strListTemp[i]);
					territorio.vizinhos.add(vizinho);
				}
			}
		}
	}
}
