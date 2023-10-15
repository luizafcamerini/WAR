package Model;

import static org.junit.Assert.*;
import org.junit.Test;

public class JogadorTest {
    private static final int TIMEOUT = 2000;
	private Jogador jogador;

    private boolean contem(Object [] lObj, Object obj) {
		for(Object o: lObj) {
			if(obj.equals(o))
				return true;
		}
		return false;
	}

	@Test(timeout = TIMEOUT)
	public void testSetAssassino_e_getAssassino() {
		Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
		Jogador assassino = new Jogador(Cores.VERDE, "Jeronimo");
		jogador.setAssassino(assassino);
		assertEquals(assassino, jogador.getAssassino());
	}

	@Test(timeout = TIMEOUT)
	public void testSetObjetivo_e_getDescricaoObjetivo() {
		Objetivo objetivo = new Objetivo7();
		jogador = new Jogador(Cores.VERMELHO, "Thomas");
		jogador.setObjetivo(objetivo);
		assertEquals("Conquistar 24 territorios a sua escolha", jogador.getDescricaoObjetivo());
	}

	@Test(timeout = TIMEOUT)
	public void test_GetTerritorios_e_getQtdPaises() {
		Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
		Territorio territorio1 = new Territorio("Brasil");
		Territorio territorio2 = new Territorio("Argentina");
		jogador.addTerritorio(territorio2, 1);
		jogador.addTerritorio(territorio1, 1);
		assertEquals(2, jogador.getQtdTerritorios());
		assertTrue(contem(jogador.getTerritorios(),territorio1));
		assertTrue(contem(jogador.getTerritorios(),territorio2));
		assertEquals(2, jogador.getQtdTerritorios());
	}

	@Test(timeout = TIMEOUT)
	public void testGetPais() {
		Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
		Territorio territorio1 = new Territorio("Brasil");
		Territorio territorio2 = new Territorio("Argentina");
		jogador.addTerritorio(territorio1, 1);
		jogador.addTerritorio(territorio2, 1);
		assertEquals(territorio1, jogador.getTerritorio("Brasil"));
		assertEquals(territorio2, jogador.getTerritorio("Argentina"));
		assertNull(jogador.getTerritorio("Chile"));
	}

	@Test(timeout = TIMEOUT)
	public void testPosicionaExeCont() { // NAO FINALIZADO (?)
		Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
		Territorio territorio1 = new Territorio("TerraSemLei1");
		Territorio territorio2 = new Territorio("TerraSemLei2");
		Continente continente = new Continente("Antártida", 2);
		continente.addTerritorio(territorio1);
		continente.addTerritorio(territorio2);
		jogador.addTerritorio(territorio1, 1);
		jogador.addTerritorio(territorio2, 1);
		jogador.posicionaExeCont();
		assertEquals(2, territorio1.getQntdExercitos());
		assertEquals(2, territorio2.getQntdExercitos());
	}

	@Test(timeout = TIMEOUT)
	public void testTrocaCartasIguais() {
        // Teste para 3 cartas iguais
		Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
		Territorio territorio1 = new Territorio("Brasil");
        Territorio territorio2 = new Territorio("Argentina");
        Territorio territorio3 = new Territorio("Peru");
		Carta carta1 = new Carta(territorio1, Simbolos.CIRCULO);
		Carta carta2 = new Carta(territorio2, Simbolos.CIRCULO);
		Carta carta3 = new Carta(territorio3, Simbolos.CIRCULO);
		Carta[] cartasIguais = { carta1, carta2, carta3 };
		jogador.recebeCarta(carta1);
		jogador.recebeCarta(carta2);
		jogador.recebeCarta(carta3);
		assertArrayEquals(cartasIguais, jogador.trocaCartas()); 
        assertNull(jogador.trocaCartas()); 
	}

    @Test(timeout = TIMEOUT)
    public void testTrocaCartasDiferentes(){
        // Teste para 3 cartas diferentes
        Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");;
		Territorio territorio1 = new Territorio("Brasil");
        Territorio territorio2 = new Territorio("Argentina");
        Territorio territorio3 = new Territorio("Peru");
        Carta carta1 = new Carta(territorio1, Simbolos.TRIANGULO);
		Carta carta2 = new Carta(territorio2, Simbolos.QUADRADO);
		Carta carta3 = new Carta(territorio3, Simbolos.CIRCULO);
		Carta[] cartasDiferentes = { carta1, carta2, carta3 };
		jogador.recebeCarta(carta1);
		jogador.recebeCarta(carta2);
		jogador.recebeCarta(carta3);
		assertArrayEquals(cartasDiferentes, jogador.trocaCartas()); 
        assertNull(jogador.trocaCartas()); 
    }

    @Test(timeout = TIMEOUT)
    public void testTrocaCartasCoringa(){
        // Teste para 3 cartas com 1 coringa entre elas
        Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");;
		Territorio territorio1 = new Territorio("Brasil");
        Territorio territorio2 = new Territorio("Argentina");
        Carta carta1 = new Carta(territorio1, Simbolos.TRIANGULO);
		Carta carta2 = new Carta(null, Simbolos.CORINGA);
		Carta carta3 = new Carta(territorio2, Simbolos.TRIANGULO);
		Carta[] cartasDiferentes = { carta1, carta2, carta3 };
		jogador.recebeCarta(carta1);
		jogador.recebeCarta(carta2);
		jogador.recebeCarta(carta3);
		assertArrayEquals(cartasDiferentes, jogador.trocaCartas()); 
        assertNull(jogador.trocaCartas()); 
    }

    @Test(timeout = TIMEOUT)
    public void testTrocaCartas4Diferentes(){
        // Teste para 4 cartas que nao podem ser trocadas
        Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");;
		Territorio territorio1 = new Territorio("Brasil");
        Territorio territorio2 = new Territorio("Argentina");
        Territorio territorio3 = new Territorio("Peru");
        Territorio territorio4 = new Territorio("Venezuela");
        Carta carta1 = new Carta(territorio1, Simbolos.TRIANGULO);
		Carta carta2 = new Carta(territorio2, Simbolos.QUADRADO);
		Carta carta3 = new Carta(territorio3, Simbolos.QUADRADO);
		Carta carta4 = new Carta(territorio4, Simbolos.TRIANGULO);
		jogador.recebeCarta(carta1);
		jogador.recebeCarta(carta2);
		jogador.recebeCarta(carta3);
		jogador.recebeCarta(carta4);
		assertNull(jogador.trocaCartas()); 
    }

}
