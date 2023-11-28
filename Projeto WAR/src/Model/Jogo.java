package Model;

import java.util.ArrayList;
import java.util.Collections;

class Jogo {
	private ArrayList<Jogador> jogadores = new ArrayList<Jogador>();
	// Iterador usado na lista de jogadores (iterador%jogadores.size()):
	private int iterador;
	// Cartas totais do jogo (monte):
	private Baralho<Carta> cartas;
	// Cartas usadas na partida (retiradas do monte):
	private Baralho<Carta> cartasUsadas;
	private Baralho<Objetivo> objetivos;
	private int contadorTroca = 1;

	public Jogo() {
		/** Construtor que monta o mapa do jogo e cria um novo baralho. */
		cartas = Territorio.montaBaralho();
		cartasUsadas = new Baralho<Carta>();
		objetivos = Objetivo.montaBaralho();
	}

	public void inicializa() {
		/** Metodo que inicializa as distribuicoes do jogo. */
		Collections.shuffle(jogadores); // Sorteia ordem dos jogadores
		System.out.println("O jogador " + jogadores.get(iterador % jogadores.size()).getNome()
				+ " começa distribuindo as cartas.");

		distribuiTerritorios(); // distribui as cartas com os territorios
		System.out.println("O jogador " + jogadores.get(iterador % jogadores.size()).getNome() + " começa o jogo.");

		// Volta as cartas de territorios usadas para o monte:
		Baralho<Carta> aux = cartas;
		cartas = cartasUsadas;
		cartasUsadas = aux;

		// Adiciona os coringas no monte e embaralha novamente:
		cartas.adiciona(new Carta(null, Simbolos.CORINGA));
		cartas.adiciona(new Carta(null, Simbolos.CORINGA));
		cartas.embaralha();

		// Distribui as cartas dos objetivos:
		distribuiObjetivos();
	}

	public void adicionaJogador(Jogador j) {
		/**
		 * Metodo que adiciona os jogadores da partida na lista de jogadores e adiciona
		 * objetivos em relacao a cor destes jogadores.
		 */
		if (jogadores.size() < 6) {
			jogadores.add(j);

			Cores cor = j.getCor();
			objetivos.adiciona(Objetivo.setAlvo(cor, j));
		}
	}

	public Jogador getProxJogador() {
		/** Metodo que retorna o proximo jogador da partida. */
		Jogador j = jogadores.get(iterador % jogadores.size());
		iterador++;
		if (j.getAssassino() != null) { // Jogador morto, então pula para o próximo
			return getProxJogador();
		}
		return j;
	}

	public void exibeJogadores() {
		/** Metodo que exibe os jogadores da partida. */
		for (Jogador j : jogadores) {
			System.out.println(j.getNome() + " " + j.getCor());
		}
	}

	public Jogador getJogadorCor(Cores cor) {
		/** Metodo que retorna o jogador de uma dada cor. */
		Jogador j;
		for (int i = 0; i < jogadores.size(); i++) {
			j = jogadores.get(i);
			if (j.getCor() == cor)
				return j;
		}
		return null;
	}

	private void distribuiTerritorios() {
		/** Metodo que distribui as cartas e preenche o mapa com os exercitos. */
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
		iterador = iterador % jogadores.size();
	}

	private void distribuiObjetivos() {
		/** Metodo que embaralha e distribui os objetivos para os jogadores. */
		objetivos.embaralha();
		Jogador j;
		for (int i = 0; i < jogadores.size(); i++) {
			j = jogadores.get(i);
			j.setObjetivo(objetivos.retira());
		}
	}

	public void entregaCarta(Jogador j) {
		/** Metodo que entrega uma carta do baralho ao jogador atual. */
		if (j.getCartas().length >= 5) {
			System.out.println(
					"Não foi possível entregar carta ao jogador " + j.getNome() + " pois ele já possui 5 cartas");
			return;
		}
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

	public int trocaCartas(Jogador j, Carta[] descartadas) {
		/**
		 * Metodo que retorna a quantidade adicional de exercitos em relacao a troca de
		 * cartas e que acrescenta exercitos pela carta trocada com o territorio
		 * conquistado.
		 */
		for (Carta c : descartadas) {
			Territorio t = c.getTerritorio();
			if (t != null && t.getDono() == j)
				c.getTerritorio().acrescentaExe(2);
			cartasUsadas.adiciona(c);
		}

		// Calcula os exercitos adicionais pelo contador de troca:
		int exeAd;
		if (contadorTroca < 6) {
			exeAd = 2 + 2 * contadorTroca;
		} else {
			exeAd = 5 * (contadorTroca - 3);
		}
		contadorTroca++;
		return exeAd;
	}

	public String getEstadoStr() {
		/** Metodo que salva o estado do jogo em uma string. */
		String estado = "";
		Jogador jogador; // começa salvando pelo jogador da vez
		String nomeJogador;
		Territorio[] territorios;
		String nomeTerritorio;
		String objetivoJogador;
		String assassino;
		int qtdExercitos;
		String corJogador;
		Carta[] cartasJogador;
		Carta carta;

		// Estado comecao com o numero da troca e numero da rodada:
		estado += Integer.toString(contadorTroca) + ',' + Integer.toString(iterador) + '\n';

		// Escreve as cartas do baralho nao usado de cartas:
		if ((!cartas.vazio())) {
			Baralho<Carta> temp = new Baralho<Carta>();
			while (!cartas.vazio()) {
				carta = cartas.retira();
				if (carta.getSimbolo() == Simbolos.CORINGA)
					estado += "CORINGA,";
				else
					estado += carta.getTerritorio().getNome() + ',';
				temp.adiciona(carta);
			}
			cartas = temp;
		} else {
			estado += ',';
		}

		estado += '\n';

		// Escreve as cartas usadas (cartas descartadas pelos jogadores):
		if ((!cartasUsadas.vazio())) {
			Baralho<Carta> temp = new Baralho<Carta>();
			while (!cartasUsadas.vazio()) {
				carta = cartasUsadas.retira();
				if (carta.getSimbolo() == Simbolos.CORINGA)
					estado += "CORINGA,";
				else
					estado += carta.getTerritorio().getNome() + ',';
				temp.adiciona(carta);
			}
			cartasUsadas = temp;
		} else {
			estado += ',';
		}

		estado += '\n';

		for (int i = 0; i < jogadores.size(); i++) {
			// Escreve o nome, cor, assassino (null ou nao) e numero do objetivo do jogador:
			jogador = jogadores.get(i);
			corJogador = jogador.getCor().toString();
			// Pega o numero do objetivo do jogador:
			objetivoJogador = jogador.getImgNameObjetivo().replaceAll("\\D+", "");
			cartasJogador = jogador.getCartas();
			nomeJogador = jogador.getNome();
			if (jogador.getAssassino() != null) {
				assassino = jogador.getAssassino().getCor().toString();
			} else {
				assassino = "null";
			}

			estado += nomeJogador + ',' + corJogador + ',' + assassino + '\n';
			estado += objetivoJogador + '\n';

			// Escreve as cartas do jogador:
			if (cartasJogador.length != 0) {
				for (Carta c : cartasJogador) {
					if (c.getSimbolo() == Simbolos.CORINGA)
						estado += "CORINGA,";
					else
						estado += c.getTerritorio().getNome() + ',';
				}
			} else {
				estado += ',';
			}

			estado += '\n';

			// Escreve os territorios do jogador e a quantidade de exercito em cada um
			territorios = jogador.getTerritorios();
			for (Territorio t : territorios) {
				nomeTerritorio = t.getNome();
				qtdExercitos = t.getQntdExercitos();
				estado += nomeTerritorio + "," + qtdExercitos + '\n';
			}

			// Separa os dados de cada jogador com um ponto e virgula:
			estado += ";\n";
		}
		return estado;
	}

	public void setCartas(Baralho<Carta> baralho) {
		/** Metodo que define o baralho de cartas. */
		cartas = baralho;
	}

	public void setCartasUsadas(Baralho<Carta> baralho) {
		/** Metodo que define o baralho de cartas usadas. */
		cartasUsadas = baralho;
	}

	public void setContadorTroca(int n) {
		/** Metodo que define o contaador de troca de cartas. */
		contadorTroca = n;
	}

	public void setIterador(int n) {
		/** Metodo que define o iterador da lista de jogadores. */
		iterador = n;
	}

	public void entregaCartaAssassino(Jogador assassino, Jogador morto) {
		/** Metodo que entrega as cartas do jogador morto para seu assassino. */
		Baralho<Carta> tempBaralho = new Baralho<Carta>();
		int count = assassino.getCartas().length;

		while (morto.getCartas().length > 0) {
			tempBaralho.adiciona(morto.removeCarta(0));
		}
		tempBaralho.embaralha();
		while (!tempBaralho.vazio()) {
			Carta carta = tempBaralho.retira();
			if (count < 5) {
				assassino.recebeCarta(carta);
				count++;
			} else
				cartasUsadas.adiciona(carta);
		}
	}

}
