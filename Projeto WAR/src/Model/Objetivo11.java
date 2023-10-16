package Model;

class Objetivo11 extends Objetivo {
	public Objetivo11() {
		descricao = "Conquistar na totalidade a America do Norte e a Oceania";
	}

	public boolean verifica() {
		if (Continente.getContinente("América do Norte").pertence(dono) &&
				Continente.getContinente("Oceania").pertence(dono))
			return true;
		return false;
	}
}
