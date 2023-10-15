package Model;

import java.util.ArrayList;

class Jogador {

	private ArrayList<Territorio> territorios = new ArrayList<Territorio>(); // Trocar para private
	private ArrayList<Carta> cartas = new ArrayList<Carta>(); // Cartas que o jogador possui
	private String nome;
	private Objetivo objetivo;
	private Cores cor;
	private Jogador assassino;
	// vai ter cartas de troca? Vai sim.

	public Jogador(Cores cor, String nome) {
		this.cor = cor;
		this.nome = nome;
		// Jogo.jogadores.add(this); //depois da construcao do jogador,
		// ele ja eh adicionado na lista de jogadores do jogo
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
		/** Funcao que define o assassino de um jogador. */
		this.assassino = assassino;
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

	public boolean verificaObjetivo(){
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

	public Territorio getTerritorio(String nomePais) {
		/** Funcao que retorna um pais pertencente ao jogador. */
		for (int i = 0; i < territorios.size(); i++) {
			if (territorios.get(i).getNome() == nomePais) {
				return territorios.get(i);
			}
		}
		return null;
	}

	public void addTerritorio(Territorio pais, int qtdExe) {
		/** Funcao que adiciona um territorio na lista de um jogador e quantos exercitos posicionar nele. */
		territorios.add(pais);
		pais.trocaDono(this, qtdExe);
	}

	public void posicionaExeCont() {
		/** Funcao que percorre os continentes e verifica se o jogador tem seu dominio total. */
		for(Continente c: Continente.getContinentes()){
			posicionaExeCont(c);
		}

		// Pergunta se o jogador quer trocar as cartas por exercitos caso a condição
		// seja verdadeira (3 cartas do mesmo tipo ou 1 de cada tipo)
		// Se sim, adiciona o valor correspondente ao numero de exercitos pendentes
		// adiciona exercitos de acordo com o numero de territórios / 2
	}

	private void posicionaExeCont(Continente c) {
		/** Funcao que posiciona os exercitos no continente dominado de forma fixa. */
		if (c.pertence(this)) {
			int numExeContinente = c.getNumExeAdicionais();
			int i = 0;
			int tam = c.getTerritorios().length;
			while (i < numExeContinente) {
				int n = 1; // = input();
				int iPais = i % tam; // = input();
				c.getTerritorios()[iPais].acrescentaExe(n);
				i++;
			}
		}
	}
	
	public void posicionaExe(int numExeAd){
		/** Funcao que posiciona os exercitos gerais e das cartas sequencialmente nos terrritorios dominados. */
		int tam = this.territorios.size();
		int numExe = tam > 6 ? tam / 2 : 3;
		numExe += numExeAd;

		for (int i = 0; i<numExe; i++){
			int n = 1; // = input();
			int iPais = i % tam; // = input();
			this.territorios.get(iPais).acrescentaExe(n);
		}
	}

	public void recebeCarta(Carta carta) {
		cartas.add(carta);
	}

	private boolean verificaTroca(Carta c1, Carta c2, Carta c3) {
		/**
		 * Método que verifica se o jogador tem 3 cartas do mesmo tipo ou 1 de cada tipo
		 */
		if (c1.getSimbolo() == Simbolos.CORINGA || c2.getSimbolo() == Simbolos.CORINGA || c2.getSimbolo() == Simbolos.CORINGA){
			return true;
		}
		if (c1.getSimbolo() == c2.getSimbolo() && c1.getSimbolo() == c3.getSimbolo()){
			return true;
		}
		if (c1.getSimbolo() != c2.getSimbolo() && c1.getSimbolo() != c3.getSimbolo() && c2.getSimbolo() != c3.getSimbolo()){
			return true;
		}
		return false;
	}

	public Carta[] trocaCartas(){
		/** Funcao que faz com que o jogador troque cartas ou nao. */
		// No momento, esta função realiza a troca sempre que possível, ser perguntar ao jogador
		
		Carta[] resultado = new Carta[3];
		if (this.cartas.size() >= 3){
			for(int i = 0; i < cartas.size() - 2; i++){
				Carta c1 = cartas.get(i);
				for(int j = i+1; j < cartas.size() - 1; j++){
					Carta c2 = cartas.get(j);
					for(int k = j+1; k < cartas.size(); k++){
						Carta c3 = cartas.get(k);
						if(verificaTroca(c1,c2,c3)){
							resultado[2] = cartas.remove(k);
							resultado[1] = cartas.remove(j);
							resultado[0] = cartas.remove(i);
							return resultado;
						}
					}
				}
			}
		}
		return null;
	}
}
