package Model;
import java.util.ArrayList;

class Jogador {
    public enum Cores{ AZUL, VERMELHO, VERDE, BRANCO, PRETO, AMARELO }; //cores disponiveis
    public ArrayList <Territorio> paises = new ArrayList<Territorio>(); //Trocar para private
    private String nome;
    private Objetivo objetivo;
    private Cores cor;
    private Jogador assassino;
    //vai ter cartas de troca? Vai sim.

    public Jogador(Cores cor, String nome){
        this.cor = cor;
        this.nome = nome;
//        Jogo.jogadores.add(this); //depois da construcao do jogador, 
        //ele ja eh adicionado na lista de jogadores do jogo
    }

    public String getNome(){
        return nome;
    }
    
    public Cores getCor() {
    	return cor;
    }
    
    public Jogador getAssassino() {
    	return assassino;
    }
    
    public void setObjetivo(Objetivo o) {
    	objetivo = o;
    	o.defineDono(this);
    }

    public ArrayList<Territorio> getTerritorios() {
    	return paises;
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

    public void addPais(Territorio pais, int qtdExe){
        paises.add(pais);
        pais.trocaDono(this, qtdExe);
    }

    //acoes do jogador:
    //atacar
    //defender
    //mover as pecas (territorio origem -> territorio destino)

    //efeitos sobre o jogador:
    //perde um territorio => nao tem mais territorio, perde o jogo
    //perde um exercito
}
