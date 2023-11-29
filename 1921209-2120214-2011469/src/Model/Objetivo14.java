package Model;

class Objetivo14 extends Objetivo {

	protected Objetivo14() {
		descricao = "Conquistar 18 territórios com pelo menos 2 exércitos em cada.";
		imgName = "war_carta_objetivo14.png";
	}

	public boolean verifica() {
		int count = 0;
		for (Territorio t : dono.getTerritorios()) {
			if (t.getQntdExercitos() >= 2)
				count++;
		}
		if (count >= 18)
			return true;
		return false;
	}
}
