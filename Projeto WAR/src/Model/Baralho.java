package Model;

import java.util.ArrayList;
import java.util.Collections;

class Baralho<Tipo> {
	private ArrayList<Tipo> cartas = new ArrayList<Tipo>();

	public void adiciona(Tipo carta) {
		/** Metodo que adiciona uma carta no baralho. */
		cartas.add(carta);
	}

	public boolean vazio() {
		/** Metodo que verifica se o baralho esta vazio. */
		return cartas.isEmpty();
	}

	public Tipo retira() {
		/** Metodo que retira uma carta do baralho. */
		if (!cartas.isEmpty())
			return cartas.remove(cartas.size() - 1);
		else
			return null;
	}

	public Tipo retira(int i) {
		/** Metodo que retira uma carta do baralho em uma posição específica. */
		if (!cartas.isEmpty())
			return cartas.remove(i);
		else
			return null;
	}

	public void embaralha() {
		/** Metodo que embaralha as cartas do baralho. */
		Collections.shuffle(cartas);
	}

	public ArrayList<Tipo> array() {
		/** Metodo que retorna a array de cartas do baralho. */
		return cartas;
	}

}
