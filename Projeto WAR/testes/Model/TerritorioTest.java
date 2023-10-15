package Model;

import static org.junit.Assert.*;
import org.junit.Test;

public class TerritorioTest {
	private static final int TIMEOUT = 2000;
	
	private boolean contem(Object [] lObj, Object obj) {
		for(Object o: lObj) {
			if(o.equals(obj))
				return true;
		}
		return false;
	}

	@Test(timeout = TIMEOUT)
	public void testQtdExeInicial() {
		Territorio t = new Territorio("Terra1");
		assertEquals("Quantidade inicial de exercitos",1,t.getQntdExercitos());
	}
	
	@Test(timeout = TIMEOUT)
	public void testAddExe() {
		Territorio t = new Territorio("Terra1");
		t.acrescentaExe(2);
		assertEquals("Acrescenta exercitos",3,t.getQntdExercitos());
	}
	
	@Test(timeout = TIMEOUT)
	public void testReduzExe() {
		Territorio t = new Territorio("Terra1");
		t.acrescentaExe(6);
		t.reduzExe(5);
		assertEquals("Reduz exercitos",2,t.getQntdExercitos());
	}
	
	@Test(timeout = TIMEOUT)
	public void testTrocaDono() {
		Territorio t = new Territorio("Terra1");
		Jogador j1 = new Jogador(Cores.AMARELO, "Jogador1");
		Jogador j2 = new Jogador(Cores.AMARELO, "Jogador2");
		t.trocaDono(j1, 2);
		assertEquals(j1, t.getDono());
		assertEquals(2, t.getQntdExercitos());
		t.trocaDono(j2, 3);
		assertEquals(j2, t.getDono());
		assertEquals(3, t.getQntdExercitos());
	}

	@Test(timeout = TIMEOUT)
	public void testVizinhosExistem() {
		Baralho<Carta> cartas = Territorio.montaBaralho();
		Territorio t, viz[];
		String msg;
		while(!cartas.vazio()) {
			t = cartas.retira().getTerritorio();
			viz = t.getVizinhos();
			for(Territorio v: viz) {
				msg = String.format("Vizinho de %s é null.",t.getNome());
				assertNotNull(msg, v);
			}
		}
	}

	@Test(timeout = TIMEOUT)
	public void testVizinhosCorrespondem() {
		Baralho<Carta> cartas = Territorio.montaBaralho();
		Territorio t, viz[];
		while(!cartas.vazio()) {
			t = cartas.retira().getTerritorio();
			viz = t.getVizinhos();
			for(Territorio v: viz) {
				String msg = String.format("Vizinhos %s e %s não correspondem.", v.getNome(),t.getNome());
				assertTrue(msg,contem(v.getVizinhos(),t));
			}
		}
	}
}
