package Model;

import Model.Jogador.Cores;

public class Main {
    public static void main(String[] args) {
        Jogador jogador0 = new Jogador(Cores.BRANCO, "LUIZA");
        Jogador jogador1 = new Jogador(Cores.VERMELHO, "THOMAS");
        Jogador jogador2 = new Jogador(Cores.VERDE, "JERONIMO");

        Mapa mapa = new Mapa();

        //testando o mapa:
        for (int i =0; i<mapa.paises.size(); i++){
            System.out.printf("\n%s\n",mapa.paises.get(i).nome);
            for (int j = 0; j<mapa.paises.get(i).vizinhos.size();j++){
                System.out.println(mapa.paises.get(i).vizinhos.get(j).nome);
            }
        }
    }
    
}
