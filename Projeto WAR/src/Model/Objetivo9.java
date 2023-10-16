package Model;

class Objetivo9 extends Objetivo {
	public Objetivo9() {
		descricao = "Conquistar na totalidade Asia e America do Sul";
	}

	public boolean verifica() {
		if (Continente.getContinente("Ásia").pertence(dono) &&
				Continente.getContinente("América do Sul").pertence(dono))
			return true;
		return false;
	}
}
