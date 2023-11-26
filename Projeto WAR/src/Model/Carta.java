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

	static public boolean verificaTroca(Carta[] cartas) {
		/**
		 * Funcao que verifica se as 3 cartas sao diferentes.
		 */
		Carta c1 = cartas[0];
		Carta c2 = cartas[1];
		Carta c3 = cartas[2];

		if (c1.getSimbolo() == Simbolos.CORINGA || c2.getSimbolo() == Simbolos.CORINGA
				|| c2.getSimbolo() == Simbolos.CORINGA) {
			return true;
		}
		if (c1.getSimbolo() == c2.getSimbolo() && c1.getSimbolo() == c3.getSimbolo()) {
			return true;
		}
		if (c1.getSimbolo() != c2.getSimbolo() && c1.getSimbolo() != c3.getSimbolo()
				&& c2.getSimbolo() != c3.getSimbolo()) {
			return true;
		}
		return false;
	}
}
