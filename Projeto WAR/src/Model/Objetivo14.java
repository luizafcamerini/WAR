package Model;

class Objetivo14 extends Objetivo {
	public Objetivo14() {
		descricao = "Conquistar 18 territorios com pelo menos 2 exercitos em cada";
	}

	public boolean verifica() {
		int count = 0;
		for(Territorio t: dono.getTerritorios()) {
			if (t.getQntdExercitos() >= 2)
				count++;
		}
		if(count >= 18)
			return true;
		return false;
	}
}
