package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import Observer.ObservadoIF;
import Observer.ObservadorIF;

class Territorio implements ObservadoIF {
	private static ArrayList<Carta> cartasTerritorio = new ArrayList<Carta>();
	private static Hashtable<String, Territorio> territorios = new Hashtable<String, Territorio>();

	private ArrayList<Territorio> vizinhos = new ArrayList<Territorio>();
	private Jogador dono;
	private int qntdExercito = 1;
	private String nome;
	private List<ObservadorIF> lst = new ArrayList<ObservadorIF>();

	public Territorio(String nome) {
		/** Construtor que cria um territorio com seu nome. */
		this.nome = nome;
	}

	public void addObservador(ObservadorIF o) {
		lst.add(o);
	}

	public void removeObservador(ObservadorIF o) {
		lst.remove(o);
	}

	public int get(int i) {
		if (i == 1) {
			return qntdExercito;
		}

		else if (i == 2) {
			return ModelAPI.getInstance().color2int(dono.getCor());
		}

		return 0;
	}

	private void notificaObservadores() {
		for (ObservadorIF o : lst) {
			o.notify(this);
		}
	}

	public String getNome() {
		/** Metodo que retorna o nome do territorio. */
		return this.nome;
	}

	public Jogador getDono() {
		/** Metodo que retorna o dono do territorio. */
		return this.dono;
	}

	public void setDono(Jogador j) {
		/** Metodo que define o dono do territorio. */
		this.dono = j;
	}

	public void setQtdExercitos(int qtd) {
		/** Metodo que define a quantidade de exercitos no territorio. */
		this.qntdExercito = qtd;
		notificaObservadores();
	}

	public int getQntdExercitos() {
		/** Metodo que retorna a quantidade de exercito no territorio. */
		return this.qntdExercito;
	}

	public void trocaDono(Jogador j) {
		/**
		 * Metodo que troca o dono do territorio e a quantidade de exercitos do novo
		 * dono.
		 */
		if (j == null) {
			dono = null;
			qntdExercito = 1;
			return;
		}
		Jogador donoAnterior = dono;
		if (dono != null) {
			donoAnterior.removeTerritorio(this);
			if (donoAnterior.getQtdTerritorios() == 0)
				donoAnterior.setAssassino(j);
			// tranferir as cartas do morto para o assassino
		}
		dono = j;
		j.addTerritorio(this);
		qntdExercito = 1;

		notificaObservadores();
	}

	public void acrescentaExe(int qtd) {
		/** Metodo que acrescenta uma quantidade de exercitos em um territorio. */
		qntdExercito += qtd;
		notificaObservadores();
	}

	public void reduzExe(int qtd) {
		/** Metodo que decrescenta uma quantidade de exercitos em um territorio. */
		qntdExercito -= qtd;
		notificaObservadores();
	}

	public Territorio[] getVizinhos() {
		/** Metodo que retorna uma copia da lista de vizinhos de um territorio. */
		return vizinhos.toArray(new Territorio[vizinhos.size()]);
	}

	private void orndenaDecrescente(int[] lista) {
		Arrays.sort(lista); // Ordena em ordem crescente
		for (int i = 0; i < lista.length / 2; i++) { // Inverte a lista
			int temp = lista[i];
			lista[i] = lista[lista.length - i - 1];
			lista[lista.length - i - 1] = temp;
		}
	}

	private void sorteiaDados(int[] lista) {
		/**
		 * Metodo que preenche uma lista de inteiros com inteiros aleatorios, de 1 a 6
		 */
		for (int i = 0; i < lista.length; i++) {
			Random rand = new Random();
			lista[i] = rand.nextInt(1, 7);
		}

	}

	private boolean verificarVizinhos(Territorio t) {
		/** Metodo que verifica se dois territorios sao vizinhos */
		return vizinhos.contains(t);
	}

	public boolean verificaCondicoesAtaque(Territorio alvo) {
		/** Metodo que verifica as condicoes de ataque dado um territorio alvo. */
		return (this.verificarVizinhos(alvo)) &&
				(this.dono != alvo.getDono()) &&
				(this.qntdExercito > 1);
	}

	public void atacar(Territorio alvo, int[][] dados) {
		/** Metodo em que um territorio ataca outro e faz com que o outro se defenda */
		int dadosAtaque[] = dados[0];
		int dadosDefesa[] = dados[1];

		orndenaDecrescente(dadosAtaque);
		orndenaDecrescente(dadosDefesa);

		// Comparação dos valores dos dados
		int minTam = dadosAtaque.length > dadosDefesa.length ? dadosDefesa.length : dadosAtaque.length;
		for (int i = 0; i < minTam; i++) {
			if (dadosAtaque[i] > dadosDefesa[i]) {
				alvo.reduzExe(1);
			} else {
				this.reduzExe(1);
			}
		}

		// Verifica se ocorreu conquista de território
		if (alvo.qntdExercito == 0) {
			alvo.trocaDono(this.dono);
			this.reduzExe(1);
		}
	}

	public int[][] atacar(Territorio alvo) {
		/**
		 * Metodo em que um territorio ataca outro e faz com que o outro se defenda.
		 * Retorna uma matriz de dados.
		 */
		int dadosAtaque[];
		int dadosDefesa[];
		int listasDados[][] = new int[2][];
		boolean condicoesAtaque = verificaCondicoesAtaque(alvo);

		if (condicoesAtaque) {

			// Cria e sorteia dados de ataque
			if (this.qntdExercito > 3) {
				dadosAtaque = new int[3];
			} else {
				dadosAtaque = new int[this.qntdExercito - 1];
			}
			sorteiaDados(dadosAtaque);
			listasDados[0] = dadosAtaque;

			// Cria e sorteia dados de defesa
			if (alvo.qntdExercito > 3) {
				dadosDefesa = new int[3];
			} else {
				dadosDefesa = new int[alvo.qntdExercito];
			}
			sorteiaDados(dadosDefesa);
			listasDados[1] = dadosDefesa;

			this.atacar(alvo, listasDados);
		} else {
			System.out.printf("Não foi possível atacar %s\n.", alvo.getNome());
			return null;
		}

		return listasDados;
	}

	public static Baralho<Carta> montaBaralho() {
		/** Metodo que cria um baralho de cartas de territorios */
		Baralho<Carta> mapa = new Baralho<Carta>();
		for (Carta carta : cartasTerritorio) {
			mapa.adiciona(carta);
		}
		return mapa;
	}

	public static Territorio getTerritorio(String nome) {
		/** Metodo que retorna um território pelo nome */
		return territorios.get(nome);
	}

	public static Territorio[] getTerritorios() {
		/** Metodo que retorna todos os territorios */
		return territorios.values().toArray(new Territorio[territorios.size()]);
	}

	public static Carta getCarta(String nome) {
		for (int i = 0; i < cartasTerritorio.size(); i++) {
			Carta c = cartasTerritorio.get(i);
			if (c.getTerritorio().getNome().equals(nome))
				return c;
		}
		return null;
	}

	static {
		/*
		 * Bloco estatico que cria os continentes e territorios a partir de uma string e
		 * de uma hashtable.
		 */
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

				Alasca,América do Norte,triangulo,Calgary,Vancouver,Sibéria
				Calgary,América do Norte,circulo,Alasca,Groelândia,Vancouver
				Califórnia,América do Norte,quadrado,Vancouver,Texas,México
				Groelândia,América do Norte,circulo,Calgary,Québec,Reino Unido
				México,América do Norte,quadrado,Califórnia,Texas,Venezuela
				Nova York,América do Norte,quadrado,Québec,Texas
				Québec,América do Norte,circulo,Groelândia,Vancouver,Texas,Nova York
				Texas,América do Norte,triangulo,Vancouver,Nova York,Québec,México,Califórnia
				Vancouver,América do Norte,triangulo,Calgary,Alasca,Califórnia,Québec,Texas

				África do Sul,África,triangulo,Somalia,Angola
				Angola,África,quadrado,África do Sul,Somalia,Nigéria
				Argélia,África,circulo,Egito,Nigéria,Espanha,Itália
				Egito,África,triangulo,Argélia,Nigéria,Somalia,Romênia,Jordânia
				Nigéria,África,circulo,Angola,Somalia,Egito,Argélia,Brasil
				Somalia,África,quadrado,África do Sul,Angola,Egito,Nigéria,Arábia Saudita

				Arábia Saudita,Ásia,circulo,Jordânia,Iraque,Somalia
				Bangladesh,Ásia,circulo,Índia,Coréia do Sul,Tailândia,Indonésia
				Cazaquistão,Ásia,circulo,Sibéria,Rússia,Letônia,Turquia,China,Mongolia,Japão
				China,Ásia,quadrado,Cazaquistão,Mongolia,Coréia do Norte,Coréia do Sul,Índia,Paquistão,Turquia
				Coréia do Norte,Ásia,quadrado,Coréia do Sul,China,Japão
				Coréia do Sul,Ásia,triangulo,China,Índia,Coréia do Norte,Tailândia,Bangladesh
				Estônia,Ásia,circulo,Suécia,Letônia,Rússia
				Índia,Ásia,triangulo,Paquistão,China,Coréia do Sul,Bangladesh,Indonésia
				Irã,Ásia,quadrado,Iraque,Síria,Paquistão
				Iraque,Ásia,triangulo,Arábia Saudita,Jordânia,Síria,Irã
				Japão,Ásia,circulo,Coréia do Norte,Mongolia,Cazaquistão
				Jordânia,Ásia,quadrado,Arábia Saudita,Iraque,Síria,Egito
				Letônia,Ásia,quadrado,Estônia,Rússia,Cazaquistão,Polônia,Ucrânia,Suécia,Turquia
				Mongolia,Ásia,triangulo,Cazaquistão,China,Japão
				Paquistão,Ásia,circulo,Turquia,Síria,Irã,China,Índia
				Rússia,Ásia,triangulo,Estônia,Letônia,Cazaquistão,Sibéria
				Sibéria,Ásia,quadrado,Alasca,Rússia,Cazaquistão
				Síria,Ásia,quadrado,Jordânia,Iraque,Irã,Paquistão,Turquia
				Tailândia,Ásia,triangulo,Bangladesh,Coréia do Sul
				Turquia,Ásia,triangulo,Letônia,Cazaquistão,China,Paquistão,Síria,Ucrânia

				Espanha,Europa,circulo,França,Argélia
				França,Europa,triangulo,Espanha,Reino Unido,Itália,Suécia
				Itália,Europa,quadrado,França,Polônia,Romênia,Argélia,Suécia
				Polônia,Europa,triangulo,Itália,Letônia,Romênia,Ucrânia
				Reino Unido,Europa,circulo,França,Groelândia
				Romênia,Europa,triangulo,Itália,Polônia,Ucrânia,Egito
				Suécia,Europa,quadrado,Estônia,Letônia,França,Itália
				Ucrânia,Europa,circulo,Polônia,Letônia,Turquia,Romênia

				Austrália,Oceania,triangulo,Perth,Nova Zelândia,Indonésia
				Indonésia,Oceania,triangulo,Índia,Bangladesh,Austrália,Nova Zelândia
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
