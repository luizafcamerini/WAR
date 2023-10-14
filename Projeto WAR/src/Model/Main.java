package Model;


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

		while (true){
			jAtual = jogo.getProxJogador();
			Continente cont = Continente.getContinente("América do Norte");

			if (cont.pertence(jAtual)){
				jAtual.addExeContinente(cont);
				int exeRest = cont.getNumExeAdicionais();

				// 1° while (recebe exercitos correspondentes ao continente conquistado e posiciona)
				while (exeRest > 0){

					int n = 1;// = input();
					int iPais = 0;// = input();

					if (n <= exeRest){
						cont.getPaises().get(iPais).acrescentaExe(n);
						exeRest -= n;
					}
				}

				// enqunto temExeP:
				// 	add n no território
				
				// jAtual.exibeTerritoriosDisponiveis();


			}
			if (Continente.getContinente("América do Sul").pertence(jAtual)){

			}
			if (Continente.getContinente("África").pertence(jAtual)){

			}
			if (Continente.getContinente("Europa").pertence(jAtual)){

			}
			if (Continente.getContinente("Oceania").pertence(jAtual)){

			}
			if (Continente.getContinente("Ásia").pertence(jAtual)){

			}
			break;
		}
	}

}
