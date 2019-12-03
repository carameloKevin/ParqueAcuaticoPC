/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

class CarreraGomones {
    
	private Random random = new Random();
	private final int CANT_ASIENTOS_TREN = 15;
	private final int CANT_ESPACIO_CAMIONETA = 20;
	private final int CANT_GOMONES = 5;
	private Camioneta camioneta;
	private Transporte trencito;
	private Gomon[] gomonesSolo, gomonesDuo;
	CyclicBarrier barrera = new CyclicBarrier(5);
	AtomicInteger posicionGomonesCarrera = new AtomicInteger(0);
	
	public CarreraGomones()
	{
		trencito = new Transporte("Tren01", CANT_ASIENTOS_TREN);
		camioneta = new Camioneta("Camioneta01", CANT_ESPACIO_CAMIONETA);
		gomonesSolo = new Gomon[CANT_GOMONES];
		gomonesDuo = new Gomon[CANT_GOMONES];
		inicializarGomones();
	}
	
	private void inicializarGomones() {
		for(int i = 0; i < CANT_GOMONES; i++)
		{
			//Creo el gomon y lo incio apenas se crea
			
			gomonesSolo[i] = new Gomon("Solo " + i, barrera, 1, posicionGomonesCarrera);
			new Thread(gomonesSolo[i]).start();
			
			gomonesDuo[i] = new Gomon("Duo "+ i, barrera, 2, posicionGomonesCarrera);
			new Thread(gomonesDuo[i]).start();
		}
	}

	public void realizarActividadCarreraGomones(Visitante unVisitante)
	{
		Gomon[] seleccionGomon;
		boolean viajaSolo = random.nextBoolean();
		boolean pudoSubirse = false;
		int pos = 0;
		
		System.out.println("VISITANTE " + unVisitante.getNombre() +" - Empezo la actividad de carrera de gomones");
		if(random.nextBoolean())
		{
			subirEnBici(unVisitante);
		}else {
			subirEnTrencito(unVisitante);
		}
		
		if(unVisitante.getTieneMochila())
		{
			System.out.println("VISITANTE " + unVisitante.getNombre() +" - Esta guardando las cosas");
			camioneta.guardarBolso(unVisitante);
			System.out.println("VISITANTE " + unVisitante.getNombre() +" - Guardo sus cosas bien");
		}
		
		//Busca un gomon libre para subirse, se sube al primero que encuentra
		
		if(viajaSolo)
		{
			seleccionGomon = gomonesSolo;
		}else {
			seleccionGomon = gomonesDuo;
		}
		
		while(!pudoSubirse)
		{
			//Intenta hasta subirse a un gomon. Si no pudo, continua intentado;
			pudoSubirse = seleccionGomon[pos].subirPasajero(unVisitante);
			pos = pos+1 % CANT_GOMONES;
		}
		
		//La carrera la hace el mismo run del gomon
		
		seleccionGomon[pos].bajarPasajero(unVisitante);
		
		if(unVisitante.getDejoMochila())
		{
			camioneta.recuperarBolso(unVisitante);
		}
		System.out.println("VISITANTE " + unVisitante.getNombre()+ " - Termino la carrera de gomones");
	}


	public void subirEnBici(Visitante unVisitante)
	{
		System.out.println("Visitante " + unVisitante.getNombre() +" - Se fue en bici");
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Visitante "+ unVisitante.getNombre() + " - Llego a la cima en bici");
	}

	public void subirEnTrencito(Visitante unVisitante)
	{
		trencito.subirPasajero(unVisitante);
		trencito.bajarPasajero(unVisitante);
	}
	
}
