package Model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TerritorioTest {
	private static final int TIMEOUT = 2000;
	
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
	

}
