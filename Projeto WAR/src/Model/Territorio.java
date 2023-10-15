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

				Alasca,América do Norte,triangulo,Calgary,Vancouver,Siberia
				Calgary,América do Norte,circulo,Alasca,Groelândia,Vancouver
				California,América do Norte,quadrado,Vancouver,Texas,México
				Groelândia,América do Norte,circulo,Calgary,Québec,Reino Unido
				México,América do Norte,quadrado,Califórnia,Texas,Venezuela
				Nova York,América do Norte,quadrado,Québec,Texas
				Quebec,América do Norte,circulo,Groelândia,Vancouver,Texas,Nova York
				Texas,América do Norte,triangulo,Vancouver,Nova York,Québec,México,Califórnia
				Vancouver,América do Norte,triangulo,Calgary,Alasca,Califórnia,Québec,Texas
				
				Africa do Sul,África,triangulo,Somalia,Angola
				Angola,África,quadrado,Africa do Sul,Somalia,Nigéria
				Argélia,África,circulo,Egito,Nigéria,Espanha,Itália
				Egito,África,triangulo,Argélia,Nigéria,Somalia,Romênia,Jordânia
				Nigéria,África,circulo,Angola,Somalia,Egito,Argélia,Brasil
				Somalia,África,quadrado,Africa do Sul,Angola,Egito,Nigéria,Arábia Saudita

				Arábia Saudita,Ásia,circulo,Jordânia,Iraque,Somalia
				Bangladesh,Ásia,circulo,Índia,Coréia do Sul,Tailândia,Indonésia
				Cazaquistão,Ásia,circulo,Sibéria,Russia,Letônia,Turquia,China,Mongolia,Japão
				China,Ásia,quadrado,Cazaquistão,Mongolia,Coréia do Norte,Coréia do Sul,Índia,Paquistão,Turquia
				Coréia do Norte,Ásia,quadrado,Coréia do Sul,China,Bangladesh,Tailândia
				Coréia do Sul,Ásia,triangulo,China,Índia,Coréia do Norte,Tailândia
				Estônia,Ásia,circulo,Suécia,Letônia,Russia
				Índia,Ásia,triangulo,Paquistão,China,Coréia do Sul,Bangladesh,Indonésia
				Irã,Ásia,quadrado,Iraque,Síria,Paquistão
				Iraque,Ásia,triangulo,Arábia Saudita,Jordânia,Síria,Irã
				Japão,Ásia,circulo,Coréia do Norte,Mongolia,Cazaquistão
				Jordânia,Ásia,quadrado,Arábia Saudita,Iraque,Síria,Egito
				Letônia,Ásia,quadrado,Estônia,Russia,Cazaquistão,Polônia
				Mongolia,Ásia,triangulo,Cazaquistão,China,Japão
				Paquistão,Ásia,circulo,Turquia,Síria,Irã,China,Índia
				Russia,Ásia,triangulo,Estônia,Letônia,Cazaquistão,Sibéria
				Sibéria,Ásia,quadrado,Alasca,Russia,Cazaquistão
				Síria,Ásia,quadrado,Jordânia,Iraque,Irã,Paquistão,Turquia
				Tailândia,Ásia,triangulo,Bangladesh,Coréia do Sul
				Turquia,Ásia,triangulo,Letônia,Cazaquistão,China,Paquistão,Síria,Ucrânia
				
				Espanha,Europa,circulo,França,Argélia
				França,Europa,triangulo,Espanha,Reino Unido,Itália,Suécia
				Itália,Europa,quadrado,França,Polônia,Romênia,Argélia
				Polônia,Europa,triangulo,Itália,Letônia,Romênia,Ucrânia
				Reino Unido,Europa,circulo,França,Groelândia
				Romênia,Europa,triangulo,Itália,Polônia,Ucrânia,Egito
				Suécia,Europa,quadrado,Estônia,Letônia,França,Itália
				Ucrânia,Europa,circulo,Polônia,Letônia,Turquia,Romênia
				
				Austrália,Oceania,triangulo,Perth,
				Indonésia,Oceania,triangulo,Índia,Bangladesh,Australia,Nova Zelânia
				Nova Zelândia,Oceania,quadrado,Austrália,Indonésia
				Perth,Oceania,circulo,Austrália
			
				""";

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
			if (strListTemp.length >= 3) { // Verifica que esta lendo uma linha com território
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
