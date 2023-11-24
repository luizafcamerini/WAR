package Model;

abstract class Objetivo {
	protected String descricao;
	protected Jogador dono;
	protected String imgName;
	private static Objetivo[] lst;

	abstract public boolean verifica();

	public static Objetivo setAlvo(Cores cor, Jogador alvo) {
		int i=0;
		switch (cor) {
			case AZUL:
				i = 0;
				Objetivo1 o1 = (Objetivo1) lst[0];
				o1.setJogadorAlvo(alvo);
				break;
			case AMARELO:
				i = 1;
				Objetivo2 o2 = (Objetivo2) lst[1];
				o2.setJogadorAlvo(alvo);
				break;
			case VERMELHO:
				i = 2;
				Objetivo3 o3 = (Objetivo3) lst[2];
				o3.setJogadorAlvo(alvo);
				break;
			case PRETO:
				i = 3;
				Objetivo4 o4 = (Objetivo4) lst[3];
				o4.setJogadorAlvo(alvo);
				break;
			case BRANCO:
				i = 4;
				Objetivo5 o5 = (Objetivo5) lst[4];
				o5.setJogadorAlvo(alvo);
				break;
			case VERDE:
				i = 5;
				Objetivo6 o6 = (Objetivo6) lst[5];
				o6.setJogadorAlvo(alvo);
				break;
		}
		return lst[i];
	}

	public static Objetivo getObjetivo(int i) {
		return lst[i];
	}

	public static void criaObjetivos() {
		if (lst != null) return;

		lst = new Objetivo[14];
		lst[0] = new Objetivo1();
		lst[1] = new Objetivo2();
		lst[2] = new Objetivo3();
		lst[3] = new Objetivo4();
		lst[4] = new Objetivo5();
		lst[5] = new Objetivo6();
		lst[6] = new Objetivo7();
		lst[7] = new Objetivo8();
		lst[8] = new Objetivo9();
		lst[9] = new Objetivo10();
		lst[10] = new Objetivo11();
		lst[11] = new Objetivo12();
		lst[12] = new Objetivo13();
		lst[13] = new Objetivo14();
	}

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
		if (lst == null){
			criaObjetivos();
		}
		Baralho<Objetivo> objetivos = new Baralho<Objetivo>();

		for (int i = 6; i < 14; i++){
			objetivos.adiciona(lst[i]);
		}

		// objetivos.adiciona(new Objetivo7());
		// objetivos.adiciona(new Objetivo8());
		// objetivos.adiciona(new Objetivo9());
		// objetivos.adiciona(new Objetivo10());
		// objetivos.adiciona(new Objetivo11());
		// objetivos.adiciona(new Objetivo12());
		// objetivos.adiciona(new Objetivo13());
		// objetivos.adiciona(new Objetivo14());

		return objetivos;
	}

}
