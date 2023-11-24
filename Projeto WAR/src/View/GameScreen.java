package View;

import javax.swing.*;
import java.awt.*;

class GameScreen extends JFrame {
	private Container content;
	private static final int ALTURA_TELA = 700;
	private static final int LARGURA_TELA = 1200;
	// private GamePanel gP;
	// private InfoPainel iP;

	public GameScreen(GamePanel gP) {

		setTitle("War");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(LARGURA_TELA, ALTURA_TELA);
		setResizable(false);

		// Centraliza a janela na tela
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int sl = screenSize.width;
		int sa = screenSize.height;
		int x = sl / 2 - LARGURA_TELA / 2;
		int y = sa / 2 - ALTURA_TELA / 2;
		setBounds(x, y, LARGURA_TELA, ALTURA_TELA);

		content = getContentPane();
		content.add(gP);
	}

}