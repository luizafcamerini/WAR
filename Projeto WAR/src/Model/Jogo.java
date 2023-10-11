package Model;
import java.util.Random;
import java.util.ArrayList;

abstract class Jogo {
    static ArrayList <Jogador> jogadores = new ArrayList<Jogador>();
    
    public void rodada(){
        /** Funcao que estabelece o jogador atual da rodada e circula dentre os jogadores */
        int i = escolheJogador(jogadores.size());
        while (true){
            int jogador_atual = i % jogadores.size();
            //... o jogo fica rolando por aqui, ate alguem ganhar -> break;
            i++;
        }
    }

    private int escolheJogador(int tamLista){
        /** Funcao que escolhe um indice aleatorio na lista de jogadores. */
        Random rand = new Random();
        int i = rand.nextInt(tamLista); //escolhe entre 0 e tamLista-1
        return 0;
    }

    private void distribuiCartas(){
        /** Funcao que distribui as cartas para os jogadores e preenche o mapa com os exercitos */
    }
}
