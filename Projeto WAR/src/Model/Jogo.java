package Model;

import java.util.Random;
import java.util.ArrayList;

class Jogo {
	private ArrayList<Jogador> jogadores = new ArrayList<Jogador>();
	private int iterador; // Usado para iterar na lista jogadores (iterador%jogadores.size())
	private Baralho<Carta> cartas; // cartas totais do jogo
	private Baralho<Carta> cartasUsadas; // cartas usadas na partida
	private Baralho<Objetivo> objetivos;

	public Jogo() {
		/** Construtor que monta o mapa do jogo e cria um novo baralho. */
		cartas = Territorio.montaBaralho();
		cartasUsadas = new Baralho<Carta>();

		objetivos = Objetivo.montaBaralho();
	}

	public void inicializa() {
		/** Funcao que inicializa as distribuicoes do jogo. */
		iterador = escolheJogador();
		System.out.println("O jogador " + jogadores.get(iterador % jogadores.size()).getNome()
				+ " começa distribuindo as cartas.");

		distribuiTerritorios();
		System.out.println("O jogador " + jogadores.get(iterador % jogadores.size()).getNome() + " começa o jogo.");

		// Volta as cartas usadas para o monte.
		Baralho<Carta> aux = cartas;
		cartas = cartasUsadas;
		cartasUsadas = aux;

		// Adiciona os coringas no monte e embaralha novamente.
		cartas.adiciona(new Carta(null, Carta.Simbolo.CORINGA));
		cartas.adiciona(new Carta(null, Carta.Simbolo.CORINGA));
		cartas.embaralha();

		distribuiObjetivos();
	}

	public void adicionaJogador(Jogador j) {
		/** Funcao que adiciona os jogadores da partida na lista de jogadores. */
		if (jogadores.size() < 6) {
			jogadores.add(j);

			// Adiciona carta de destruir jogador no baralho de objetivos
			Jogador.Cores cor = j.getCor();
			switch (cor) {
				case AZUL:
					objetivos.adiciona(new Objetivo1(j));
					break;
				case AMARELO:
					objetivos.adiciona(new Objetivo2(j));
					break;
				case VERMELHO:
					objetivos.adiciona(new Objetivo3(j));
					break;
				case PRETO:
					objetivos.adiciona(new Objetivo4(j));
					break;
				case BRANCO:
					objetivos.adiciona(new Objetivo5(j));
					break;
				case VERDE:
					objetivos.adiciona(new Objetivo6(j));
					break;
			}
		}
	}

	public int getQtdJogadores() {
		/** Funcao que retorna a quantidade de jogadores da partida. */
		return jogadores.size();
	}

	public Jogador getProxJogador() {
		/** Funcao que retorna o proximo jogador */
		Jogador j = jogadores.get(iterador % jogadores.size());
		iterador++;
		return j;
	}

	private int escolheJogador() {
		/**
		 * Funcao que retorna um indice aleatorio do jogador que vai começar
		 * distribuindo as cartas.
		 */
		int tamLista = jogadores.size();
		Random rand = new Random();
		int i = rand.nextInt(tamLista); // escolhe entre 0 e tamLista-1
		return i;
	}

	private void distribuiTerritorios() {
		/** Funcao que distribui as cartas e preenche o mapa com os exercitos. */
		cartas.embaralha();
		Carta c;
		while (!cartas.vazio()) {
			c = cartas.retira();
			jogadores.get(iterador % jogadores.size()).addPais(c.getTerritorio(), 1);
			cartasUsadas.adiciona(c);
			iterador++;
		}
	}

	private void distribuiObjetivos() {
		objetivos.embaralha();
		Jogador j;
		for (int i = 0; i < jogadores.size(); i++) {
			j = jogadores.get(i);
			j.setObjetivo(objetivos.retira());
		}
	}

}
