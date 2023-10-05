package Model;
import java.util.ArrayList;
abstract class Territorio {
    public ArrayList<Territorio> adjacentes; //precisa ser arraylist pra ter metodos tipo add
    public void addAdj(Territorio adj){
        adjacentes.add(adj);
    }
}
