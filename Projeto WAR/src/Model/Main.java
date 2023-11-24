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

		// try {
        //     BufferedImage img = ImageIO.read(new File("src/View/images/dado_ataque_5.png"));
		// 	int x = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getWidth() - img.getWidth()) / 2);
		// 	int y = (int) ((Toolkit.getDefaultToolkit().getScreenSize().getHeight() - img.getHeight()) / 2);
		// 	 // Criando um JLabel para exibir a imagem
		// 	 JLabel label = new JLabel(new ImageIcon(img));

		// 	 // Criando um JFrame para conter o JLabel
		// 	 JFrame frame = new JFrame();
		// 	 frame.setSize(1280, 720);
		// 	 frame.setLocation(x - 500, y - 400);
		// 	 frame.getContentPane().add(label, BorderLayout.CENTER);
		// 	//  frame.pack();
		// 	 frame.setVisible(true);
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

}


