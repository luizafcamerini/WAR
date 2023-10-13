package Model;

import Model.Jogador.Cores;

public class Main {
    public static void main(String[] args) {
        Jogo jogo = new Jogo();
        Jogador jAtual;
        
        jogo.adicionaJogador(new Jogador(Cores.BRANCO, "LUIZA"));
        jogo.adicionaJogador(new Jogador(Cores.VERMELHO, "THOMAS"));
        jogo.adicionaJogador(new Jogador(Cores.VERDE, "JERONIMO"));

        jogo.inicializa();
        
        for (int i =0; i<jogo.getQtdJogadores(); i++){
        	jAtual = jogo.getProxJogador();
            System.out.printf("\n%s\n",jAtual.getNome());
            for (int j=0; j<jAtual.paises.size();j++){
                System.out.printf("%s: %s\n", jAtual.paises.get(j).nome, jAtual.paises.get(j).dono.getNome());
            }
        }
    }
    
}
