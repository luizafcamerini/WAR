package Model;
import java.util.ArrayList;

abstract class Jogador {
    enum Cores{ AZUL, VERMELHO, VERDE, BRANCO, PRETO, AMARELO }; //cores disponiveis
    private ArrayList <Territorio> paises;
    private Objetivos objetivo;
    private int cor;
    //vai ter cartas de troca?

    public Jogador(int cor, ArrayList <Territorio> paisesIniciais, Objetivos obj){
        this.cor = cor;
        this.paises = paisesIniciais;
        this.objetivo = obj;
    }

    //acoes do jogador:
    //atacar
    //defender
    //mover as pecas (territorio origem -> territorio destino)

    //efeitos sobre o jogador:
    //perde um territorio => nao tem mais territorio, perde o jogo
    //perde um exercito
}
