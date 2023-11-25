package Model;

import java.util.Random;
import java.util.ArrayList;


class Jogo {
	private ArrayList<Jogador> jogadores = new ArrayList<Jogador>();
	private int iterador; // Usado para iterar na lista jogadores (iterador%jogadores.size())
	private Baralho<Carta> cartas; // cartas totais do jogo
	private Baralho<Carta> cartasUsadas; // cartas usadas na partida
	private Baralho<Objetivo> objetivos;
	private int contadorTroca = 1;

	public Jogo() {
		/** Construtor que monta o mapa do jogo e cria um novo baralho. */
		cartas = Territorio.montaBaralho();
		cartasUsadas = new Baralho<Carta>();
		objetivos = Objetivo.montaBaralho();
	}

	public void limpa(){
		/** Funcao que limpa os dados do jogo. */
		jogadores = new ArrayList<Jogador>();
		cartas = null;
		cartas = null;
		cartasUsadas = null;
		objetivos = null;
		contadorTroca = 1;
	}

	public void iniciaJogo() {
		/** Funcao que representa o loop das rodadas do jogo, ate alguem vencer. */
		Jogador jAtual;
		while (true) { // rodada
			jAtual = getProxJogador();
			if (jAtual.verificaObjetivo())
				break; // Jogador vence o jogo

			Carta[] descartadas = jAtual.trocaCartas();
			int exeAd = 0;
			if (descartadas != null) {
				exeAd = trocaCartas(jAtual, descartadas);
			}
			jAtual.posicionaExe(exeAd);
			break; // break temporario
		}
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
		cartas.adiciona(new Carta(null, Simbolos.CORINGA));
		cartas.adiciona(new Carta(null, Simbolos.CORINGA));
		cartas.embaralha();

		distribuiObjetivos();
	}

	public void continuaJogo(Jogador j){
		/**Funcao que continua um jogo carregado por um txt. Usado em Model.loadGame() */
		iterador = jogadores.indexOf(j);
		cartas.embaralha();
	}

	public void adicionaJogador(Jogador j) {
		/**
		 * Funcao que adiciona os jogadores da partida na lista de jogadores e adiciona
		 * objetivos em relacao a cor destes jogadores.
		 */
		if (jogadores.size() < 6) {
			jogadores.add(j);

			Cores cor = j.getCor();
			objetivos.adiciona(Objetivo.setAlvo(cor, j));
		}
	}

	public Jogador getJogador(int i) {
		/** Funcao que retorna o jogador de indice i. */
		return jogadores.get(i);
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

	public int getIterador() {
		/** Funcao que retorna o iterador da lista de jogadores. */
		return iterador;
	}

	public void exibeJogadores(){
		/** Funcao que exibe os jogadores da partida. */
		for (Jogador j : jogadores) {
			System.out.println(j.getNome() + " " + j.getCor());
		}
	}

	public Jogador getJogadorCor(Cores cor) {
		Jogador j;
		for (int i = 0; i < jogadores.size(); i++) {
			j = jogadores.get(i);
			if (j.getCor() == cor)
				return j;
		}
		return null;
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
		Jogador j;
		while (!cartas.vazio()) {
			j = jogadores.get(iterador % jogadores.size());
			c = cartas.retira();
			c.getTerritorio().trocaDono(j);
			cartasUsadas.adiciona(c);
			iterador++;
		}
	}

	private void distribuiObjetivos() {
		/** Funcao que embaralha e distribui os objetivos para os jogadores. */
		objetivos.embaralha();
		Jogador j;
		for (int i = 0; i < jogadores.size(); i++) {
			j = jogadores.get(i);
			j.setObjetivo(objetivos.retira());
		}
	}

	public void entregaCarta(Jogador j) {
		/** Funcao que entrega uma carta do baralho ao jogador atual. */
		Carta carta = cartas.retira();
		j.recebeCarta(carta);
		System.out.println(carta.getSimbolo());

		if (carta.getTerritorio() != null)
			System.out.println(carta.getTerritorio().getNome());

		/** Reembaralha monte de cartas caso ele fique vazio */
		if (cartas.vazio()) {
			Baralho<Carta> aux = cartas;
			cartas = cartasUsadas;
			cartasUsadas = aux;
			cartas.embaralha();
		}
	}

	public void entregaCarta(Jogador j, String nomeTerritorio) {
		// System.out.println("Buscando por carta: " + nomeTerritorio); // apagar
		/** Funcao que entrega uma carta específica do baralho ao jogador atual. Usada em ModelAPI.loadGame()*/
		Carta carta = null;
		int i = 0;
		if(cartas.vazio()){
			System.out.println("Baralho de cartas vazio");
		}
		for(Carta c : cartas.array()){
			if (!nomeTerritorio.equals("null") && c.getTerritorio() != null){
				System.out.println("Carta: " + c.getTerritorio().getNome());
				if(c.getTerritorio().getNome().equals(nomeTerritorio)){
					carta = cartas.retira(i);
					j.recebeCarta(carta);
					System.out.println("Entregando carta: " + carta.getTerritorio().getNome() + " para o jogador: " + j.getNome());
					break;
				}
			}
			else{
				if(nomeTerritorio.equals("null") && c.getSimbolo() == Simbolos.CORINGA){
					carta = c;
					j.recebeCarta(carta);
					carta = cartas.retira(i);
					System.out.println("Entregando carta: " + carta.getSimbolo() + " para o jogador: " + j.getNome());
					break;
				}
			}
			i++;
		}
		if(carta == null){
			System.out.println("Carta " + nomeTerritorio + " não encontrada");
		}
	}

	private int trocaCartas(Jogador j, Carta[] descartadas) {
		/**
		 * Funcao que retorna a quantidade adicional de exercitos em relacao a troca de
		 * cartas.
		 */
		for (Carta c : descartadas) {
			Territorio t = c.getTerritorio();
			if (t != null && t.getDono() == j)
				c.getTerritorio().acrescentaExe(2);
			cartasUsadas.adiciona(c);
		}

		int exeAd;
		if (contadorTroca < 6) {
			exeAd = 2 + 2 * contadorTroca;
		} else {
			exeAd = 5 * (contadorTroca - 3);
		}
		contadorTroca++;
		return exeAd;
	}

	public void adicionaCoringas(){
		/** Funcao que adiciona coringas no baralho de cartas. */
		cartas.adiciona(new Carta(null, Simbolos.CORINGA));
		cartas.adiciona(new Carta(null, Simbolos.CORINGA));
		cartas.embaralha();
	}

}
