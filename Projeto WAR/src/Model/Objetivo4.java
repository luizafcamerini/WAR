package Model;

class Objetivo4 extends Objetivo {
	Jogador alvo;

	// public Objetivo4(Jogador _alvo) {
	// 	alvo = _alvo;
	// 	descricao = "Destruir todos os exércitos PRETOS";
	// 	imgName = "war_carta_objetivo4.png";
	// }

	protected Objetivo4(){
		descricao = "Destruir todos os exércitos PRETOS. Se você é quem possui os exércitos PRETOS ou se esses exércitos já foram destruídos por outro jogador, o seu objetivo passa a ser conquistar 24 territorios a sua escolha.";
		imgName = "war_carta_objetivo4.png";
	}

	public void setJogadorAlvo(Jogador _alvo){
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
		// se voce e quem possui os exércitos <X> ou se esses exercitos já foram
		// destruidos por outro jogador,
		// o seu objetivo passa a ser conquistar 24 territorios a sua escolha
	}
}
