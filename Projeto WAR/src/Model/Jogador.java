package Model;
import java.util.ArrayList;

public class Jogador {
    public enum Cores{ AZUL, VERMELHO, VERDE, BRANCO, PRETO, AMARELO }; //cores disponiveis
    private ArrayList <Territorio> paises;
    private String nome;
    //private Objetivos objetivo;
    private Cores cor;
    //vai ter cartas de troca?

    public Jogador(Cores cor, String nome){
        this.cor = cor;
        this.nome = nome;
        Jogo.jogadores.add(this); //depois da construcao do jogador, 
        //ele ja eh adicionado na lista de jogadores do jogo
    }

    public String getNome(){
        return this.nome;
    }

    public Territorio getPais(String nomePais){
        /** Funcao que retorna um pais pertencente ao jogador. */
        for (int i=0; i<paises.size(); i++){
            if (paises.get(i).nome == nomePais){
                return paises.get(i);
            }
        }
        return null;
    }

    //acoes do jogador:
    //atacar
    //defender
    //mover as pecas (territorio origem -> territorio destino)

    //efeitos sobre o jogador:
    //perde um territorio => nao tem mais territorio, perde o jogo
    //perde um exercito
}
