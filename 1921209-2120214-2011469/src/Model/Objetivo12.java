package Model;

class Objetivo12 extends Objetivo {

	protected Objetivo12() {
		descricao = "Conquistar na totalidade a Europa, a Oceania e mais um continente a sua escolha.";
		imgName = "war_carta_objetivo12.png";
	}

	public boolean verifica() {
		if (Continente.getContinente("Europa").pertence(dono) &&
				Continente.getContinente("Oceania").pertence(dono)) {
			for (Continente c : Continente.getContinentes()) {
				if (!c.getNome().equals("Europa") &&
						!c.getNome().equals("Oceania") &&
						c.pertence(dono))
					return true;
			}
		}
		return false;
	}
}
