package Model;

import Model.Jogador.Cores;

public class Main {
	public static void main(String[] args) {
		Jogo jogo = new Jogo();
		Jogador jAtual;

		jogo.adicionaJogador(new Jogador(Cores.BRANCO, "LUIZA"));
		jogo.adicionaJogador(new Jogador(Cores.VERMELHO, "THOMAS"));
		jogo.adicionaJogador(new Jogador(Cores.VERDE, "JERONIMO"));

		jogo.inicializa();

		for (int i = 0; i < jogo.getQtdJogadores(); i++) {
			jAtual = jogo.getProxJogador();
			System.out.printf("\n%s\n", jAtual.getNome());
			System.out.printf("Objetivo: %s\n", jAtual.getDescricaoObjetivo());

			for (int j = 0; j < jAtual.paises.size(); j++) {
				System.out.printf("%s: %s\n", jAtual.paises.get(j).getNome(), jAtual.paises.get(j).getDono().getNome());
			}
		}

		Continente.getContinente("África").exibe();
		Continente.getContinente("América do Norte").exibe();
		Continente.getContinente("América do Sul").exibe();
		Continente.getContinente("Ásia").exibe();
		Continente.getContinente("Oceania").exibe();
		Continente.getContinente("Europa").exibe();
	}

}
