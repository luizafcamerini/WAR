package Model;
import static org.junit.Assert.*;
import org.junit.Test;

public class ContinenteTest {
    
    @Test
    public void testgetNumExeAdicionais(){
        Continente continente = new Continente("América do Sul", 2);
        assertEquals(2, continente.getNumExeAdicionais());
    }

    @Test
    public void testAddTerritorio_e_getPaises() {
        Territorio Brasil = new Territorio("Brasil");
        Territorio Argentina = new Territorio("Argentina");
        Continente continente = new Continente("América do Sul", 2);
        continente.addTerritorio(Brasil);
        continente.addTerritorio(Argentina);
        assertEquals(2, continente.getTerritorios().length);
    }

    @Test
    public void testAddTerritorioExistente(){
        Territorio Brasil = new Territorio("Brasil");
        Territorio Argentina = new Territorio("Argentina");
        Continente continente = new Continente("América do Sul", 2);
        continente.addTerritorio(Brasil);
        continente.addTerritorio(Argentina);
        continente.addTerritorio(Argentina);
        assertEquals(2, continente.getTerritorios().length);
    }

    
    @Test 
    public void testPertence(){
        Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
        Territorio Brasil = new Territorio("Brasil");
        Territorio Argentina = new Territorio("Argentina");
        Territorio Peru = new Territorio("Peru");
        Territorio Venezuela = new Territorio("Venezuela");
        Continente continente = new Continente("América do Sul", 2);
        continente.addTerritorio(Brasil);
        continente.addTerritorio(Argentina);
        continente.addTerritorio(Peru);
        continente.addTerritorio(Venezuela);
        jogador.addTerritorio(Brasil, 1);
        jogador.addTerritorio(Argentina, 1);
        jogador.addTerritorio(Peru, 1);
        jogador.addTerritorio(Venezuela, 1);
        assertTrue(continente.pertence(jogador));
    }
    
    @Test
    public void testGetContinente(){
        Continente continente = new Continente("Ásia", 7);
        assertEquals(continente, Continente.getContinente("Ásia"));
    }
}