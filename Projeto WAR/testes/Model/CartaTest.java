package Model;
import static org.junit.Assert.*;
import org.junit.Test;

public class CartaTest {

    @Test
    public void testGetTerritorio(){
        Territorio t = new Territorio("Brasil");
        Carta c = new Carta(t, Simbolos.CORINGA);
        assertEquals(t, c.getTerritorio());
    }

    @Test
    public void testGetTerritorioNulo(){
        Carta c = new Carta(null, Simbolos.CORINGA);
        assertEquals(null, c.getTerritorio());
    }

    @Test
    public void testGetSimbolo(){
        Territorio t = new Territorio("Brasil");
        Carta c = new Carta(t, Simbolos.CORINGA);
        assertEquals(Simbolos.CORINGA, c.getSimbolo());
    }

    @Test
    public void testGetSimboloNulo(){
        Territorio t = new Territorio("Brasil");
        Carta c = new Carta(t, null);
        assertEquals(null, c.getSimbolo());
    }
    
}
