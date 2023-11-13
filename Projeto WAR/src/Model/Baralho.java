package Model;

import java.util.ArrayList;
import java.util.Collections;


class Baralho<Tipo> {
	private ArrayList<Tipo> cartas = new ArrayList<Tipo>();

	public void adiciona(Tipo carta) {
		/** Funcao que adiciona uma carta no baralho. */
		cartas.add(carta);
	}

	public boolean vazio() {
		/** Funcao que verifica se o baralho esta vazio. */
		return cartas.isEmpty();
	}

	public Tipo retira() {
		/** Funcao que retira uma carta do baralho. */
		if (!cartas.isEmpty())
			return cartas.remove(cartas.size() - 1);
		else
			return null;
	}

	public Tipo retira(int i) {
		/** Funcao que retira uma carta do baralho em uma posição específica. */
		if (!cartas.isEmpty())
			return cartas.remove(i);
		else
			return null;
	}

	public void embaralha() {
		/** Funcao que embaralha as cartas do baralho. */
		Collections.shuffle(cartas);
	}
	public ArrayList<Tipo> array(){
		return cartas;
	}

}
