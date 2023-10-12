package Model;

import Model.Jogador.Cores;

public class Main {
    public static void main(String[] args) {
        Jogo jogo = new Jogo();
        
        Jogador jogador0 = new Jogador(Cores.BRANCO, "LUIZA");
        Jogador jogador1 = new Jogador(Cores.VERMELHO, "THOMAS");
        Jogador jogador2 = new Jogador(Cores.VERDE, "JERONIMO");
        
        jogo.rodada();

        for (int i =0; i<Jogo.jogadores.size(); i++){
            System.out.printf("\n%s\n",Jogo.jogadores.get(i).getNome());
            for (int j=0; j<Jogo.jogadores.get(i).paises.size();j++){
                System.out.printf("%s: %s\n", Jogo.jogadores.get(i).paises.get(j).nome, Jogo.jogadores.get(i).paises.get(j).dono.getNome());
            }
        }
    }
    
}
