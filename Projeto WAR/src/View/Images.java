package View;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/* Classe responsável por carregar todas as imagens do jogo. */
class Images {
	private static Images instance;
	// Hashtable contendo os nomes das imagens e os objetos de imagem:
	private Hashtable<String, Image> imagensHashtable = new Hashtable<String, Image>();

	private Images() {
		/**
		 * Construtor que cria uma pasta de imagens e insere os nomes e objetos na
		 * hashtable.
		 */
		File pasta = new File("images");

		File[] imagens = pasta.listFiles();

		for (File imagem : imagens) {
			try {
				imagensHashtable.put(imagem.getName(), ImageIO.read(imagem));
				// System.out.printf("Imagem '%s' carregada\n",imagem.getName());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public static Images getInstance() {
		/** Metodo que retorna a unica instancia de Images. */
		if (instance == null) {
			instance = new Images();
		}
		return instance;
	}

	public Image getImage(String nomeImg) {
		/** Metodo que retorna o objeto de uma imagem dado o nome da imagem. */
		Image img = imagensHashtable.get(nomeImg);
		if (img == null) {
			System.out.printf("Erro ao encontrar imagem '%s'\n", nomeImg);
		}
		return img;
	}

}
