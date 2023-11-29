package Model;

class Objetivo9 extends Objetivo {

	protected Objetivo9() {
		descricao = "Conquistar na totalidade Ásia e América do Sul.";
		imgName = "war_carta_objetivo9.png";
	}

	public boolean verifica() {
		if (Continente.getContinente("Ásia").pertence(dono) &&
				Continente.getContinente("América do Sul").pertence(dono))
			return true;
		return false;
	}
}
