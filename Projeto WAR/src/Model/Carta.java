package Model;

public class Carta {
	public enum Simbolo{TRIANGULO, QUADRADO, CIRCULO, CORINGA};
	private Territorio ter;
	private Simbolo simb;
	
	public Carta(Territorio t, Simbolo s) {
		ter = t;
		simb = s;
	}
	
	public Territorio getTerritorio() {
		return ter;
	}
	
	public Simbolo getSimbolo(){
		return simb;
	}
}
