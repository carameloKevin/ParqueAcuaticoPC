package parqueacuatico;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NadoDelfines {

	/*Nado  con  delfines:
	 * para realizarla se  dispone  de  4  piletas.  Es  necesario que  el visitante elija
	 *  un horario para realizar la actividad entre los horarios preestablecidos de
	 *  la  misma.  Se  conforman  grupos  de  10  personas  por  pileta.
	 *  En  cada  pileta nadaran dos delfines  y la actividad dura aproximadamente 45 minutos.
	 *  La política del  parque  es  que  en  cada  horario  puede  haber  solo  1  grupo  incompleto
	 *  (de  las  4 piletas)*/
	
	private final int MINIMA_GENTE_EMPEZAR = 5;
	private final int CANT_PILETAS = 4;
	private Semaphore[] piletas;
	private Reloj reloj;
	private Lock lock = new ReentrantLock();
	private Condition estanTodos = lock.newCondition();
	private Condition entrarPileta = lock.newCondition();
	private Condition anotarse = lock.newCondition();
	private CyclicBarrier empezarShow = new CyclicBarrier(MINIMA_GENTE_EMPEZAR);
	
	private boolean abiertoAnotacion = true;
	boolean abiertoInscripcion = true;
	
	public NadoDelfines(Reloj reloj)
	{
		this.reloj = reloj;
		piletas = new Semaphore[CANT_PILETAS];
		inicializarPiletas();
	}
	
	
	public void realizarNadoConDelfines(Visitante unVisitante) {
		if(anotarseNadoDelfines(unVisitante))
		{
			nadarConDelfines(unVisitante);
			salirDelNadoConDelfines(unVisitante);
		}
	}
	
	public void anotarseNadoDelfines(Visitante unVisitante)
	{
		int pos = -1;
		boolean anotado = false; 
		
		System.out.println(unVisitante.getNombreCompleto() + " - Se esta queriendo anotar al nado con delfines");
		
		lock.lock;
		if(!abiertoAnotacion)
		{
			anotarse.await();//Un lock para pararlos a todos
		}
		
		while(pos < CANT_PILETAS && !anotado)
		{
			pos++;
			anotado = piletas[pos].tryAcquire();
			
			if(pos > 3)
			{
				estanTodos.signalAll();
			}
		}
		
		System.out.println(unVisitante.getNombreCompleto() + " - Se anoto en una pileta");
		System.out.println(unVisitante.getNombreCompleto() + " - Se quiere meter a la pileta, pero tiene que esperar que esten todo");
		//!------------------!Si se produce deadlock aca es porque no soltas el lock! Supuse que la cyclic barrier la soltaba ---
		
		estanTodos.await();
		
		System.out.println(unVisitante.getNombreCompleto() + " - Empezo el show de los delfines!");
		Thread.sleep(1000);
		
		
		
		
	}
	
	
	private void inicializarPiletas()
	{
		int cantGente = 10;
		for(int i =0; i < CANT_PILETAS; i++)
		{
			piletas[i] = new Semaphore(cantGente);
		}
	}


	
	public void abrirRegistros() {
		int horaActual = reloj.getHoraActual();
		lock.lock();
		abiertoAnotacion = true;
		lock.unlock();
		System.out.println("NadoDelfines - Checkeando si esta abierto");
		while(horaActual != 10 || horaActual != 12 || horaActual != 14 || horaActual != 16)
		{
			reloj.esperarUnaHora();
		}
	}
	
	public void comenzarShow()
	{
		cantGente.acquire(MINIMA_GENTE_EMPEZAR);
	}
	
}
