package Model;

class Objetivo6 extends Objetivo{
	Jogador alvo;
    public Objetivo6(Jogador _alvo) {
    	alvo = _alvo;
    	descricao = "Destruir todos os exércitos VERDES";
    }
    public boolean verifica(){
    	Jogador kAlvo = alvo.getAssassino();
    	if (kAlvo == null)
    		return false;
    	else if (kAlvo == dono)
    		return true;
    	else if (dono.getTerritorios().size() >= 24){
    		return true;
    	}
    	return false;
        // se voce e quem possui os exércitos <X> ou se esses exercitos já foram destruidos por outro jogador,
        // o seu objetivo passa a ser conquistar 24 territorios a sua escolha
    }
}
