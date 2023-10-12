package Model;
import java.util.ArrayList;
import java.util.Collections;

class Mapa {
    ArrayList <Territorio> paises = new ArrayList<Territorio>();

    public Mapa(){
        montaMapa();
    }

    private void montaMapa(){
        /** Funcao que adiciona os paises e seus adjacentes no mapa */
        Territorio BRASIL = new Territorio("BRASIL");
        Territorio ARGENTINA = new Territorio("ARGENTINA");
        Territorio COLOMBIA = new Territorio("COLOMBIA");
        Territorio CHILE = new Territorio("CHILE");
        Territorio PERU = new Territorio("PERU");
        Territorio BOLIVIA = new Territorio("BOLIVIA");
        Collections.addAll(BRASIL.vizinhos, ARGENTINA, COLOMBIA);
        Collections.addAll(ARGENTINA.vizinhos, BRASIL, BOLIVIA);
        Collections.addAll(COLOMBIA.vizinhos, BRASIL);
        Collections.addAll(CHILE.vizinhos, ARGENTINA, BOLIVIA);
        Collections.addAll(PERU.vizinhos, BRASIL, BOLIVIA, CHILE);
        Collections.addAll(BOLIVIA.vizinhos, BRASIL, PERU, ARGENTINA);
        paises.add(BRASIL);
        paises.add(ARGENTINA);
        paises.add(COLOMBIA);
        paises.add(CHILE);
        paises.add(PERU);
        paises.add(BOLIVIA);
    }
    
}
