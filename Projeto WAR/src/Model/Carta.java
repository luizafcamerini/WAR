package Model;

class Carta {
	
	private Territorio ter;
	private Simbolos simb;

	public Carta(Territorio t, Simbolos s) {
		ter = t;
		simb = s;
	}

	public Territorio getTerritorio() {
		/** Funcao retorna o territorio da carta. */
		return ter;
	}

	public Simbolos getSimbolo() {
		return simb;
	}
}
