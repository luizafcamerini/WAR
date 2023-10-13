package Model;
import java.util.Random;
import java.util.ArrayList;

class Jogo {
    private ArrayList <Jogador> jogadores = new ArrayList<Jogador>();
    private int iterador; // Usado para iterar na lista jogadores (iterador%jogadores.size())
    private Baralho cartas; //cartas totais do jogo
    private Baralho cartasUsadas; //cartas usadas na partida
    
    public Jogo() {
        /** Construtor que monta o mapa do jogo e cria um novo baralho. */
    	cartas = Territorio.montaMapa();
    	cartasUsadas = new Baralho();
    }
    
    public void adicionaJogador(Jogador j) {
        /** Funcao que adiciona os jogadores da partida na lista de jogadores. */
    	if (jogadores.size() < 6)
    		jogadores.add(j);
    }
    
    public int getQtdJogadores() {
        /** Funcao que retorna a quantidade de jogadores da partida. */
    	return jogadores.size();
    }
    
    public void inicializa() {
    	/** Funcao que inicializa a partida. */
    	iterador = escolheJogador();
    	System.out.println("O jogador " + jogadores.get(iterador%jogadores.size()).getNome() + " começa distribuindo as cartas.");
    	
    	distribuiCartas();
    	System.out.println("O jogador " + jogadores.get(iterador%jogadores.size()).getNome() + " começa o jogo.");
    	
    	// Volta as cartas usadas para o monte.
    	Baralho aux = cartas;
    	cartas = cartasUsadas;
    	cartasUsadas = aux;
    	
    	// Adiciona os coringas no monte e embaralha novamente.
    	cartas.adiciona(new Carta(null,Carta.Simbolo.CORINGA));
    	cartas.adiciona(new Carta(null,Carta.Simbolo.CORINGA));
    	cartas.embaralha();
    }
    
    public Jogador getProxJogador() {
        /** Funcao que retorna o proximo jogador */
    	Jogador j = jogadores.get(iterador%jogadores.size());
    	iterador++;
    	return j;
    }

    private int escolheJogador(){
        /** Funcao que retorna um indice aleatorio do jogador que vai começar distribuindo as cartas. */
    	int tamLista = jogadores.size();
        Random rand = new Random();
        int i = rand.nextInt(tamLista); //escolhe entre 0 e tamLista-1
        return i;
    }

    private void distribuiCartas(){
    	/** Funcao que distribui as cartas e preenche o mapa com os exercitos. */
    	cartas.embaralha();
    	Carta c;
    	while(!cartas.vazio()) {
    		c = (Carta) cartas.retira();
    		jogadores.get(iterador%jogadores.size()).addPais(c.getTerritorio(),1);
    		cartasUsadas.adiciona(c);
    		iterador++;
    	}
    }
}
