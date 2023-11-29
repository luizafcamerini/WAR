package Model;

class Objetivo7 extends Objetivo {

	protected Objetivo7() {
		descricao = "Conquistar 24 territórios a sua escolha.";
		imgName = "war_carta_objetivo7.png";
	}

	public boolean verifica() {
		return dono.getQtdTerritorios() >= 24;
	}
}
