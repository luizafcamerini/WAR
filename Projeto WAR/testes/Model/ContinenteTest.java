package Model;

import static org.junit.Assert.*;
import org.junit.Test;

public class ContinenteTest {
	private static final int TIMEOUT = 2000;

	@Test(timeout = TIMEOUT)
	public void testGetContinente() {
		Continente continente = new Continente("Antártida", 2);
		assertEquals(continente, Continente.getContinente("Antártida"));
	}

	@Test(timeout = TIMEOUT)
	public void testGetNumExeAdicionais() {
		Continente continente = new Continente("Antártida", 2);
		assertEquals(2, continente.getNumExeAdicionais());
	}

	@Test(timeout = TIMEOUT)
	public void testAddTerritorio_e_GetPaises() {
		Territorio territorio1 = new Territorio("TerraSemLei1");
		Territorio territorio2 = new Territorio("TerraSemLei2");
		Continente continente = new Continente("Antártida", 2);
		continente.addTerritorio(territorio1);
		continente.addTerritorio(territorio2);
		Territorio[] territorios = { territorio1, territorio2 };
		assertArrayEquals(territorios, continente.getTerritorios());
	}

	@Test(timeout = TIMEOUT)
	public void testAddTerritorioExistente() {
		Territorio territorio1 = new Territorio("TerraSemLei1");
		Territorio territorio2 = new Territorio("TerraSemLei2");
		Continente continente = new Continente("Antártida", 2);
		continente.addTerritorio(territorio1);
		continente.addTerritorio(territorio2);
		continente.addTerritorio(territorio2);
		assertEquals(2, continente.getTerritorios().length);
	}

	@Test(timeout = TIMEOUT)
	public void testPertence() {
		Jogador jogador1 = new Jogador(Cores.VERMELHO, "Thomas");
		Jogador jogador2 = new Jogador(Cores.BRANCO, "Luiza");
		Territorio territorio1 = new Territorio("TerraSemLei1");
		Territorio territorio2 = new Territorio("TerraSemLei2");
		Continente continente = new Continente("Antártida", 2);
		continente.addTerritorio(territorio1);
		continente.addTerritorio(territorio2);
		territorio1.trocaDono(jogador1);
		territorio2.trocaDono(jogador2);
		assertFalse(continente.pertence(jogador1));
		territorio2.trocaDono(jogador1);
		assertTrue(continente.pertence(jogador1));
		territorio2.trocaDono(jogador2);
		assertFalse(continente.pertence(jogador1));
	}
}