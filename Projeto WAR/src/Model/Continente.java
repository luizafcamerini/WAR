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
	
	public static Continente getContinente(String nome) {
		return continentes.get(nome);
	}

	public ArrayList<Territorio> getPaises() {
		return this.paises;
	}

	public int getNumExeAdicionais() {
		return this.numExeAdicionais;
	}

	public void addTerritorio(Territorio t) {
		for (int i = 0;i<this.paises.size(); i++){
			if (this.paises.get(i) == t){
				return;
			}
		}
		paises.add(t);
	}

	public boolean pertence(Jogador j) {
		/** Funcao que retorna se um jogador e dono de todos os paises de um continente. */
		for (Territorio t : paises) {
			if (t.getDono() != j)
				return false;
		}
		return true;
	}

	// imprime o continente
	public void exibe() {
		/** Funcao que  */
		System.out.println("\n\nContinente: " + this.nome);
		System.out.println("Numero de exercitos adicionais:" + Integer.toString(this.numExeAdicionais));
		System.out.print("Paises: ");
		for (Territorio pais : this.paises) {
			System.out.println(pais.getNome());
		}
	}


}
