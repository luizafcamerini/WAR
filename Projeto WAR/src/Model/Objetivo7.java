package Model;

class Objetivo7 extends Objetivo {

	public Objetivo7() {
		descricao = "Conquistar 24 territorios a sua escolha";
	}

	public boolean verifica() {
		return dono.getQtdTerritorios() >= 24;
	}
}
