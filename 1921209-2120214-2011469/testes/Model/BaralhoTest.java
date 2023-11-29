package Model;

import static org.junit.Assert.*;
import org.junit.Test;

public class BaralhoTest {
	private static final int TIMEOUT = 2000;
	private Territorio territorio = new Territorio("Brasil");

	@Test(timeout = TIMEOUT)
	public void testVazio() {
		Baralho<Carta> baralho = new Baralho<Carta>();
		assertTrue(baralho.vazio());
	}

	@Test(timeout = TIMEOUT)
	public void testAdiciona() {
		Baralho<Carta> baralho = new Baralho<Carta>();
		Carta carta = new Carta(territorio, Simbolos.CIRCULO);
		baralho.adiciona(carta);
		assertFalse(baralho.vazio());
	}

	@Test(timeout = TIMEOUT)
	public void testRetira() {
		Baralho<Carta> baralho = new Baralho<Carta>();
		Carta carta = new Carta(territorio, Simbolos.CIRCULO);
		assertNull(baralho.retira());
		baralho.adiciona(carta);
		assertFalse(baralho.vazio());
		assertEquals(carta, baralho.retira());
		assertTrue(baralho.vazio());
	}
}