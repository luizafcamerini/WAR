package Model;

import java.awt.Image;

abstract class Objetivo {
	String descricao;
	Jogador dono;
	String imgName;

	abstract public boolean verifica();

	public void defineDono(Jogador j) {
		dono = j;
	}

	public String getDescricao() {
		return descricao;
	}

	public String getImgName() {
		return imgName;
	}

	public static Baralho<Objetivo> montaBaralho() {
		Baralho<Objetivo> objetivos = new Baralho<Objetivo>();

		objetivos.adiciona(new Objetivo7());
		objetivos.adiciona(new Objetivo8());
		objetivos.adiciona(new Objetivo9());
		objetivos.adiciona(new Objetivo10());
		objetivos.adiciona(new Objetivo11());
		objetivos.adiciona(new Objetivo12());
		objetivos.adiciona(new Objetivo13());
		objetivos.adiciona(new Objetivo14());

		return objetivos;
	}

	public static Baralho<Objetivo> montaBaralhoCompleto() {
		Baralho<Objetivo> objetivos = new Baralho<Objetivo>();

		objetivos.adiciona(new Objetivo7());
		objetivos.adiciona(new Objetivo8());
		objetivos.adiciona(new Objetivo9());
		objetivos.adiciona(new Objetivo10());
		objetivos.adiciona(new Objetivo11());
		objetivos.adiciona(new Objetivo12());
		objetivos.adiciona(new Objetivo13());
		objetivos.adiciona(new Objetivo14());

		return objetivos;
	}
}
