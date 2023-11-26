package Model;

import java.util.ArrayList;

class Jogador {

	// Territorios que pertencem ao jogador:
	private ArrayList<Territorio> territorios = new ArrayList<Territorio>();
	// Cartas que o jogador possui:
	private ArrayList<Carta> cartas = new ArrayList<Carta>();
	private String nome;
	private Objetivo objetivo;
	private Cores cor;
	private Jogador assassino;

	public Jogador(Cores cor, String nome) {
		/** Construtor do jogador com sua respectiva cor e nome. */
		this.cor = cor;
		this.nome = nome;
	}

	public Carta[] getCartas() {
		/** Funcao que retorna uma copia da lista de cartas de um jogador. */
		Carta c[] = cartas.toArray(new Carta[cartas.size()]);
		// for (Carta c1: c) {
		// System.out.println(c1.getTerritorio().getNome());
		// }
		// return new ArrayList<Carta>(cartas);
		return c;
	}

	public String getNome() {
		/** Funcao que retorna o nome do jogador. */
		return nome;
	}

	public Cores getCor() {
		/** Funcao que retorna a cor do jogador. */
		return cor;
	}

	public Jogador getAssassino() {
		/** Funcao que retorna o assassino do jogador. */
		return assassino;
	}

	public void setAssassino(Jogador assassino) {
		/** Funcao que define o assassino de um jogador e entrega as cartas do morto para o assassino. */
		this.assassino = assassino;
		// Assassino pega as cartas do morto
		if(cartas.size() > 0){
			ModelAPI.getInstance().entregaCartaAssassino(this);
		}
	}

	public void setObjetivo(Objetivo o) {
		/** Funcao que define o objetivo de um jogador. */
		objetivo = o;
		o.defineDono(this);
	}

	public String getDescricaoObjetivo() {
		/** Funcao que retorna a descricao de um objetivo. */
		return objetivo.getDescricao();
	}

	public String getImgNameObjetivo() {
		/** Funcao que retorna o nome da imagem do objetivo de um jogador. */
		return objetivo.getImgName();
	}

	public boolean verificaObjetivo() {
		/** Funcao que verifica o objetivo do jogador. */
		return objetivo.verifica();
	}

	public Territorio[] getTerritorios() {
		/** Funcao que retorna uma copia da lista de territorios de um jogador. */
		return territorios.toArray(new Territorio[territorios.size()]);
	}

	public int getQtdTerritorios() {
		/** Funcao que retorna a quantidade de territorios de um jogador. */
		return this.territorios.size();
	}

	public void removeTerritorio(Territorio t) {
		/** Funcao que remove um pais pertencente ao jogador. */
		territorios.remove(t);
	}

	public void addTerritorio(Territorio pais) {
		/** Funcao que adiciona um territorio na lista de um jogador */
		territorios.add(pais);
	}

	public void addTerritorio(Territorio pais, int numExe) {
		/**
		 * Funcao que adiciona um territorio na lista de um jogador e quantos exercitos
		 * posicionar nele.
		 */
		territorios.add(pais);
		pais.acrescentaExe(numExe);
	}

	public int getExeAd() {
		/**
		 * Funcao que retorna o numero de exercitos adicionais em relacao a quantidade
		 * de territorios do jogador
		 */
		int tam = this.territorios.size();
		return tam > 6 ? tam / 2 : 3;
	}

	public void posicionaExe(int numExeAd) {
		/**
		 * Funcao que posiciona os exercitos gerais e das cartas sequencialmente nos
		 * terrritorios dominados.
		 */
		int tam = this.territorios.size();
		int numExe = tam > 6 ? tam / 2 : 3;
		numExe += numExeAd;
		for (int i = 0; i < numExe; i++) {
			int n = 1; // = input();
			int iPais = i % tam; // = input();
			this.territorios.get(iPais).acrescentaExe(n);
		}
	}

	public void recebeCarta(Carta carta) {
		/** Funcao que inclui uma carta no jogador. */
		System.out.println("Jogador " + this.nome + " recebeu uma carta " + carta.getSimbolo());
		cartas.add(carta);
	}

	public Carta removeCarta(int index){
		/** Funcao que retorna a carta removida das cartas do jogador. */
		return cartas.remove(index);
	}


}
