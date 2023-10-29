package View;
import javax.swing.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.Hashtable;

public class Territorio {
	private static Hashtable<String, Territorio> territorios = new Hashtable<String, Territorio>();
	
    private String nome;
    private int x, y;
    private Color cor;
    private int raio = 25;

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

    public void draw(Graphics g){
        Graphics2D g2d=(Graphics2D) g;
        Ellipse2D circ= new Ellipse2D.Double(x,y,raio,raio);
        g2d.setPaint(cor);
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
    		String txtTerritorios = """
    				Brasil,
    				Argentina,
    				Peru,
    				Venezuela,

    				Alasca,
    				Calgary,
    				Califórnia,
    				Groelândia,
    				México,
    				Nova York,
    				Québec,
    				Texas,
    				Vancouver,

    				África do Sul,
    				Angola,
    				Argélia,África,circulo,Egito,Nigéria,Espanha,Itália
    				Egito,África,triangulo,Argélia,Nigéria,Somalia,Romênia,Jordânia
    				Nigéria,África,circulo,Angola,Somalia,Egito,Argélia,Brasil
    				Somalia,África,quadrado,África do Sul,Angola,Egito,Nigéria,Arábia Saudita

    				Arábia Saudita,Ásia,circulo,Jordânia,Iraque,Somalia
    				Bangladesh,Ásia,circulo,Índia,Coréia do Sul,Tailândia,Indonésia
    				Cazaquistão,Ásia,circulo,Sibéria,Russia,Letônia,Turquia,China,Mongolia,Japão
    				China,Ásia,quadrado,Cazaquistão,Mongolia,Coréia do Norte,Coréia do Sul,Índia,Paquistão,Turquia
    				Coréia do Norte,Ásia,quadrado,Coréia do Sul,China,Japão
    				Coréia do Sul,Ásia,triangulo,China,Índia,Coréia do Norte,Tailândia,Bangladesh
    				Estônia,Ásia,circulo,Suécia,Letônia,Russia
    				Índia,Ásia,triangulo,Paquistão,China,Coréia do Sul,Bangladesh,Indonésia
    				Irã,Ásia,quadrado,Iraque,Síria,Paquistão
    				Iraque,Ásia,triangulo,Arábia Saudita,Jordânia,Síria,Irã
    				Japão,Ásia,circulo,Coréia do Norte,Mongolia,Cazaquistão
    				Jordânia,Ásia,quadrado,Arábia Saudita,Iraque,Síria,Egito
    				Letônia,Ásia,quadrado,Estônia,Russia,Cazaquistão,Polônia,Ucrânia,Suécia,Turquia
    				Mongolia,Ásia,triangulo,Cazaquistão,China,Japão
    				Paquistão,Ásia,circulo,Turquia,Síria,Irã,China,Índia
    				Russia,Ásia,triangulo,Estônia,Letônia,Cazaquistão,Sibéria
    				Sibéria,Ásia,quadrado,Alasca,Russia,Cazaquistão
    				Síria,Ásia,quadrado,Jordânia,Iraque,Irã,Paquistão,Turquia
    				Tailândia,Ásia,triangulo,Bangladesh,Coréia do Sul
    				Turquia,Ásia,triangulo,Letônia,Cazaquistão,China,Paquistão,Síria,Ucrânia

    				Espanha,Europa,circulo,França,Argélia
    				França,Europa,triangulo,Espanha,Reino Unido,Itália,Suécia
    				Itália,Europa,quadrado,França,Polônia,Romênia,Argélia,Suécia
    				Polônia,Europa,triangulo,Itália,Letônia,Romênia,Ucrânia
    				Reino Unido,Europa,circulo,França,Groelândia
    				Romênia,Europa,triangulo,Itália,Polônia,Ucrânia,Egito
    				Suécia,Europa,quadrado,Estônia,Letônia,França,Itália
    				Ucrânia,Europa,circulo,Polônia,Letônia,Turquia,Romênia

    				Austrália,Oceania,triangulo,Perth,Nova Zelândia,Indonésia
    				Indonésia,Oceania,triangulo,Índia,Bangladesh,Austrália,Nova Zelândia
    				Nova Zelândia,Oceania,quadrado,Austrália,Indonésia
    				Perth,Oceania,circulo,Austrália

    				""";
    		
    		
    		
    		
    	}
    	
    	return territorios.values().toArray(new Territorio[territorios.size()]);
    }

}
