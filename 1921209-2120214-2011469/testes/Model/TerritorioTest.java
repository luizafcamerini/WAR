package Model;

import static org.junit.Assert.*;
import org.junit.Test;

public class TerritorioTest {
	private static final int TIMEOUT = 2000;

	private boolean contem(Object[] lObj, Object obj) {
		for (Object o : lObj) {
			if (obj.equals(o))
				return true;
		}
		return false;
	}

	@Test(timeout = TIMEOUT)
	public void testQtdExeInicial() {
		Territorio t = new Territorio("Terra1");
		assertEquals("Quantidade inicial de exercitos", 1, t.getQntdExercitos());
	}

	@Test(timeout = TIMEOUT)
	public void testAddExe() {
		Territorio t = new Territorio("Terra1");
		t.acrescentaExe(2);
		assertEquals("Acrescenta exercitos", 3, t.getQntdExercitos());
	}

	@Test(timeout = TIMEOUT)
	public void testReduzExe() {
		Territorio t = new Territorio("Terra1");
		t.acrescentaExe(6);
		t.reduzExe(5);
		assertEquals("Reduz exercitos", 2, t.getQntdExercitos());
	}

	@Test(timeout = TIMEOUT)
	public void testTrocaDono() {
		Territorio t = new Territorio("Terra1");
		Jogador j1 = new Jogador(Cores.AMARELO, "Jogador1");
		Jogador j2 = new Jogador(Cores.AMARELO, "Jogador2");
		t.acrescentaExe(2);
		t.trocaDono(j1);
		assertEquals(j1, t.getDono());
		assertEquals(1, t.getQntdExercitos());
		t.trocaDono(j2);
		assertFalse(contem(j1.getTerritorios(), t));
		assertEquals(j2, t.getDono());
		assertEquals(1, t.getQntdExercitos());
	}

	@Test(timeout = TIMEOUT)
	public void testVizinhosExistem() {
		Baralho<Carta> cartas = Territorio.montaBaralho();
		Territorio t, viz[];
		String msg;
		while (!cartas.vazio()) {
			t = cartas.retira().getTerritorio();
			viz = t.getVizinhos();
			for (Territorio v : viz) {
				msg = String.format("Vizinho de %s é null.", t.getNome());
				assertNotNull(msg, v);
			}
		}
	}

	@Test(timeout = TIMEOUT)
	public void testAtacar() {
		Territorio[] territorios = Territorio.getTerritorios();
		Territorio t1 = territorios[0];
		Territorio t2 = t1.getVizinhos()[0];
		Jogador j1 = new Jogador(Cores.AMARELO, "Jogador1");
		Jogador j2 = new Jogador(Cores.AMARELO, "Jogador2");
		t1.trocaDono(j1);
		t2.trocaDono(j2);
		t1.acrescentaExe(3);
		t2.acrescentaExe(3);
		int[][] dados = { { 2, 3, 4 }, { 1, 6, 1 } };
		t1.atacar(t2, dados);
		assertEquals(3, t1.getQntdExercitos());
		assertEquals(2, t2.getQntdExercitos());
		int[][] dados2 = { { 2, 6, 5 }, { 4, 5 } };
		t1.atacar(t2, dados2);
		assertEquals(2, t1.getQntdExercitos());
		assertEquals(1, t2.getQntdExercitos());
		assertEquals(t2.getDono(), j1);
		assertEquals(j2.getAssassino(), j1);
	}

	@Test(timeout = TIMEOUT)
	public void testVizinhosCorrespondem() {
		Baralho<Carta> cartas = Territorio.montaBaralho();
		Territorio t, viz[];
		while (!cartas.vazio()) {
			t = cartas.retira().getTerritorio();
			viz = t.getVizinhos();
			for (Territorio v : viz) {
				String msg = String.format("Vizinhos %s e %s não correspondem.", v.getNome(), t.getNome());
				assertTrue(msg, contem(v.getVizinhos(), t));
			}
		}
	}
}
