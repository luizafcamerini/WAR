package View;
import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.Hashtable;

public class Territorio {
	private static Hashtable<String, Territorio> territorios;
	
    private String nome;
	private int num = 654;
    private int x, y;
    private Color cor;
    private int raio = 12;
    private Color cor2, cor3, cor4;
	boolean marcado = false;
	boolean ocuto = false;
	

    public Territorio(String _nome, int _x, int _y){
        nome = _nome;
        x = _x;
        y = _y;
    }

    public void setCor(Color _cor){
        cor = _cor;

		int red = this.cor.getRed();
        int green = this.cor.getGreen();
        int blue = this.cor.getBlue();
        
        // Converte RGB para HSB
        float[] hsb = Color.RGBtoHSB(red, green, blue, null);

        // Adiciona 0.5 (180 graus no círculo cromático) ao valor de Hue
        hsb[0] = (hsb[0] + 0.5f) % 1;

        // Converte HSB de volta para RGB
        cor3 = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));

		red = 255 - this.cor.getRed();
        green = 255 - this.cor.getGreen();
        blue = 255 - this.cor.getBlue();
        
        cor2 = new Color(red, green, blue);
        
        int d = 100;
        
        red = (this.cor.getRed() > 128) ? this.cor.getRed() - d : this.cor.getRed() + d;
        green = (this.cor.getGreen() > 128) ? this.cor.getGreen() - d : this.cor.getGreen() + d;
        blue = (this.cor.getBlue() > 128) ? this.cor.getBlue() - d : this.cor.getBlue() + d;

        cor4 = new Color(red, green, blue);
    }
    
    public String getNome() {
    	return nome;
    }

	public void setCoord(int _x, int _y){
        x = _x;
        y = _y;
    }

	public void marca(boolean b){
		marcado = b;
	}

	public void ocuta(boolean b){
		ocuto = b;
	}


    public void draw(Graphics g){
        Graphics2D g2d=(Graphics2D) g;
		if (!ocuto)
	        g2d.setPaint(cor);
		else
			g2d.setPaint(cor4);
        Ellipse2D circ= new Ellipse2D.Double(x-raio,y-raio,raio*2,raio*2);
		g2d.fill(circ);
		
		String txt = Integer.toString(num);
        FontMetrics fm = g.getFontMetrics();
		// Centraliza o texto no círculo
        double textWidth = fm.getStringBounds(txt, g).getWidth();
        double textHeight = fm.getStringBounds(txt, g).getHeight();
        int _x = (int) (x - textWidth / 2);
        int _y = (int) (y - textHeight / 2 + fm.getAscent());

		// Define a cor da borda
		g2d.setColor(cor2);
		// Define a espessura da borda
		g2d.setStroke(new BasicStroke(1));
		g2d.draw(circ);

		if (marcado){
			int espessuraBorda = 5;
			// Desenha a borda
			Ellipse2D borda = new Ellipse2D.Double(x-raio-espessuraBorda, y-raio-espessuraBorda, (raio+espessuraBorda)*2, (raio+espessuraBorda)*2);
			g2d.setColor(cor2);
			g2d.setStroke(new BasicStroke(espessuraBorda));
			g2d.draw(borda);
		}

		g2d.setPaint(cor2);
		g.drawString(txt,_x,_y);
    }
    
    public boolean estaEm(int _x, int _y) {
    	int dx = Math.abs(x - _x);
    	int dy = Math.abs(y - _y);
    	int r = (int)Math.sqrt(dx*dx+dy*dy);
    	return r <= raio;
    }
    
    
    public static Territorio[] getTerritorios() {
    	if (territorios == null) {
			territorios = new Hashtable<String, Territorio>();
    		String txtTerritorios = """
				Espanha,525,215
				Polônia,660,140
				Síria,760,225
				Québec,350,125
				África do Sul,660,510
				Paquistão,845,260
				Alasca,150,75
				Indonésia,1000,445
				Índia,895,315
				Egito,650,330
				Nova Zelândia,1035,590
				Arábia Saudita,760,350
				Calgary,235,80
				China,895,230
				Jordânia,710,290
				Japão,1060,210
				Nigéria,595,370
				Austrália,975,555
				Perth,915,540
				Groelândia,395,40
				Argentina,340,505
				Vancouver,220,125
				Cazaquistão,945,150
				Itália,610,180
				França,560,185
				Angola,640,445
				México,200,300
				Venezuela,260,370
				Mongolia,960,195
				Argélia,535,310
				Somalia,705,425
				Romênia,660,195
				Letônia,750,130
				Texas,230,195
				Peru,300,435
				Suécia,610,80
				Sibéria,995,75
				Coréia do Norte,970,245
				Coréia do Sul,970,275
				Tailândia,1000,300
				Bangladesh,950,320
				Reino Unido,545,130
				Ucrânia,680,170
				Califórnia,170,200
				Nova York,270,205
				Russia,885,95
				Estônia,770,80
				Brasil,350,400
				Turquia,825,185
				Iraque,770,280
				Irã,810,285
    				""";

				String nome;
				Territorio territorio;

				String[] linhasTerritorios = txtTerritorios.split("\n");
				String[] strListTemp;
				int x,y;

				// Cria os territórios
				for (String linha : linhasTerritorios) {
					linha = linha.trim();
					// System.out.println(linha);

					strListTemp = linha.split(",");
					if (strListTemp.length ==3) { // Verifica que esta lendo uma linha com território
						nome = strListTemp[0];
						System.out.println(nome);
						x = Integer.parseInt(strListTemp[1]);
						y = Integer.parseInt(strListTemp[2]);
						territorio = new Territorio(nome, x+10, y);
						if (territorios.size()%6 == 0)
							territorio.setCor(Color.BLACK);
						else if (territorios.size()%6 == 1)
							territorio.setCor(Color.RED);
						else if (territorios.size()%6 == 2)
							territorio.setCor(Color.YELLOW);
						else if (territorios.size()%6 == 3)
							territorio.setCor(Color.WHITE);
						else if (territorios.size()%6 == 4)
							territorio.setCor(Color.GREEN);
						else
							territorio.setCor(Color.BLUE);

						territorios.put(nome, territorio);
				}
		}
    		
    	}
    	
    	return territorios.values().toArray(new Territorio[territorios.size()]);
    }

}
