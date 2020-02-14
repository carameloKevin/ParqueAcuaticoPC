/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Carolina
 */
public class Parque {

	private final int CANT_RESTAURANTES = 3;
	
	private Random random = new Random();
	private Reloj reloj;
	private Semaphore molinetes = new Semaphore(6, true);
    private Shop elShop = new Shop();
    private Restaurante[] restaurantes = new Restaurante[CANT_RESTAURANTES];
    private NadoSnorkel actSnorkel = new NadoSnorkel();
    private Asistente[] asistentesSnorkel = new Asistente[3];
    private FaroMirador elFaroTobogan;
    private CarreraGomones laCarreraGomones;
    private MundoAventura elMundoAventura;
    private NadoDelfines nadoDelfines;
    
    
    public Parque(Reloj unReloj)
    {
    	reloj = unReloj;
    	
    	//Lo inicialice aca porque no sabia bien si iba a funcionar si lo inicializa arriba con los otros
    	nadoDelfines = new NadoDelfines(reloj);
    	elMundoAventura = new MundoAventura(reloj);
    	elFaroTobogan = new FaroMirador(reloj);
    	laCarreraGomones = new CarreraGomones(reloj);
    	//Inicializo los asistntes y los ejecuto
    
    	for(int i = 0; i < asistentesSnorkel.length; i++)
    	{
    		asistentesSnorkel[i] = new Asistente("" + i, actSnorkel);
    		(new Thread(asistentesSnorkel[i])).start();
    	}
    	
    	
    	//Inicializo los restaurantes
    	for(int i = 0; i < CANT_RESTAURANTES; i++)
    	{
    		restaurantes[i] = new Restaurante(i);
    	}
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
    
    public void realizarNadoSnorkel(Visitante unVisitante)
    {
    	this.actSnorkel.realizarNadoSnorkel(unVisitante);
    }
    
    public boolean estaAbierto()
    {
    	int hora = this.reloj.getHoraActual();
    	return (hora >= 9 && hora < 17);
    }
    
    public Reloj getReloj()
    {
    	return reloj;
    }

	public void realizarActividades(Visitante unVisitante) {
		while(estaAbierto())
		{
			int numActividad = 6;		//Debug
			//int numActividad = random.nextInt(7);
			
			switch(numActividad)
			{
			case 0: //Shop
				realizarShop(unVisitante);
				break;
			case 1: //Restaurante
				comerRestaurante(unVisitante);
				break;
			case 2: //Nado con delfines
				realizarNadoDelfines(unVisitante);
				break;
			case 3: //Snorkel
					realizarNadoSnorkel(unVisitante);
					break;
			case 4: 
				realizarMundoAventura(unVisitante);
				break;
			case 5: //Faro-Mirador
				realizarFaroMirador(unVisitante);
				break;
			case 6: //Carrera Gomones
				realizarCarreraGomones(unVisitante);
				break;
			default: //No deberia pasar, pero bue¸siempre tiene que ver un default
				System.out.println("Dio un paseo por el parque");
				break;
			}
		}
		
	}

	private void comerRestaurante(Visitante unVisitante) {
		int numRestaurante = random.nextInt(CANT_RESTAURANTES);
		
		if(unVisitante.getCantTickets() > 0 && estaAbierto())
		{
			if(restaurantes[numRestaurante].comioEnRestaurante(unVisitante))
			{
				numRestaurante = (numRestaurante+1) % CANT_RESTAURANTES;
			}
			
			restaurantes[numRestaurante].comerRestaurante(unVisitante);
		}
	}

	public void entrarParque(Visitante visitante) {
		System.out.println(visitante.getNombreCompleto() + " - Esta ingresando al parque por los molinetes");
		try {
			molinetes.acquire();
			Thread.sleep(100);
			molinetes.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(visitante.getNombreCompleto() + " - Ingreso bien por los molinetes");
		
	}
}
