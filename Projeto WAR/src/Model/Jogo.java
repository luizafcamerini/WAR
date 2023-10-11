package Model;

import java.util.ArrayList;

abstract class Jogo {
    static ArrayList <Jogador> jogadores;
    
    public void ordemJogadas(){
        int i = 0;
        while (true){
            int jogador_atual = i % jogadores.size();
            //... o jogo fica rolando por aqui!
            i++;
        }
    }
}
