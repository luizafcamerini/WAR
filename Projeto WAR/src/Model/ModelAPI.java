package Model;

public class ModelAPI{
    private static ModelAPI instance;
    private Jogo jogo;
    private Jogador JAtual;

    public ModelAPI(){
        instance = this;
    }

    public static ModelAPI getInstance(){
        if(instance == null){
            instance = new ModelAPI();
        }
        return instance;
    }

    public void inicializaJogo(){
        if(jogo == null){
            jogo = new Jogo();
        }
        jogo.adicionaJogador(new Jogador(Cores.BRANCO, "LUIZA"));
		jogo.adicionaJogador(new Jogador(Cores.VERMELHO, "THOMAS"));
		jogo.adicionaJogador(new Jogador(Cores.VERDE, "JERONIMO"));
        jogo.inicializa();
    }

    
}


// Na main vai ficar assim (por enquanto, depois o controller que irá chamar estes métodos e a MainFutura irá chamar o controller):

/*
public static void main(String[] args){
    ModelAPI modelAPI = ModelAPI.getInstance();
    modelAPI.inicializaJogo()

}

ta assim atualmente:

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

			for (Territorio t: jAtual.getTerritorios()){
				System.out.printf("%s: %s\n", t.getNome(),
						t.getDono().getNome());
			}
		}

		Continente.getContinente("África").exibe();
		Continente.getContinente("América do Norte").exibe();
		Continente.getContinente("América do Sul").exibe();
		Continente.getContinente("Ásia").exibe();
		Continente.getContinente("Oceania").exibe();
		Continente.getContinente("Europa").exibe();


		jogo.iniciaJogo();

	}

}

*/