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
		Carta.Simbolo simb = null;
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
					simb = Carta.Simbolo.TRIANGULO;
				else if (nomeSimb.equals("quadrado"))
					simb = Carta.Simbolo.QUADRADO;
				else if (nomeSimb.equals("circulo"))
					simb = Carta.Simbolo.CIRCULO;

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

		/*
		 * 
		 * for (String linha: linhasTerritorios){
		 * 
		 * linha = linha.trim();
		 * strListTemp = linha.split(",");
		 * }
		 * 
		 * 
		 * for(int i = 0; i < strListTemp.length; i++){
		 * territorio.put(nome, territorio);
		 * }
		 * 
		 * }
		 * 
		 * for(linha l: arquivo){
		 * l=l.strip();
		 * lista=l.split(",");
		 * nome = lista[0];
		 * nome = "oi";
		 * 
		 * territorio = new Territorio(nome);
		 * territorio.vizinhos(lista[1]...etc)
		 * territorios.put(nome, territorio);
		 * 
		 * 
		 * 
		 * }
		 * 
		 * 
		 * // Cria instâncias de territórios
		 * Territorio brasil = new Territorio("Brasil");
		 * Territorio argentina = new Territorio("Argentina");
		 * Territorio peru = new Territorio("Peru");
		 * Territorio venezuela = new Territorio("Venezuela");
		 * Territorio mexico = new Territorio("Mexico");
		 * Territorio nigeria = new Territorio("Nigéria");
		 * 
		 * // Cria instâncias de continentes
		 * Continente africa = new Continente("África",3);
		 * Continente america_do_norte = new Continente("América do Norte",5);
		 * Continente america_do_sul = new Continente("América do Sul",2);
		 * Continente asia = new Continente("Ásia",7);
		 * Continente europa = new Continente("Europa",5);
		 * Continente oceania = new Continente("Oceania",2);
		 * 
		 * 
		 * // Adiciona os vizinhos de cada pais
		 * Collections.addAll(brasil.vizinhos, argentina, peru, venezuela, nigeria);
		 * Collections.addAll(argentina.vizinhos, brasil, peru);
		 * Collections.addAll(peru.vizinhos, brasil, argentina, venezuela);
		 * Collections.addAll(venezuela.vizinhos, brasil, peru, mexico);
		 * Collections.addAll(mexico.vizinhos, venezuela);
		 * Collections.addAll(nigeria.vizinhos, brasil);
		 * 
		 * // Adiciona as cartas no baralho
		 * cartasTerritorio.add(new Carta(brasil,Carta.Simbolo.CIRCULO));
		 * cartasTerritorio.add(new Carta(argentina,Carta.Simbolo.QUADRADO));
		 * cartasTerritorio.add(new Carta(peru,Carta.Simbolo.TRIANGULO));
		 * cartasTerritorio.add(new Carta(venezuela,Carta.Simbolo.TRIANGULO));
		 * cartasTerritorio.add(new Carta(mexico,Carta.Simbolo.QUADRADO));
		 * cartasTerritorio.add(new Carta(nigeria,Carta.Simbolo.CIRCULO));
		 * 
		 * // Adiciona territórios aos continentes
		 * africa.addTerritorio(nigeria);
		 * 
		 * 
		 * // Metodo que adiciona os continentes e seus paises
		 * 
		 * Continente.addContinente(africa);
		 * Continente.addContinente(america_do_norte);
		 * Continente.addContinente(america_do_sul);
		 * Continente.addContinente(asia);
		 * Continente.addContinente(europa);
		 * Continente.addContinente(oceania);
		 * 
		 * 
		 * 
		 * // Collections.addAll(america_do_sul.getPaises(), brasil, argentina, peru,
		 * venezuela);
		 */
	}

}
