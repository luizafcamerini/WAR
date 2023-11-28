package Model;

import java.util.ArrayList;
import java.util.Hashtable;

class Continente {
	private static Hashtable<String, Continente> continentes = new Hashtable<String, Continente>();
	private ArrayList<Territorio> territorios = new ArrayList<>();
	private String nome;
	private int numExeAdicionais;

	public Continente(String nome, int nExe) {
		/**
		 * Construtor que cria um continente com seu nome e numero de exercitos
		 * adicionais e o coloca na hashtable.
		 */
		this.nome = nome;
		numExeAdicionais = nExe;
		continentes.put(nome, this);
	}

	public String getNome() {
		/** Metodo que retorna o nome do continente */
		return this.nome;
	}

	public Territorio[] getTerritorios() {
		/** Metodo que retorna uma copia da lista de territorios de um continente. */
		return territorios.toArray(new Territorio[territorios.size()]);
	}

	public int getNumExeAdicionais() {
		/**
		 * Metodo que retorna o numero de exercitos adicionais de um continente
		 * dominado.
		 */
		return this.numExeAdicionais;
	}

	public void addTerritorio(Territorio t) {
		/** Metodo que adiciona um territorio em um continente. */
		if (territorios.contains(t))
			return;
		territorios.add(t);
	}

	public boolean pertence(Jogador j) {
		/**
		 * Metodo que retorna se um jogador e dono de todos os paises de um continente.
		 */
		for (Territorio t : territorios) {
			if (t.getDono() != j)
				return false;
		}
		return true;
	}

	public void exibe() {
		/**
		 * Metodo que exibe os dados de um continente: nome, exercitos adicionais e seus
		 * paises.
		 */
		System.out.println("\n\nContinente: " + this.nome);
		System.out.println("Numero de exercitos adicionais:" + Integer.toString(this.numExeAdicionais));
		System.out.print("Paises: ");
		for (Territorio pais : this.territorios) {
			System.out.println(pais.getNome());
		}
	}

	public static Continente getContinente(String nome) {
		/** Metodo que retorna um continente pelo seu nome. */
		return continentes.get(nome);
	}

	public static Continente[] getContinentes() {
		/** Metodo que retorna a lista de continentes a partir da hashtable. */
		return continentes.values().toArray(new Continente[continentes.size()]);
	}

}
