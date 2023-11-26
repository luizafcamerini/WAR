package Model;

class Objetivo10 extends Objetivo {

	protected Objetivo10() {
		descricao = "Conquistar na totalidade a América do Norte e a África";
		imgName = "war_carta_objetivo10.png";
	}

	public boolean verifica() {
		if (Continente.getContinente("América do Norte").pertence(dono) &&
				Continente.getContinente("África").pertence(dono))
			return true;
		return false;
	}
}
