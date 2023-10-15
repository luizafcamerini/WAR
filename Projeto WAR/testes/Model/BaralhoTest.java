package Model;
import static org.junit.Assert.*;
import org.junit.Test;

public class BaralhoTest {
    private Baralho<Carta> baralho = new Baralho<Carta>(); 
    private Territorio territorio1 = new Territorio("Brasil");
    private Baralho<Carta> baralho2 = new Baralho<Carta>(); 

    private Carta carta1 = new Carta(territorio1, Simbolos.CIRCULO);

    @Test
    public void testVazio(){
        assertTrue(baralho2.vazio());
    }

    @Test
    public void testAdiciona(){
        baralho.adiciona(carta1);
        assertFalse(baralho.vazio());
    }

    @Test
    public void testRetira() {
        baralho2.retira();
        assertTrue(baralho2.vazio());
    }
}