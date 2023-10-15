package Model;
import static org.junit.Assert.*;
import org.junit.Test;

public class JogoTest {
	private static final int TIMEOUT = 2000;
	
	@Test(timeout = TIMEOUT)
    public void testQtdJogadores(){
        Jogo jogo = new Jogo(); // create a new Jogo object
        jogo.adicionaJogador(new Jogador(Cores.BRANCO, "LUIZA"));
        assertEquals(1, jogo.getQtdJogadores());
        jogo.adicionaJogador(new Jogador(Cores.VERDE, "LUIZA"));
        assertEquals(2, jogo.getQtdJogadores());
    }

    

}
