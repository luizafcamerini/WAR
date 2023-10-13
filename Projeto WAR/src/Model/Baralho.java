package Model;
import java.util.ArrayList;
import java.util.Collections;

public class Baralho {
	private ArrayList <Object> cartas = new ArrayList<>();
	
	public void adiciona(Object carta) {
		cartas.add(carta);
	}
	
	public boolean vazio() {
		return cartas.isEmpty();
	}
	
    public Object retira() {
    	if (!cartas.isEmpty())
		    return cartas.remove(cartas.size() - 1);
    	else
		    return null;
    }
    
    public void embaralha() {
        Collections.shuffle(cartas);
    }
}
