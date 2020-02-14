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
import java.util.concurrent.locks.ReentrantLock;

class CarreraGomones {
    
	private Random random = new Random();
	private final int CANT_ASIENTOS_TREN = 15;
	private final int CANT_ESPACIO_CAMIONETA = 20;
	private final int CANT_GOMONES = 5;
	
	private int cantGomonesSolo = CANT_GOMONES;
	private int cantGomonesDuo = CANT_GOMONES;
	private int ultPosDuo = 0;
	private int ultPosSolo = 0;
	private boolean yaHabiaAlguienDuo = false;
	
	private Camioneta camioneta;
	private Transporte trencito;
	private Gomon[] gomonesSolo = new Gomon[cantGomonesSolo];
	private Gomon[] gomonesDuo = new Gomon[cantGomonesDuo];
	private Chofer[] instructoresGuias = new Chofer[cantGomonesSolo + cantGomonesDuo];
	private Chofer choferTrencito;
	private Reloj elReloj;
	private ReentrantLock lock = new ReentrantLock();
	CyclicBarrier barrera = new CyclicBarrier(3);
	AtomicInteger posicionGomonesCarrera = new AtomicInteger(0);
	
	public CarreraGomones(Reloj unReloj)
	{
		elReloj = unReloj;
		trencito = new Transporte("TREN 01", CANT_ASIENTOS_TREN);
		choferTrencito = new Chofer("CHOFER_TREN 01", trencito);
		(new Thread(choferTrencito)).start();
		
		camioneta = new Camioneta("CAMIONETA 01", CANT_ESPACIO_CAMIONETA);
		(new Thread(camioneta)).start();
		
		inicializarGomones();
	}
	
	private void inicializarGomones() {
		
		//Podria haber inicializado los dos en el mismo for
		
		for(int i = 0; i < cantGomonesSolo; i++)
		{
			gomonesSolo[i] = new Gomon("GomonSolo" + i,1,1, barrera, posicionGomonesCarrera);
			new Thread(new Chofer("INSTRUCTORGOMON 1" + i, gomonesSolo[i])).start();
		}
		
		for(int i = 0; i < cantGomonesDuo; i++)
		{
			gomonesDuo[i] = new Gomon("GomonDuo" + i, 2,2, barrera, posicionGomonesCarrera);
			new Thread(new Chofer("INSTRUCTORGOMON 2" + i, gomonesDuo[i])).start();
		}
	}

	public void realizarCarreraGomones(Visitante unVisitante)
	{
		boolean subeEnBici = random.nextBoolean();
		boolean vaEnDuo;
		Gomon elGomon;
		
		//Metodo para subir
		if(subeEnBici)
		{
			subirEnBici(unVisitante);
		}else{
			subirEnTrencito(unVisitante);
		}
		
		//Guarda bolso del visitante
		if(unVisitante.getTieneMochila())
		{
			System.out.println(unVisitante.getNombreCompleto() + " - Dejo la mochila en la camioneta");
			camioneta.guardarBolso(unVisitante);
		}
		
		//Subida, carrera (adentro de gomones) y descenso de gomones
		
		vaEnDuo = random.nextBoolean();
		
		
		//Este lock esta por elGomon, ultPosDuo/Solo y yaHabiaAlguienDuo. Lo necesito para cuidar esas variables
		//elGomon lo podria sacar y que lo guarde el visitante, pero no me gusto mucho la idea
		System.out.println("-------------------");
		lock.lock();
		System.out.println("==================");
		if(vaEnDuo)
		{
			elGomon = gomonesDuo[ultPosDuo];
			gomonesDuo[ultPosDuo].subirPasajero(unVisitante);
			
			yaHabiaAlguienDuo = !yaHabiaAlguienDuo;
			if(!yaHabiaAlguienDuo)
			{
				ultPosDuo = (ultPosDuo + 1) % cantGomonesDuo; 
			}
		} else {
			elGomon = gomonesSolo[ultPosSolo];
			gomonesSolo[ultPosSolo].subirPasajero(unVisitante);
			ultPosSolo = (ultPosSolo+1) % cantGomonesSolo;
		}
		lock.unlock();
		
		//La carrera la hacen los gomones por su cuenta entre ellos, compartiendo la cyclicbarrier.
		elGomon.bajarPasajero(unVisitante);
		
		
		
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
		//Funciona igual que el Colectivo
		trencito.subirPasajero(unVisitante);
		trencito.bajarPasajero(unVisitante);
	}
	
}
