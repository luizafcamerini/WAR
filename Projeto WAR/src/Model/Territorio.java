package Model;
import java.util.ArrayList;

class Territorio {
    public ArrayList<Territorio> vizinhos = new ArrayList<Territorio>();
    public Jogador dono;
    public int qntdExercito;
    public String nome;
    //imagem do territorio

    public Territorio(String nome){
        this.nome = nome;
    }

    public void acrescentaExe(){
        this.qntdExercito++;
    }
}
