package Model;

public class Carta {
	public enum Simbolo {
		TRIANGULO, QUADRADO, CIRCULO, CORINGA
	};

	private Territorio ter;
	private Simbolo simb;

	public Carta(Territorio t, Simbolo s) {
		ter = t;
		simb = s;
	}

	public Territorio getTerritorio() {
		/** Funcao retorna o territorio da carta. */
		return ter;
	}

	public Simbolo getSimbolo() {
		return simb;
	}
}
