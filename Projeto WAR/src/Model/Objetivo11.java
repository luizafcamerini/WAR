package Model;

class Objetivo11 extends Objetivo {

	protected Objetivo11() {
		descricao = "Conquistar na totalidade a América do Norte e a Oceania";
		imgName = "war_carta_objetivo11.png";
	}

	public boolean verifica() {
		if (Continente.getContinente("América do Norte").pertence(dono) &&
				Continente.getContinente("Oceania").pertence(dono))
			return true;
		return false;
	}
}
