package Model;
import java.util.ArrayList;
import java.util.Collections;

class Territorio {
    private ArrayList<Territorio> vizinhos = new ArrayList<Territorio>();
    public Jogador dono; // Trocar para private
    private int qntdExercito;
    public String nome; // Trocar para private
    //imagem do territorio

    public Territorio(String nome){
        this.nome = nome;
    }
    
    public void trocaDono(Jogador j, int qtdExe) {
    	dono = j;
    	qntdExercito = qtdExe;
    }
    
    public void acrescentaExe(int qtd){
        qntdExercito += qtd;
    }
    
    public void reduzExe(int qtd){
        qntdExercito -= qtd;
    }
    
    public static Baralho<Carta> montaBaralho(){
    	// Ainda falta o continente
    	Baralho<Carta> mapa = new Baralho<Carta>();
    	/** Funcao que adiciona os paises e seus adjacentes no mapa */
        Territorio BRASIL = new Territorio("BRASIL");
        Territorio ARGENTINA = new Territorio("ARGENTINA");
        Territorio PERU = new Territorio("PERU");
        Territorio VENEZUELA = new Territorio("VENEZUELA");
        Territorio MEXICO = new Territorio("MEXICO");
        Territorio NIGERIA = new Territorio("NIGERIA");
        
        
        Collections.addAll(BRASIL.vizinhos, ARGENTINA, PERU, VENEZUELA, NIGERIA);
        Collections.addAll(ARGENTINA.vizinhos, BRASIL, PERU);
        Collections.addAll(PERU.vizinhos, BRASIL, ARGENTINA, VENEZUELA);
        Collections.addAll(VENEZUELA.vizinhos, BRASIL, PERU, MEXICO);
        Collections.addAll(MEXICO.vizinhos, VENEZUELA);
        Collections.addAll(NIGERIA.vizinhos, BRASIL);
        
        mapa.adiciona(new Carta(BRASIL,Carta.Simbolo.CIRCULO));
        mapa.adiciona(new Carta(ARGENTINA,Carta.Simbolo.QUADRADO));
        mapa.adiciona(new Carta(PERU,Carta.Simbolo.TRIANGULO));
        mapa.adiciona(new Carta(VENEZUELA,Carta.Simbolo.TRIANGULO));
        mapa.adiciona(new Carta(MEXICO,Carta.Simbolo.QUADRADO));
        mapa.adiciona(new Carta(NIGERIA,Carta.Simbolo.CIRCULO));
        
    	return mapa;
    }
    
}
