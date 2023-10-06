package Model;
import java.util.ArrayList;
abstract class Territorio {
    public ArrayList<Territorio> adjacentes; //precisa ser arraylist pra ter metodos tipo add
    public Jogador dono;
    public int qntdExercito;
    public String nome;
    //imagem do territorio
}
