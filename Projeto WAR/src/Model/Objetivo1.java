package Model;


public class Objetivo1 implements Objetivo{
    String descricao = "Destruir todos os exércitos AZUIS";
    public boolean verifica(){
        // se voce e quem possui os exércitos <X> ou se esses exercitos já foram destruidos por outro jogador,
        // o seu objetivo passa a ser conquistar 24 territorios a sua escolha
        return true;
    }

}
