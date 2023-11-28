package View;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.font.FontRenderContext;
import java.util.List;

import Observer.ObservadoIF;
import Observer.ObservadorIF;

class Territorio implements ObservadoIF, ObservadorIF, MouseListener, MouseMotionListener {
	private static Hashtable<String, Territorio> territorios;
	private static Hashtable<String, String> imgTerritorios;

	private String nome;
	private int num = 0; // Quantidade de exércitos neste território
	private int x, y;
	private int xName, yName;
	private Color cor, cor2, cor3, cor4;
	private Font font1, font2;
	private int raio = 12;
	private boolean marcado = false;
	private boolean oculto = false;
	private boolean clicavel = false;
	
	private int idTerritorio = -1;
	private int acaoMouse;
	private final int TERRITORIO_ALTERADO = -1;
	private final int CLICK_TERRITORIO = 0;
	private final int ENTROU_TERRITORIO = 1;
	private final int SAIU_TERRITORIO = 2;

	private List<ObservadorIF> lst = new ArrayList<ObservadorIF>();

	public void addObservador(ObservadorIF o) {
		lst.add(o);
	}

	public void removeObservador(ObservadorIF o) {
		lst.remove(o);
	}

	public int get(int i) {
		if (i == 1)
			return acaoMouse;
		else if (i == 2)
			return idTerritorio;
		return -1;
	}

	public void notify(ObservadoIF o) {
		int n = o.get(1);
		int c = o.get(2);

		setNum(n);

		Color cor = ViewAPI.getInstance().int2color(c);
		setCor(cor);

		acaoMouse = TERRITORIO_ALTERADO;

		notificaObservadores();
	}

	public void setIDTerritorio(int id) {
		this.idTerritorio = id;
	}

	public Territorio(String _nome, int _x, int _y, int _xName, int _yName) {
		nome = _nome;
		x = _x;
		y = _y;
		xName = _xName;
		yName = _yName;
		font1 = new Font(null);
		font2 = new Font("Arial", Font.BOLD, 14);
	}

	public void setCor(Color _cor) {
		cor = _cor;
		if (cor == null)
			return;

		int red = this.cor.getRed();
		int green = this.cor.getGreen();
		int blue = this.cor.getBlue();
		cor3 = new Color(128, 128, 128, 128);

		red = this.cor.getRed();
		green = this.cor.getGreen();
		blue = this.cor.getBlue();

		// double luminance = (0.299 * red + 0.587 * green + 0.114 * blue) / 255;
		double luminance = (0.501 * red + 0.587 * green + 0.114 * blue) / 255;

		cor2 = luminance > 0.5 ? Color.BLACK : Color.WHITE;

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
		if (num == n)
			return;
		num = n;
	}

	public void setMarcado(boolean b) {
		if (marcado != b) {
			marcado = b;
			acaoMouse = TERRITORIO_ALTERADO;
			notificaObservadores();
		}
	}

	public void setOculto(boolean b) {
		oculto = b;
	}

	public void setClicavel(boolean b) {
		clicavel = b;
	}

	public boolean estaEm(int _x, int _y) {
		if (!clicavel)
			return false;
		int dx = Math.abs(x - _x);
		int dy = Math.abs(y - _y);
		int r = (int) Math.sqrt(dx * dx + dy * dy);
		return r <= raio;
	}

	public void draw(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g.setFont(font1);
		if (oculto && clicavel) {

			Font fonte = font2;
			g2d.setFont(fonte);
			FontRenderContext frc = g2d.getFontRenderContext();
			int x = xName - (int) fonte.getStringBounds(nome, frc).getWidth() / 2;
			int y = yName - (int) fonte.getStringBounds(nome, frc).getHeight() / 2;

			g.setColor(Color.BLACK);
			for (int i = -2; i <= 2; i++) {
				for (int j = -2; j <= 2; j++) {
					g.drawString(nome, x + i, y + j);
				}
			}
			g.setColor(Color.WHITE);
			g.drawString(nome,x,y);
			g2d.setPaint(cor4);
		} else {
			g2d.setPaint(cor);
		}

		Ellipse2D circ = new Ellipse2D.Double(x - raio, y - raio, raio * 2, raio * 2);
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
			Ellipse2D circ2 = new Ellipse2D.Double(x - raio, y - raio, raio * 2, raio * 2);
			g2d.setColor(cor3);
			g2d.fill(circ2);
		}

		if (marcado) {
			int espessuraBorda = 5;
			// Desenha a borda
			Ellipse2D borda = new Ellipse2D.Double(x - raio - espessuraBorda, y - raio - espessuraBorda,
					(raio + espessuraBorda) * 2, (raio + espessuraBorda) * 2);
			g2d.setColor(cor2);
			g2d.setStroke(new BasicStroke(espessuraBorda));
			g2d.draw(borda);
		}

		g2d.setPaint(cor2);
		g.drawString(txt, _x, _y);
	}

	public void draw_name(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Font fonte = font2;
		g2d.setFont(fonte);
		FontRenderContext frc = g2d.getFontRenderContext();
		int x = xName - (int) fonte.getStringBounds(nome, frc).getWidth() / 2;
		int y = yName - (int) fonte.getStringBounds(nome, frc).getHeight() / 2;

		g.setColor(Color.BLACK);
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				g.drawString(nome, x + i, y + j);
			}
		}
		g.setColor(Color.WHITE);
		g.drawString(nome,x,y);
	}

	public static Territorio getTerritorio(String nome) {
		return territorios.get(nome);
	}

	public static String getImgTerritorio(String nome) {
		if (nome == null || imgTerritorios.get(nome) == null) {
			return "war_carta_coringa.png";
		}
		return imgTerritorios.get(nome);
	}

	private void notificaObservadores() {
		for (ObservadorIF o : lst) {
			o.notify(this);
		}
	}

	public void mouseClicked(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (estaEm(x, y)) {
			acaoMouse = CLICK_TERRITORIO;
			notificaObservadores();
			// ViewAPI.getInstance().click(nome);
		}
	}

	public void mouseEntered(MouseEvent e) {
		// System.out.println("Mouse Entered");
	}

	public void mouseExited(MouseEvent e) {
		// System.out.println("Mouse Exited");
	}

	public void mousePressed(MouseEvent e) {
		// System.out.println("Mouse Pressed");
	}

	public void mouseReleased(MouseEvent e) {
		// System.out.println("Mouse Released");
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		if (!clicavel)
			return;

		if (estaEm(x, y) && !oculto) {
			setOculto(true);
			// System.out.print("Entrou\n");
			mouseEntered(e);
			acaoMouse = ENTROU_TERRITORIO;
			notificaObservadores();
		} else if (!estaEm(x, y) && oculto) {
			setOculto(false);
			// System.out.print("Saiu\n");
			mouseExited(e);
			acaoMouse = SAIU_TERRITORIO;
			notificaObservadores();
		}
	}

	public static Territorio[] getTerritorios() {
		if (territorios == null) {
			territorios = new Hashtable<String, Territorio>();
			imgTerritorios = new Hashtable<String, String>();
			String txtTerritorios = """
				Espanha,525,215,0,35,war_carta_eu_espanha.png
				Polônia,660,140,0,-10,war_carta_eu_polonia.png
				Síria,760,225,0,-10,war_carta_as_siria.png
				Québec,350,125,0,-10,war_carta_an_quebec.png
				África do Sul,660,510,0,-10,war_carta_af_africadosul.png
				Paquistão,845,260,0,-10,war_carta_as_paquistao.png
				Alasca,150,75,0,-10,war_carta_an_alasca.png
				Indonésia,1000,445,0,-10,war_carta_oc_indonesia.png
				Índia,895,315,0,-10,war_carta_as_india.png
				Egito,650,330,0,-10,war_carta_af_egito.png
				Nova Zelândia,1035,590,10,-10,war_carta_oc_novazelandia.png
				Arábia Saudita,760,350,0,-10,war_carta_as_arabiasaudita.png
				Calgary,235,80,0,-10,war_carta_an_calgary.png
				China,895,230,0,-10,war_carta_as_china.png
				Jordânia,710,290,0,-10,war_carta_as_jordania.png
				Japão,1060,210,0,-10,war_carta_as_japao.png
				Nigéria,595,370,0,-10,war_carta_af_nigeria.png
				Austrália,975,555,0,-10,war_carta_oc_australia.png
				Perth,915,540,0,-10,war_carta_oc_perth.png
				Groelândia,395,40,0,-10,war_carta_an_groelandia.png
				Argentina,340,505,0,-10,war_carta_asl_argentina.png
				Vancouver,220,135,0,-10,war_carta_an_vancouver.png
				Cazaquistão,945,150,0,-10,war_carta_as_cazaquistao.png
				Itália,610,180,0,-10,war_carta_eu_italia.png
				França,560,185,0,-10,war_carta_eu_franca.png
				Angola,640,445,0,-10,war_carta_af_angola.png
				México,200,300,0,-10,war_carta_an_mexico.png
				Venezuela,260,370,0,-10,war_carta_asl_venezuela.png
				Mongolia,960,195,0,-10,war_carta_as_mongolia.png
				Argélia,535,310,0,-10,war_carta_af_argelia.png
				Somalia,705,425,0,-10,war_carta_af_somalia.png
				Romênia,660,195,0,35,war_carta_eu_romenia.png
				Letônia,750,130,0,-10,war_carta_as_letonia.png
				Texas,225,195,0,-10,war_carta_an_texas.png
				Peru,300,435,0,-10,war_carta_asl_peru.png
				Suécia,610,80,0,-10,war_carta_eu_suecia.png
				Sibéria,995,75,0,-10,war_carta_as_siberia.png
				Coréia do Norte,970,245,70,15,war_carta_as_coreiadonorte.png
				Coréia do Sul,970,275,65,15,war_carta_as_coreiadosul.png
				Tailândia,1000,300,50,15,war_carta_as_tailandia.png
				Bangladesh,950,320,0,35,war_carta_as_bangladesh.png
				Reino Unido,545,130,0,-10,war_carta_eu_reinounido.png
				Ucrânia,680,170,40,15,war_carta_eu_ucrania.png
				Califórnia,170,200,-10,-10,war_carta_an_california.png
				Nova York,270,205,20,-10,war_carta_an_novayork.png
				Rússia,885,95,0,-10,war_carta_as_russia.png
				Estônia,770,80,0,-10,war_carta_as_estonia.png
				Brasil,350,400,0,-10,war_carta_asl_brasil.png
				Turquia,825,185,0,-10,war_carta_as_turquia.png
				Iraque,770,280,0,-10,war_carta_as_iraque.png
				Irã,810,285,0,-10,war_carta_as_ira.png
									""";

			String nome;
			Territorio territorio;

			String[] linhasTerritorios = txtTerritorios.split("\n");
			String[] strListTemp;
			int x, y, xName, yName;

			ViewAPI api = ViewAPI.getInstance();

			// Cria os territórios
			for (String linha : linhasTerritorios) {
				linha = linha.trim();
				// System.out.println(linha);

				strListTemp = linha.split(",");
				if (strListTemp.length == 6) { // Verifica que esta lendo uma linha com território
					nome = strListTemp[0];
					// System.out.println(nome);
					x = Integer.parseInt(strListTemp[1]);
					y = Integer.parseInt(strListTemp[2]);
					xName = x +10+ Integer.parseInt(strListTemp[3]);
					yName = y + Integer.parseInt(strListTemp[4]);
					territorio = new Territorio(nome, x + 10, y,xName,yName);
					Color c = api.getColor(nome);
					territorio.setCor(c);

					territorios.put(nome, territorio);
					imgTerritorios.put(nome, strListTemp[5]);
				}
			}

		}

		return territorios.values().toArray(new Territorio[territorios.size()]);
	}

}