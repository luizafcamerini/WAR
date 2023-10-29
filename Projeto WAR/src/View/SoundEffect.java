package View;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class SoundEffect {
    public static void play(String filename) {
        try {
            // Carrega o arquivo de áudio
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename).getAbsoluteFile());

            // Cria um clip de áudio
            Clip clip = AudioSystem.getClip();

            // Abre o arquivo de áudio no clip
            clip.open(audioInputStream);

            // Toca o som
            clip.start();
        } catch(Exception ex) {
            System.out.println("Erro ao tocar o som.");
            ex.printStackTrace();
        }
    }
    
}