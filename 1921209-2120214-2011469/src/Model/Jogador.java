package Model;

import java.util.ArrayList;

class Jogador {
	private ArrayList<Territorio> territorios = new ArrayList<Territorio>();
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
		/** Metodo que retorna uma copia da lista de cartas de um jogador. */
		return cartas.toArray(new Carta[cartas.size()]);
	}

	public String getNome() {
		/** Metodo que retorna o nome do jogador. */
		return nome;
	}

	public Cores getCor() {
		/** Metodo que retorna a cor do jogador. */
		return cor;
	}

	public Jogador getAssassino() {
		/** Metodo que retorna o assassino do jogador. */
		return assassino;
	}

	public void setAssassino(Jogador assassino) {
		/**
		 * Metodo que define o assassino de um jogador e entrega as cartas do morto para
		 * o assassino.
		 */
		this.assassino = assassino;
		// Assassino pega as cartas do morto
		if (cartas.size() > 0) {
			ModelAPI.getInstance().entregaCartaAssassino(this);
		}
	}

	public void setObjetivo(Objetivo o) {
		/** Metodo que define o objetivo de um jogador. */
		objetivo = o;
		o.defineDono(this);
	}

	public String getDescricaoObjetivo() {
		/** Metodo que retorna a descricao de um objetivo. */
		return objetivo.getDescricao();
	}

	public String getImgNameObjetivo() {
		/** Metodo que retorna o nome da imagem do objetivo de um jogador. */
		return objetivo.getImgName();
	}

	public boolean verificaObjetivo() {
		/** Metodo que verifica o objetivo do jogador. */
		return objetivo.verifica();
	}

	public Territorio[] getTerritorios() {
		/** Metodo que retorna uma copia da lista de territorios de um jogador. */
		return territorios.toArray(new Territorio[territorios.size()]);
	}

	public int getQtdTerritorios() {
		/** Metodo que retorna a quantidade de territorios de um jogador. */
		return this.territorios.size();
	}

	public void removeTerritorio(Territorio t) {
		/** Metodo que remove um pais pertencente ao jogador. */
		territorios.remove(t);
	}

	public void addTerritorio(Territorio pais) {
		/** Metodo que adiciona um territorio na lista de um jogador */
		territorios.add(pais);
	}

	public void addTerritorio(Territorio pais, int numExe) {
		/**
		 * Metodo que adiciona um territorio na lista de um jogador e quantos exercitos
		 * posicionar nele.
		 */
		territorios.add(pais);
		pais.acrescentaExe(numExe);
	}

	public int getExeAd() {
		/**
		 * Metodo que retorna o numero de exercitos adicionais em relacao a quantidade
		 * de territorios do jogador
		 */
		int tam = this.territorios.size();
		return tam > 6 ? tam / 2 : 3;
	}

	public void posicionaExe(int numExeAd) {
		/**
		 * Metodo que posiciona os exercitos gerais e das cartas sequencialmente nos
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
		/** Metodo que inclui uma carta no jogador. */
		cartas.add(carta);
	}

	public Carta removeCarta(int index) {
		/** Metodo que retorna a carta removida das cartas do jogador. */
		return cartas.remove(index);
	}

}
