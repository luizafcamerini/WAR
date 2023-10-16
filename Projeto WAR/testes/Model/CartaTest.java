package Model;
import static org.junit.Assert.*;
import org.junit.Test;

public class CartaTest {
    private static final int TIMEOUT = 2000;

    @Test(timeout = TIMEOUT)
    public void testGetTerritorio(){
        Territorio t = new Territorio("Brasil");
        Carta c = new Carta(t, Simbolos.CIRCULO);
        assertEquals(t, c.getTerritorio());
    }

    @Test(timeout = TIMEOUT)
    public void testGetTerritorioNulo(){
        Carta c = new Carta(null, Simbolos.CORINGA);
        assertNull(c.getTerritorio());
    }

    @Test(timeout = TIMEOUT)
    public void testGetSimbolo(){
        Territorio t = new Territorio("Brasil");
        Carta c = new Carta(t, Simbolos.CIRCULO);
        assertEquals(Simbolos.CIRCULO, c.getSimbolo());
    }
    
}
