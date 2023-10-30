package View;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

// Classe responsável por carregar todas as imagens do jogo
class Images {
    private static Images instance;
    private Hashtable<String, Image> imagensHashtable = new Hashtable<String, Image>();

    private Images(){
        File pasta = new File("src/View/images");

        File[] imagens = pasta.listFiles();

        for(File imagem : imagens){
            try {
                imagensHashtable.put(imagem.getName(), ImageIO.read(imagem));
                System.out.println(imagem.getName());
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }

    public static Images getInstance(){
        if(instance == null){
            instance = new Images();
        }
        return instance;
    }

    public Image getImage(String chave){
        return imagensHashtable.get(chave);
    }

}
