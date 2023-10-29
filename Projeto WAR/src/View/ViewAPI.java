package View;

import java.awt.*;
import Model.ModelAPI;
import Controller.ControllerAPI;

public class ViewAPI {
    private static ViewAPI instance;
    private ControllerAPI control;
    private ModelAPI model;
    private int etapa;
    private String []territorios;
    private String []vizinhos;
    private String selecionado;
    private int corAtual;
    
    private final Color [] cores = {Color.YELLOW, Color.BLUE, Color.WHITE, Color.BLACK, Color.GREEN, Color.RED};
	
    
    
    private GameScreen gameScreen;
    
    private ViewAPI() {
    }

    public static ViewAPI getInstance(){
        if(instance == null){
            instance = new ViewAPI();
        }

    	instance.control = ControllerAPI.getInstance();
    	instance.model = ModelAPI.getInstance();
        return instance;
    }

    public void inicializaGameScreen(){
        gameScreen = new GameScreen();
        
        gameScreen.setVisible(true);
        
        Territorio []lst = Territorio.getTerritorios();
        for (Territorio t: lst) {
        	int n = model.getQtdExercitos(t.getNome());
        	t.setNum(n);
        }
    }
    
    
    
    public Color setViewColor(String territorio) {
    	int i = model.getCor(territorio);
    	if (i==-1) return null;
    	return cores[i];
    }
    
//    public int setViewQtdExe(String territorio) {
//    	return model.getQtdExercitos(territorio);
//    }
    
//    public void drawString(String menssagem) {
//    	gameScreen.drawString(menssagem);
//    	model.getQtdExercitos(territorio)
//    }
    
    
    public void setEtapa(int etapa, String[] territorios, int cor, int qtd) {
//    	Territorio t;
    	if (this.territorios!=null) {
    		for (String nome: this.territorios) {
    			atualizaTerritorio(nome,false);
    		}
    		
    	}
    	
    	this.territorios = territorios;
    	this.etapa = etapa;
    	this.corAtual = cor;
//    	Territorio t;
    	if (this.territorios!=null) {
	    	for (String nome: territorios) {
	    		atualizaTerritorio(nome,true);
	//    		t = Territorio.getTerritorio(nome);
	//    		t.setClicavel(true);
	    	}
    	}
    }
    
    private void atualizaTerritorio(String territorio, boolean click) {
    	

		int n = model.getQtdExercitos(territorio);
		Territorio t = Territorio.getTerritorio(territorio);
		t.setNum(n);
		int cor = model.getCor(territorio);
		t.setCor(cores[cor]);
		t.setClicavel(click);
	
    }
    
    
    public void click(String territorio) {
    	System.out.printf("Etapa %d\n", etapa);

    	Territorio t;
		
    	if(territorios != null)
	    	for (String nome: this.territorios) {
	    		atualizaTerritorio(nome,false);
	    	}
    	if (vizinhos != null)
	    	for (String nome: this.vizinhos) {
	    		atualizaTerritorio(nome,false);
    	}
    	
//    	if (territorio == null) {
//    		if (selecionado != null) {
//        		atualizaTerritorio(selecionado, false);
//        		
//        		t = Territorio.getTerritorio(selecionado);
//        		t.setMarcado(true);
//    		}
//    		selecionado = null;
//    	}
//    	
    	
    	if (etapa == 0) {
    		if (territorio == null) {
    			if (selecionado != null) {
	    			atualizaTerritorio(selecionado, false);
	    			t = Territorio.getTerritorio(selecionado);
	    			t.setMarcado(false);
	    			selecionado = null;
    			}
    			
    			if (territorios!=null) {
    		    	for (String nome: territorios) {
    		    		atualizaTerritorio(nome,true);
    		    	}
    	    	}
    			
    			return;
    		}
    		selecionado = territorio;
    		atualizaTerritorio(selecionado, true);
    		
    		t = Territorio.getTerritorio(selecionado);
//    		t.setClicavel(true);
    		t.setMarcado(true);
    		
    		vizinhos = model.getVizinhos(selecionado);
    		for (String nome: vizinhos) {
        		t = Territorio.getTerritorio(nome);
        		if (corAtual != model.getCor(nome))
        			t.setClicavel(true);
        	}
    		etapa = 1;
    	}
    	else if (etapa == 1) {
    		if (territorio == null) {
				if (selecionado != null) {
        			atualizaTerritorio(selecionado, false);
        			t = Territorio.getTerritorio(selecionado);
        			t.setMarcado(false);
        			selecionado = null;
				}
    			if (territorios!=null) {
    		    	for (String nome: territorios) {
    		    		atualizaTerritorio(nome,true);
    		    	}
    	    	}
    			
    			etapa = 0;
    			return;
    		}

    	    	
    		
    		t = Territorio.getTerritorio(territorio);
    		if (corAtual == model.getCor(territorio)){
    			t.setMarcado(false);
//    			Territorio t;
    	    	for (String nome: territorios) {
    	    		atualizaTerritorio(nome, true);
//    	    		t = Territorio.getTerritorio(nome);
//    	    		t.setClicavel(true);
    	    	}
    	    	
    	    	selecionado = null;
    	    	etapa = 0;
    		}
    		else {
    			control.ataca(selecionado,territorio);
//    			int n;
//    			atualizaTerritorio(selecionado);
//    			atualizaTerritorio(territorio);
//    			n = model.getQtdExercitos(selecionado);
//    			Territorio.getTerritorio(selecionado).setNum(n);
//    			n = model.getQtdExercitos(territorio);
//    			Territorio.getTerritorio(territorio).setNum(n);
    			
    			etapa = 0;
    			click(selecionado);
    		}
//    			t.setClicavel(true);
    	}
    	
    }
    
}

// package View;

// public class Main {

// 	public static void main(String[] args) {
// 		GameScreen f = new GameScreen();
// 		f.setVisible(true);

// 	}

// }