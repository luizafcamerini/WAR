package Model;
import java.util.ArrayList;

abstract class Jogador {
    enum Cores{ AZUL, VERMELHO, VERDE, BRANCO, PRETO }; //cores disponiveis
    private ArrayList <Territorio> paises;
    private int cor; //cor do jogador
    public int qtdExe;
    Territorio listaPAises[];

    public Jogador(int cor, int qtdExe){
        this.cor = cor;
        this.qtdExe = qtdExe;
    }
    
    public int getCor(){
        return this.cor;
    }

    public void addPais(Territorio pais){
        paises.add(pais);
    }

    public void atacar(Territorio paisOri, Territorio paisDest){

    }
    
}
