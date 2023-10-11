package Model;
import java.util.ArrayList;

abstract class Jogador {
    enum Cores{ AZUL, VERMELHO, VERDE, BRANCO, PRETO, AMARELO }; //cores disponiveis
    private ArrayList <Territorio> paises;
    private String nome;
    //private Objetivos objetivo;
    private int cor;
    //vai ter cartas de troca?

    public Jogador(int cor, String nome){
        this.cor = cor;
        this.nome = nome;
        Jogo.jogadores.add(this); //depois da construcao do jogador, ele ja eh adicionado na lista de jogadores do jogo
    }

    //acoes do jogador:
    //atacar
    //defender
    //mover as pecas (territorio origem -> territorio destino)

    //efeitos sobre o jogador:
    //perde um territorio => nao tem mais territorio, perde o jogo
    //perde um exercito
}
