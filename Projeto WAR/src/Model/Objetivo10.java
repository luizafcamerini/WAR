package Model;

class Objetivo10 extends Objetivo {
	public Objetivo10() {
		descricao = "Conquistar na totalidade a America do Norte e a Africa";
	}

	public boolean verifica() {
		if (Continente.getContinente("América do Norte").pertence(dono) &&
				Continente.getContinente("África").pertence(dono))
			return true;
		return false;
	}
}
