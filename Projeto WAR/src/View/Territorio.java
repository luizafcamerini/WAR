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
    private int x, y;
    private Color cor;
    private int raio = 12;

    public Territorio(String _nome, int _x, int _y){
        nome = _nome;
        x = _x;
        y = _y;
    }

    public void setCor(Color _cor){
        cor = _cor;
    }
    
    public String getNome() {
    	return nome;
    }

	public void setCoord(int _x, int _y){
        x = _x;
        y = _y;
    }

    public void draw(Graphics g){
        Graphics2D g2d=(Graphics2D) g;
        g2d.setPaint(cor);
        Ellipse2D circ= new Ellipse2D.Double(x-raio,y-raio,raio*2,raio*2);
        g2d.fill(circ);
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
				Romênia,670,195
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
						territorio = new Territorio(nome, x, y);
						territorio.cor = Color.BLACK;
						territorios.put(nome, territorio);
				}
		}
    		
    	}
    	
    	return territorios.values().toArray(new Territorio[territorios.size()]);
    }

}
