package Model;

class Objetivo8 extends Objetivo {

	protected Objetivo8() {
		descricao = "Conquistar na totalidade a Ásia e a África.";
		imgName = "war_carta_objetivo8.png";
	}

	public boolean verifica() {
		if (Continente.getContinente("Ásia").pertence(dono) &&
				Continente.getContinente("África").pertence(dono))
			return true;
		return false;
	}
}
