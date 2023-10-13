package Model;

import java.util.ArrayList;
import java.util.Hashtable;

class Continente {
	private static Hashtable<String, Continente> continentes = new Hashtable<String, Continente>();

	private ArrayList<Territorio> paises = new ArrayList<>();
	private String nome;
	private int numExeAdicionais;

	public Continente(String nome, int nExe) {
		this.nome = nome;
		numExeAdicionais = nExe;
		continentes.put(nome, this);
	}

	public int getNumExeAdicionais() {
		return this.numExeAdicionais;
	}

	public ArrayList<Territorio> getPaises() {
		return this.paises;
	}

	public void addTerritorio(Territorio t) {
		paises.add(t);
	}

	public boolean pertence(Jogador j) {
		for (Territorio t : paises) {
			if (t.getDono() != j)
				return false;
		}
		return true;
	}

	// imprime o continente
	public void exibe() {
		System.out.println("\n\nNome: " + this.nome);
		System.out.println("Numero de exercitos adicionais:" + Integer.toString(this.numExeAdicionais));
		System.out.print("Paises: ");
		for (Territorio pais : this.paises) {
			System.out.println(pais.getNome());
		}
	}

	public static Continente getContinente(String nome) {
		return continentes.get(nome);
	}

}
