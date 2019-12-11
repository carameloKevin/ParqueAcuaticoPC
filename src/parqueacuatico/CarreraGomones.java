/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

class CarreraGomones {
    
	private Random random = new Random();
	private final int CANT_ASIENTOS_TREN = 15;
	private final int CANT_ESPACIO_CAMIONETA = 20;
	private final int CANT_GOMONES = 5;
	private int cantGomonesSolo = CANT_GOMONES;
	private int cantGomonesDuo = CANT_GOMONES;
	private int ultPosDuo = 0, cantidadGomones;
	private Camioneta camioneta;
	private Transporte trencito;
	private Chofer choferTrencito;
	CyclicBarrier barrera = new CyclicBarrier(3);
	AtomicInteger posicionGomonesCarrera = new AtomicInteger(0);
	private Semaphore[] gomonesSolo, gomonesDuo;
	
	public CarreraGomones()
	{
		
		trencito = new Transporte("TREN 01", CANT_ASIENTOS_TREN);
		choferTrencito = new Chofer("CHOFER_TREN 01", trencito);
		(new Thread(choferTrencito)).start();
		
		camioneta = new Camioneta("CAMIONETA 01", CANT_ESPACIO_CAMIONETA);
		(new Thread(camioneta)).start();;
		//gomonesSolo = new Gomon[CANT_GOMONES];
		//gomonesDuo = new Gomon[CANT_GOMONES];
		//inicializarGomones();
		
		gomonesSolo = new Semaphore[cantGomonesSolo];
		gomonesDuo = new Semaphore[cantGomonesDuo];
		inicializarGomones();
	}
	
	private void inicializarGomones() {
		
		for(int i = 0; i < cantGomonesSolo; i++)
		{
		/*	
			//gomonesSolo[i] = new Gomon("Solo " + i, barrera, 1, posicionGomonesCarrera);
			//new Thread(gomonesSolo[i]).start();
			
			//gomonesDuo[i] = new Gomon("Duo "+ i, barrera, 2, posicionGomonesCarrera);
			//new Thread(gomonesDuo[i]).start();
		*/
			
			gomonesSolo[i] = new Semaphore(1);
		}
		
		for(int i = 0; i < cantGomonesDuo; i++)
		{
			gomonesDuo[i] = new Semaphore(2);
		}
	}

	public void realizarCarreraGomones(Visitante unVisitante)
	{
		Semaphore[] seleccionGomon;
		boolean viajaDuo = (cantGomonesDuo > 0) && random.nextBoolean();
		boolean pudoSubirse = false;
		int pos = 0;
		
		System.out.println(unVisitante.getNombreCompleto() + " - Empezo la actividad de carrera de gomones");
		System.out.println("-----ESTA DESABILITADO EL TREN-----");
		if(true)
		{
			subirEnBici(unVisitante);
		}else {
			//subirEnBici(unVisitante);
			subirEnTrencito(unVisitante);
		}
		
		
		if(unVisitante.getTieneMochila())
		{
			System.out.println(unVisitante.getNombreCompleto() + " - Esta guardando las cosas");
			camioneta.guardarBolso(unVisitante);
			System.out.println(unVisitante.getNombreCompleto() + " - Guardo sus cosas bien");
		}
		
		
		//Busca un gomon libre para subirse, se sube al primero que encuentra
		
		if(viajaDuo)
		{
			seleccionGomon = gomonesDuo;
			cantidadGomones = cantGomonesDuo;
			pos = ultPosDuo;	//Va al ultimo gomon doble que alguien se subio para ver si hay alguien esperando
		}else {
			seleccionGomon = gomonesSolo;
			cantidadGomones = cantGomonesSolo;
		}
		
		while(!pudoSubirse)
		{
			//Intenta hasta subirse a un gomon. Si no pudo, continua intentado;
			pudoSubirse = seleccionGomon[pos].tryAcquire();
			if(!pudoSubirse)
			{
				pos = (pos+1) % cantidadGomones;	//Lo puse en un IF para poder guardar la POS una vez que subio a uno
			}
		}
		System.out.println(unVisitante.getNombreCompleto() + " - Ya se subio al gomon, esperando para bajar");
		try {
			barrera.await(100, TimeUnit.MILLISECONDS);
		} catch ( BrokenBarrierException | TimeoutException e) {
			System.out.println(unVisitante.getNombreCompleto() + " - Se canso de esperar y salio con menos del limite<-----------------------------");
		}catch(InterruptedException e)
		{
			
		}
		
		System.out.println("CARRERAGOMONES - Sale el gomon "+ pos); 
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("CARRERAGOMONES - LLego a la meta el gomon " + pos);
		seleccionGomon[pos].release();
		
		
		if(unVisitante.getDejoMochila())
		{
			camioneta.recuperarBolso(unVisitante);
		}
		
		System.out.println(unVisitante.getNombreCompleto()+ " - Termino la carrera de gomones");
	}

	public void subirEnBici(Visitante unVisitante)
	{
		System.out.println(unVisitante.getNombreCompleto()+ " - Se fue en bici");
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(unVisitante.getNombreCompleto()+ " - Llego a la cima en bici");
	}

	public void subirEnTrencito(Visitante unVisitante)
	{
		trencito.subirPasajero(unVisitante);
		trencito.bajarPasajero(unVisitante);
	}
	
}
