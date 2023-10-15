package Model;

class Carta {
	
	private Territorio ter;
	private Simbolos simb;

	public Carta(Territorio t, Simbolos s) {
		/** Construtor que cria uma carta com seu territorio e simbolo. */
		ter = t;
		simb = s;
	}

	public Territorio getTerritorio() {
		/** Funcao retorna o territorio de uma carta. */
		return ter;
	}

	public Simbolos getSimbolo() {
		/** Funcao que retorna o simbolo de uma carta. */
		return simb;
	}
}
