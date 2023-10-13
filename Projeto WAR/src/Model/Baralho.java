package Model;
import java.util.ArrayList;
import java.util.Collections;

public class Baralho <Tipo> {
	private ArrayList <Tipo> cartas = new ArrayList<Tipo>();
	
	public void adiciona(Tipo carta) {
		cartas.add(carta);
	}
	
	public boolean vazio() {
		return cartas.isEmpty();
	}
	
    public Tipo retira() {
    	if (!cartas.isEmpty())
		    return cartas.remove(cartas.size() - 1);
    	else
		    return null;
    }
    
    public void embaralha() {
        Collections.shuffle(cartas);
    }
}
