package Model;
import java.util.ArrayList;

class Objetivo7 extends Objetivo{
	
	public Objetivo7() {
		descricao = "Conquistar 24 territorios a sua escolha";
    }
	
    public boolean verifica(){
    	ArrayList<Territorio> paises = dono.getTerritorios();
    	if (paises.size() >= 24)
    		return true;
    	return false;
    }
}
