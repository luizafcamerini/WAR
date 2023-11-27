package Model;

class Objetivo6 extends Objetivo {
	Jogador alvo;

	protected Objetivo6() {
		descricao = "Destruir todos os exércitos VERDES.\nSe você é quem possui os exércitos VERDES ou se esses exércitos já foram destruídos por outro jogador,\no seu objetivo passa a ser conquistar 24 territórios a sua escolha.";
		imgName = "war_carta_objetivo6.png";
	}

	public void setJogadorAlvo(Jogador _alvo) {
		alvo = _alvo;
	}

	public boolean verifica() {
		Jogador kAlvo = alvo.getAssassino();
		if (alvo == dono) {
			if (dono.getQtdTerritorios() >= 24)
				return true;
			return false;
		}
		if (kAlvo == null)
			return false;
		else if (kAlvo == dono)
			return true;
		else if (dono.getQtdTerritorios() >= 24)
			return true;
		return false;
	}
}
