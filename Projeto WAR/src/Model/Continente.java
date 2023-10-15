package Model;

import java.util.ArrayList;
import java.util.Hashtable;

class Continente {
	private static Hashtable<String, Continente> continentes = new Hashtable<String, Continente>();

	private ArrayList<Territorio> territorios = new ArrayList<>();
	private String nome;
	private int numExeAdicionais;

	public Continente(String nome, int nExe) {
		/** Construtor que cria um continente com seu nome e numero de exercitos adicionais e o coloca na hashtable. */
		this.nome = nome;
		numExeAdicionais = nExe;
		continentes.put(nome, this);
	}
	
	public static Continente getContinente(String nome) {
		/** Funcao que retorna um continente pelo seu nome. */
		return continentes.get(nome);
	}

	public ArrayList<Territorio> getTerritorios() {
		/** Funcao que retorna uma copia da lista de territorios de um continente. */
		return (ArrayList<Territorio>) this.territorios.clone();
	}

	public int getNumExeAdicionais() {
		/** Funcao que retorna o numero de exercitos adicionais de um continente dominado. */
		return this.numExeAdicionais;
	}

	public void addTerritorio(Territorio t) {
		/** Funcao que adiciona um territorio em um continente. */
		for (int i = 0;i<this.territorios.size(); i++){
			if (this.territorios.get(i) == t){
				return;
			}
		}
		territorios.add(t);
	}

	public boolean pertence(Jogador j) {
		/** Funcao que retorna se um jogador e dono de todos os paises de um continente. */
		for (Territorio t : territorios) {
			if (t.getDono() != j)
				return false;
		}
		return true;
	}

	// imprime o continente
	public void exibe() {
		/** Funcao que exibe os dados de um continente: nome, exercitos adicionais e seus paises. */
		System.out.println("\n\nContinente: " + this.nome);
		System.out.println("Numero de exercitos adicionais:" + Integer.toString(this.numExeAdicionais));
		System.out.print("Paises: ");
		for (Territorio pais : this.territorios) {
			System.out.println(pais.getNome());
		}
	}

	public static Continente[] getContinentes(){
		return continentes.values().toArray(new Continente[continentes.size()]);
	}

}
