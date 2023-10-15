package Model;
import static org.junit.Assert.*;
import org.junit.Test;

public class JogadorTest {
    private Jogador jogador;
    private Objetivo objetivo;
    private Territorio territorio1;
    private Territorio territorio2;
    private Continente continente;
    
    @Test
    public void testSetAssassino_e_getAssassino() {
        Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
        Jogador assassino = new Jogador(Cores.VERDE, "Jeronimo");
        jogador.setAssassino(assassino);
        assertEquals(assassino, jogador.getAssassino());
    }

    
    @Test
    public void testSetObjetivo_e_getDescricaoObjetivo() {
        Objetivo objetivo = new Objetivo7();
        jogador = new Jogador(Cores.VERMELHO, "Thomas");
        jogador.setObjetivo(objetivo);
        assertEquals("Conquistar 24 territorios a sua escolha", jogador.getDescricaoObjetivo());
    }
    
    
    @Test
    public void test_GetTerritorios_e_getQtdPaises() { 
        Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
        Territorio territorio1 = new Territorio("Brasil");
        Territorio territorio2 = new Territorio("Argentina");
        jogador.addPais(territorio2, 1);
        jogador.addPais(territorio1, 1);
        assertEquals(2, jogador.getTerritorios().size());
        assertTrue(jogador.getTerritorios().contains(territorio1));
        assertTrue(jogador.getTerritorios().contains(territorio2));
        assertEquals(2, jogador.getQtdPaises());
    }
    
    
    @Test
    public void testGetPais() {
        Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
        Territorio territorio1 = new Territorio("Brasil");
        Territorio territorio2 = new Territorio("Argentina");
        jogador.addPais(territorio1, 1);
        jogador.addPais(territorio2, 1);
        assertEquals(territorio1, jogador.getPais("Brasil"));
        assertEquals(territorio2, jogador.getPais("Argentina"));
        assertNull(jogador.getPais("Chile"));
    }
    
    
    @Test
    public void testPosicionaExeCont() { // NAO FINALIZADO (?)
        Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
        Territorio territorio1 = new Territorio("Brasil");
        Territorio territorio2 = new Territorio("Argentina");
        Continente continente = new Continente("América do Sul", 2);
        continente.addTerritorio(territorio1);
        continente.addTerritorio(territorio2);
        jogador.addPais(territorio1, 1);
        jogador.addPais(territorio2, 1);
        jogador.posicionaExeCont();
        assertEquals(2, territorio1.getQntdExercitos());
        assertEquals(2, territorio2.getQntdExercitos());
    }
    

    @Test
    public void testTrocaCartas(){
        Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
        Jogador jogador2 = new Jogador(Cores.VERDE, "Jeronimo");
        Jogador jogador3 = new Jogador(Cores.AMARELO, "Luiza");
        Territorio territorio1 = new Territorio("Brasil");
        Territorio territorio2 = new Territorio("Argentina");
        Territorio territorio3 = new Territorio("Peru");
        Carta carta1 = new Carta(territorio1, Simbolos.CIRCULO);
        Carta carta2 = new Carta(territorio2, Simbolos.CIRCULO);
        Carta carta3 = new Carta(territorio3, Simbolos.CIRCULO);
        Carta[] cartasIguais = {carta1, carta2, carta3};
        jogador.recebeCarta(carta1);
        jogador.recebeCarta(carta2);
        jogador.recebeCarta(carta3);
        assertArrayEquals(cartasIguais, jogador.trocaCartas()); // Teste para 3 cartas iguais
        Carta carta4 = new Carta(territorio1, Simbolos.TRIANGULO);
        Carta carta5 = new Carta(territorio2, Simbolos.QUADRADO);
        Carta carta6 = new Carta(territorio3, Simbolos.CIRCULO);
        Carta[] cartasDiferentes = {carta4, carta5, carta6};
        jogador2.recebeCarta(carta4);
        jogador2.recebeCarta(carta5);
        jogador2.recebeCarta(carta6);
        assertArrayEquals(cartasDiferentes, jogador2.trocaCartas()); // Teste para 3 cartas diferentes
        Carta carta7 = new Carta(territorio1, Simbolos.TRIANGULO);
        Carta carta8 = new Carta(territorio2, Simbolos.QUADRADO);
        Carta carta9 = new Carta(territorio3, Simbolos.QUADRADO);
        Carta carta10 = new Carta(territorio1, Simbolos.TRIANGULO);
        jogador3.recebeCarta(carta7);
        jogador3.recebeCarta(carta8);
        jogador3.recebeCarta(carta9);
        jogador3.recebeCarta(carta10);
        assertNull(jogador3.trocaCartas()); // Teste para 4 cartas que nao podem ser trocadas
    }

}