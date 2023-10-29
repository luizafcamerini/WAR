package Model;

public class ModelAPI {
	private static ModelAPI instance;
	private Jogo jogo = new Jogo();;
	private Jogador jAtual;
	
	
	private final Cores [] cores = {Cores.AMARELO, Cores.AZUL, Cores.BRANCO, Cores.PRETO, Cores.VERDE, Cores.VERMELHO};
	

	private ModelAPI() {}

	public static ModelAPI getInstance() {
		if (instance == null) {
			instance = new ModelAPI();
		}
		return instance;
	}

	public void adicionaJogador(String nome, int cor){
		jogo.adicionaJogador(new Jogador(cores[cor], nome));
	}

	
	
	public int getQtdExercitos(String territorio){
		return Territorio.getTerritorio(territorio).getQntdExercitos();
	}

	public int getCor(String territorio){
		Jogador dono = Territorio.getTerritorio(territorio).getDono();
		if (dono == null) return -1;
		Cores c =  dono.getCor();
		for(int i = 0; i < cores.length; i++){
			if (cores[i] == c)
				return i;
		}
		return -1;
	}
	
	public int getCorAtual() {
		Cores c = jAtual.getCor();
		
		for(int i = 0; i < cores.length; i++){
			if (cores[i] == c)
				return i;
		}
		return -1;
	}

	public void inicializaJogo() {
		jogo.inicializa();
		jAtual = jogo.getProxJogador();
	}
	
	public String []getTerritorios(int cor) {
		Jogador j = jogo.getJogador(cores[cor]);
		Territorio []territorios = j.getTerritorios();
		String []lst = new String[territorios.length];
		for (int i = 0; i< territorios.length; i++) {
			lst[i] = territorios[i].getNome();
		}
		return lst;
		
	}

	public String []getVizinhos(String territorio) {
		Territorio []viz = Territorio.getTerritorio(territorio).getVizinhos();
		String []lst = new String[viz.length];
		for (int i = 0; i< viz.length; i++) {
			lst[i] = viz[i].getNome();	
		}
		return lst;
	}
	
	
	
	public int[][] ataca(String atacante, String defensor) {
    	Territorio atac = Territorio.getTerritorio(atacante);
    	Territorio def = Territorio.getTerritorio(defensor);
    	return atac.atacar(def);
    	
    }

	public int getExeAd() {
		return jAtual.getExeAd();
	}
	
	public void addExe(String territorio, int n) {
		Territorio.getTerritorio(territorio).acrescentaExe(n);
	}

	

}

// Na main vai ficar assim (por enquanto, depois o controller que irá chamar
// estes métodos e a MainFutura irá chamar o controller):

/*
 * public static void main(String[] args){
 * ModelAPI modelAPI = ModelAPI.getInstance();
 * modelAPI.inicializaJogo()
 * 
 * }
 * 
 * ta assim atualmente:
 * 
 * package Model;
 * 
 * public class Main {
 * public static void main(String[] args) {
 * Jogo jogo = new Jogo();
 * Jogador jAtual;
 * 
 * jogo.adicionaJogador(new Jogador(Cores.BRANCO, "LUIZA"));
 * jogo.adicionaJogador(new Jogador(Cores.VERMELHO, "THOMAS"));
 * jogo.adicionaJogador(new Jogador(Cores.VERDE, "JERONIMO"));
 * 
 * jogo.inicializa();
 * 
 * for (int i = 0; i < jogo.getQtdJogadores(); i++) {
 * jAtual = jogo.getProxJogador();
 * System.out.printf("\n%s\n", jAtual.getNome());
 * System.out.printf("Objetivo: %s\n", jAtual.getDescricaoObjetivo());
 * 
 * for (Territorio t: jAtual.getTerritorios()){
 * System.out.printf("%s: %s\n", t.getNome(),
 * t.getDono().getNome());
 * }
 * }
 * 
 * Continente.getContinente("África").exibe();
 * Continente.getContinente("América do Norte").exibe();
 * Continente.getContinente("América do Sul").exibe();
 * Continente.getContinente("Ásia").exibe();
 * Continente.getContinente("Oceania").exibe();
 * Continente.getContinente("Europa").exibe();
 * 
 * 
 * jogo.iniciaJogo();
 * 
 * }
 * 
 * }
 * 
 */