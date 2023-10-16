package Model;

class Objetivo8 extends Objetivo {
	public Objetivo8() {
		descricao = "Conquistar na totalidade a Asia e a Africa";
	}

	public boolean verifica() {
		if (Continente.getContinente("Ásia").pertence(dono) &&
				Continente.getContinente("África").pertence(dono))
			return true;
		return false;
	}
}
