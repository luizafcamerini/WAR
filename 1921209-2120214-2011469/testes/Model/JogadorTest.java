package Model;

import static org.junit.Assert.*;
import org.junit.Test;

public class JogadorTest {
	private static final int TIMEOUT = 2000;
	private Jogador jogador;

	private boolean contem(Object[] lObj, Object obj) {
		for (Object o : lObj) {
			if (obj.equals(o))
				return true;
		}
		return false;
	}

	@Test(timeout = TIMEOUT)
	public void testSetAssassino_e_GetAssassino() {
		Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
		Jogador assassino = new Jogador(Cores.VERDE, "Jeronimo");
		jogador.setAssassino(assassino);
		assertEquals(assassino, jogador.getAssassino());
	}

	@Test(timeout = TIMEOUT)
	public void testSetObjetivo_e_GetDescricaoObjetivo() {
		Objetivo objetivo = new Objetivo7();
		jogador = new Jogador(Cores.VERMELHO, "Thomas");
		jogador.setObjetivo(objetivo);
		assertEquals(objetivo.getDescricao(), jogador.getDescricaoObjetivo());
	}

	@Test(timeout = TIMEOUT)
	public void test_GetTerritorios_e_GetQtdPaises() {
		Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
		Territorio territorio1 = new Territorio("Brasil");
		Territorio territorio2 = new Territorio("Argentina");
		jogador.addTerritorio(territorio2);
		jogador.addTerritorio(territorio1);
		assertEquals(2, jogador.getQtdTerritorios());
		assertTrue(contem(jogador.getTerritorios(), territorio1));
		assertTrue(contem(jogador.getTerritorios(), territorio2));
	}

	@Test(timeout = TIMEOUT)
	public void testRemoveTerritorio() {
		Jogador jogador = new Jogador(Cores.VERMELHO, "Thomas");
		Territorio territorio1 = new Territorio("Brasil");
		jogador.addTerritorio(territorio1);
		assertTrue(contem(jogador.getTerritorios(), territorio1));
		jogador.removeTerritorio(territorio1);
		assertFalse(contem(jogador.getTerritorios(), territorio1));
	}

}
