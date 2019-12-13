/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

import java.util.Random;

/**
 *
 * @author Carolina
 */
public class Parque {

	private Random random = new Random();
	private Reloj reloj;
    private Shop elShop = new Shop();
    private Restaurante elRestaurante = new Restaurante();
    private MainSnorkel actSnorkel;
    private FaroMirador elFaroTobogan = new FaroMirador();
    private CarreraGomones laCarreraGomones = new CarreraGomones();
    private MundoAventura elMundoAventura = new MundoAventura();
    private NadoDelfines nadoDelfines;
    
    public Parque()
    {
    	reloj = new Reloj();
    	new Thread(reloj).start();
    	
    	//Lo inicialice aca porque no sabia bien si iba a funcionar si lo inicializa arriba con los otros
    	nadoDelfines = new NadoDelfines(reloj);
    }
    
    public void realizarCarreraGomones(Visitante unVisitante) {
    	this.laCarreraGomones.realizarCarreraGomones(unVisitante);
    }
    
    public void realizarMundoAventura(Visitante unVisitante)
    {
    	this.elMundoAventura.realizarMundoAventura(unVisitante);
    }
    
    public void realizarFaroMirador(Visitante unVisitante)
    {
    	this.elFaroTobogan.realizarFaroMirador(unVisitante);
    }
    
    public void realizarShop(Visitante unVisitante)
    {
    	this.elShop.realizarShop(unVisitante);
    }
    
    public void realizarNadoDelfines(Visitante unVisitante)
    {
    	this.nadoDelfines.realizarNadoDelfines(unVisitante);
    }
    
    public boolean estaAbierto()
    {
    	int hora = this.reloj.getHoraActual();
    	return (hora >= 9 && hora <= 17);
    }
    
    public Reloj getReloj()
    {
    	return reloj;
    }

	public void realizarActividades(Visitante unVisitante) {
		while(estaAbierto())
		{
			int numActividad = 2; //random.nextInt(7);
			
			switch(numActividad)
			{
			case 0: //Shop
				realizarShop(unVisitante);
				break;
			case 1: //Restaurante
			case 2: //Nado con delfines
				realizarNadoDelfines(unVisitante);
				break;
			case 3: //Snorkel
			case 4: 
				realizarMundoAventura(unVisitante);
				break;
			case 5: //Faro-Mirador
				realizarFaroMirador(unVisitante);
				break;
			case 6: //Carrera Gomones
				realizarCarreraGomones(unVisitante);
				break;
			default: //Restaurante
			}
		}
		
	}
}
