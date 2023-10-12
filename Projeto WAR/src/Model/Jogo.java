package Model;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;

class Jogo {
    static ArrayList <Jogador> jogadores = new ArrayList<Jogador>();
    Mapa mapa = new Mapa();
    
    public void rodada(){
        /** Funcao que estabelece o jogador atual da rodada e circula dentre os jogadores */
        int i = escolheJogador(jogadores.size());
        System.out.println("O jogador " + jogadores.get(i).getNome() + " começa distribuindo as cartas.");
        distribuiCartas(i); //distribui as cartas do jogo a partir do jogador escolhido
        while (true){
            int jogador_atual = i % jogadores.size();
            //... o jogo fica rolando por aqui, ate alguem ganhar -> break;
            i++;
            break;
        }
    }

    private int escolheJogador(int tamLista){
        /** Funcao que escolhe um indice aleatorio na lista de jogadores. */
        Random rand = new Random();
        int i = rand.nextInt(tamLista); //escolhe entre 0 e tamLista-1
        return i; // pelo q entendi retorna o índice do jogador que vai começar distribuindo as cartas.
    }

    private void distribuiCartas(int jogador){
        /** Funcao que distribui as cartas e preenche o mapa com os exercitos. Recebe o indice da lista de jogadores. */
        Collections.shuffle(mapa.paises); //embaralhar os paises para distribuir
        //^^^ o metodo shuffle substitui a lista por uma embaralhada
        
        for (int i = 0; i< mapa.paises.size(); i++){
            jogadores.get(i%jogadores.size()).addPais(mapa.paises.get(i));
            mapa.paises.get(i).dono = jogadores.get(i%jogadores.size());
        }
    }
}
