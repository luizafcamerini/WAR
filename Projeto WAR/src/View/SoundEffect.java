package View;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

class SoundEffect {
	Clip clip;

	SoundEffect(String filename) {
		try {
			// Carrega o arquivo de áudio
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filename).getAbsoluteFile());

			// Cria um clip de áudio
			clip = AudioSystem.getClip();

			// Abre o arquivo de áudio no clip
			clip.open(audioInputStream);
		} catch (Exception ex) {
			System.out.println("Erro ao carregar arquivo de som.");
			ex.printStackTrace();
		}
	}

	public void play() {
		// Reinicia o clip de áudio
		clip.setFramePosition(0);

		// Toca o som
		clip.start();
	}

}