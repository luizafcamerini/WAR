package Model;

class Objetivo13 extends Objetivo {
	public Objetivo13() {
		descricao = "Conquistar na totalidade a Europa, a America do Sul e mais um continente a sua escolha";
	}

	public boolean verifica() {
		if (Continente.getContinente("Europa").pertence(dono) &&
				Continente.getContinente("América do Sul").pertence(dono)) {
			for(Continente c: Continente.getContinentes()) {
				if (!c.getNome().equals("Europa") &&
						!c.getNome().equals("América do Sul") &&
						c.pertence(dono))
					return true;
			}
		}
		return false;
	}
}
