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
	private static Hashtable<String, String> imgTerritorios;

	
    private String nome;
	private int num = 0;
    private int x, y;
    private Color cor;
    private int raio = 12;
    private Color cor2, cor3, cor4;
	boolean marcado = false;
	boolean ocuto = false;
	boolean clicavel = false;
	

    public Territorio(String _nome, int _x, int _y){
        nome = _nome;
        x = _x;
        y = _y;
    }

    public void setCor(Color _cor){
        cor = _cor;
        if (cor == null) return;

		int red = this.cor.getRed();
        int green = this.cor.getGreen();
        int blue = this.cor.getBlue();
//        
//        // Converte RGB para HSB
//        float[] hsb = Color.RGBtoHSB(red, green, blue, null);
//
//        // Adiciona 0.5 (180 graus no círculo cromático) ao valor de Hue
//        hsb[0] = (hsb[0] + 0.5f) % 1;

//        // Converte HSB de volta para RGB
//        cor3 = new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
        
//        cor3 = new Color(red,green,blue, 150);
        cor3 = new Color(128,128,128,128);

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

    public void setNum(int n) {
    	num = n;
    }
    
	public void setCoord(int _x, int _y){
        x = _x;
        y = _y;
    }

	public void setMarcado(boolean b){
		marcado = b;
	}

	public void setOculto(boolean b){
		ocuto = b;
	}
	
	public void setClicavel(boolean b){
		clicavel = b;
	}

	

    public boolean estaEm(int _x, int _y) {
    	if (!clicavel) return false;
    	int dx = Math.abs(x - _x);
    	int dy = Math.abs(y - _y);
    	int r = (int)Math.sqrt(dx*dx+dy*dy);
    	return r <= raio;
    }
	
    public void draw(Graphics g){
        Graphics2D g2d=(Graphics2D) g;
		if (!ocuto)
	        g2d.setPaint(cor);
//		else if (!clicavel)
//			g2d.setPaint(cor3);
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
		
		if (!clicavel) {
			Ellipse2D circ2= new Ellipse2D.Double(x-raio,y-raio,raio*2,raio*2);
			g2d.setColor(cor3);
			g2d.fill(circ2);
		}
		

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
    
    public static Territorio getTerritorio(String nome) {
    	return territorios.get(nome);
    }

	public static String getImgTerritorio(String nome) {
    	return imgTerritorios.get(nome);
    }
    
    public static Territorio[] getTerritorios() {
    	if (territorios == null) {
			territorios = new Hashtable<String, Territorio>();
			imgTerritorios = new Hashtable<String, String>();
    		String txtTerritorios = """
				Espanha,525,215,war_carta_eu_espanha.png
				Polônia,660,140,war_carta_eu_polonia.png
				Síria,760,225,war_carta_as_siria.png
				Québec,350,125,war_carta_an_quebec.png
				África do Sul,660,510,war_carta_af_africadosul.png
				Paquistão,845,260,war_carta_as_paquistao.png
				Alasca,150,75,war_carta_an_alasca.png
				Indonésia,1000,445,war_carta_oc_indonesia.png
				Índia,895,315,war_carta_as_india.png
				Egito,650,330,war_carta_af_egito.png
				Nova Zelândia,1035,590,war_carta_oc_novazelandia.png
				Arábia Saudita,760,350,war_carta_as_arabiasaudita.png
				Calgary,235,80,war_carta_an_calgary.png
				China,895,230,war_carta_as_china.png
				Jordânia,710,290,war_carta_as_jordania.png
				Japão,1060,210,war_carta_as_japao.png
				Nigéria,595,370,war_carta_af_nigeria.png
				Austrália,975,555,war_carta_oc_australia.png
				Perth,915,540,war_carta_oc_perth.png
				Groelândia,395,40,war_carta_an_groelandia.png
				Argentina,340,505,war_carta_sa_argentina.png
				Vancouver,220,125,war_carta_an_vancouver.png
				Cazaquistão,945,150,war_carta_as_cazaquistao.png
				Itália,610,180,war_carta_eu_italia.png
				França,560,185,war_carta_eu_franca.png
				Angola,640,445,war_carta_af_angola.png
				México,200,300,war_carta_na_mexico.png
				Venezuela,260,370,war_carta_sa_venezuela.png
				Mongolia,960,195,war_carta_as_mongolia.png
				Argélia,535,310,war_carta_af_argelia.png
				Somalia,705,425,war_carta_af_somalia.png
				Romênia,660,195,war_carta_eu_romenia.png
				Letônia,750,130,war_carta_eu_letonia.png
				Texas,230,195,war_carta_na_texas.png
				Peru,300,435,war_carta_sa_peru.png
				Suécia,610,80,war_carta_eu_suecia.png
				Sibéria,995,75,war_carta_as_siberia.png
				Coréia do Norte,970,245,war_carta_as_coreiadonorte.png
				Coréia do Sul,970,275,war_carta_as_coreiadosul.png
				Tailândia,1000,300,war_carta_as_tailandia.png
				Bangladesh,950,320,war_carta_as_bangladesh.png 
				Reino Unido,545,130,war_carta_eu_reinounido.png 
				Ucrânia,680,170,war_carta_eu_ucrania.png
				Califórnia,170,200,war_carta_na_california.png 
				Nova York,270,205,war_carta_na_novayork.png
				Russia,885,95,war_carta_as_russia.png
				Estônia,770,80,war_carta_eu_estonia.png 
				Brasil,350,400,war_carta_asl_brasil.png
				Turquia,825,185,war_carta_as_turquia.png
				Iraque,770,280, war_carta_as_iraque.png
				Irã,810,285,war_carta_as_ira.png
    				""";

				String nome;
				Territorio territorio;

				String[] linhasTerritorios = txtTerritorios.split("\n");
				String[] strListTemp;
				int x,y;

				ViewAPI api = ViewAPI.getInstance();
				
				// Cria os territórios
				for (String linha : linhasTerritorios) {
					linha = linha.trim();
					// System.out.println(linha);

					strListTemp = linha.split(",");
					if (strListTemp.length ==4) { // Verifica que esta lendo uma linha com território
						nome = strListTemp[0];
//						System.out.println(nome);
						x = Integer.parseInt(strListTemp[1]);
						y = Integer.parseInt(strListTemp[2]);
						territorio = new Territorio(nome, x+10, y);
						Color c = api.setViewColor(nome);
						territorio.setCor(c);

						territorios.put(nome, territorio);
						imgTerritorios.put(nome, strListTemp[3]);
				}
		}
    		
    	}
    	
    	return territorios.values().toArray(new Territorio[territorios.size()]);
    }

}
