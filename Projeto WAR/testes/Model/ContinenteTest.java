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
		Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
		Territorio territorio1 = new Territorio("TerraSemLei1");
		Territorio territorio2 = new Territorio("TerraSemLei2");
		Continente continente = new Continente("Antártida", 2);
		continente.addTerritorio(territorio1);
		continente.addTerritorio(territorio2);
		jogador.addTerritorio(territorio1, 1);
		jogador.addTerritorio(territorio2, 1);
		assertTrue(continente.pertence(jogador));
	}
}