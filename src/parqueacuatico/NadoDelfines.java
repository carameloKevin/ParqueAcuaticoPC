package parqueacuatico;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NadoDelfines {

	/*Nado  con  delfines (FOTOCOPIA):
	 * para realizarla se  dispone  de  4  piletas.  Es  necesario que  el visitante elija
	 *  un horario para realizar la actividad entre los horarios preestablecidos de
	 *  la  misma.  Se  conforman  grupos  de  10  personas  por  pileta.
	 *  En  cada  pileta nadaran dos delfines  y la actividad dura aproximadamente 45 minutos.
	 *  La política del  parque  es  que  en  cada  horario  puede  haber  solo  1  grupo  incompleto
	 *  (de  las  4 piletas)*/
	/*
	 * NADO CON DELFINELS (LO QUE HICE):
	 * Hay una CANT_PILETAS, se van registrando los pasajeros en 
	 * las piletas (siempre se llenan de la primera a la ultima)
	 * y cuando hay CANT_PILETAS - 1 llenas, empieza el evento en
	 * el proximo horario disponible. LOS VISITANTES SIEMPRE RESERVAN PARA
	 * EL PROXIMO EVENTO, NO OTRO. El proximo evento empieza siempre y cuando
	 * este las CANT_PILETAS - 1 llenas o si son las 17 (Para que termine antes de las 18)
	 */
	
	private final int CANT_PILETAS = 4;
	private final int CANT_ESPACIO_PILETA = 10; //cantidad gente que entra en una pileta
	private final int MINIMA_GENTE_EMPEZAR = CANT_ESPACIO_PILETA * (CANT_PILETAS-1);
	private int cantGenteRegistrada = 0;
	private Reloj reloj;
	boolean comenzoShow = false;
	
	Lock lock = new ReentrantLock();
	Condition esperandoRegistrarse = lock.newCondition();
	Condition esperandoGenteShow = lock.newCondition();
	Condition esperandoHoraShow = lock.newCondition();
	Condition salirDelShow = lock.newCondition();
	
	private Pileta[] piletas = new Pileta[CANT_PILETAS];
	
	public NadoDelfines(Reloj reloj)
	{
		this.reloj = reloj;
		inicializarPiletas();
	}
	
	public void inicializarPiletas()
	{
		for(int i = 0; i < CANT_PILETAS; i++)
		{
			piletas[i] = new Pileta(CANT_ESPACIO_PILETA, this);
			(new Thread(piletas[i])).start();;
		}
	}
	
	public void realizarNadoDelfines(Visitante unVisitante)
	{
		int pos = 0;
		boolean reservoLugar = false;
		
		lock.lock();
		//Reservar Lugar
		System.out.println(unVisitante.getNombreCompleto() + " - Comenzo la actividad Nado con Delfines");
		
		if(comenzoShow)
		{
			System.out.println(unVisitante.getNombreCompleto() + " - El show estaba en progreso y no se unio. Espera a que termine");
			//Si ya empezo el show, espera para registrarse
			try {
				esperandoRegistrarse.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		lock.unlock();
		
		//Intenta registrarse en alguna pileta
		System.out.println(unVisitante.getNombreCompleto() + " - Intenta registrarse a una de las piletas");
		while(!reservoLugar && pos < CANT_PILETAS)
		{
			
			reservoLugar = piletas[pos].reservarLugar();
			pos++;
		}
		
		if(reservoLugar)
		{
			pos--;
			lock.lock();
			cantGenteRegistrada++;
			System.out.println(unVisitante.getNombreCompleto() + " Se registro bien");
			//Si pudo registrarse, intenta meterse a alguna pileta
			
			if(cantGenteRegistrada < MINIMA_GENTE_EMPEZAR && reloj.getHoraActual() < 17)	//Primero se fija que haya gente suficiente
			{
				System.out.println(unVisitante.getNombreCompleto() + " - Falta gente para que empiece el Show asi que espera un rato");
				try {
					esperandoGenteShow.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else {
				System.out.println(unVisitante.getNombreCompleto() + " - Justo era el ultimo que hacia falta para empezar");
				esperandoGenteShow.signalAll();
			}
			
			if(!comenzoShow)	//Comenzo show va a cambiar cuando sea la hora para comenzar el show
			{
				System.out.println(unVisitante.getNombreCompleto() + " - Esperando que sea la hora para arrancar");
				try {
					esperandoHoraShow.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//Comienza el show en otra cosa, el visitante no hace ningun Thread.sleep ni nada, solo pasa de aca a querer salir
			System.out.println(unVisitante.getNombreCompleto() + " - Es un milenial que no puede ni esperar dos minutos en el show que ya se quiere ir. Esta esperando que termine para irse");
			//Salir del show
			try {
				salirDelShow.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally
			{
				System.out.println(unVisitante.getNombreCompleto() + " Termino la actividad de nado con delfines");
				cantGenteRegistrada--;
				lock.unlock();
			}

		}else{
			System.out.println(unVisitante.getNombreCompleto() + " - No se pudo registrar y se va triste <----------------");
		}
	}

	public void abrirRegistros() {
		//Todos los que estan esperando para anotarse los suelta
		lock.lock();
		System.out.println("PILETA - se abrieron los registros para el proximo show");
		esperandoRegistrarse.signalAll();
		lock.unlock();
		
	}

	public void comenzarShow() {
		int horario = reloj.getHoraActual();
		lock.lock();
		System.out.println("PILETA - Esperando que se anoten");
		
		//La pileta tiene que empezar que haya el minimo de gente, si no no puede comenzar
		while(cantGenteRegistrada < MINIMA_GENTE_EMPEZAR && horario != 17)
		{
			lock.unlock();
			horario = reloj.esperarUnaHora();
			lock.lock();
		}
		
		
		
		/*
		if(cantGenteRegistrada < MINIMA_GENTE_EMPEZAR && horario < 17);
		{
			try {
				esperandoGenteShow.await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		esperandoGenteShow.signalAll();
		horario = reloj.getHoraActual();
		//Espera el horario del siguiente show
		//El horario < 17 es para que si llegan a esa las 17, arranquen el show si o si
		while((horario != 10 && horario != 12 && horario != 14 && horario != 16) && horario < 17) //Debe ver una mejor forma con un arreglo, pero por ahora sirve
		{
			System.out.println("PILETA - Esperando una hora mas para empezar el show");
			horario = reloj.esperarUnaHora();
			System.out.println(horario);
		}
		
		comenzoShow = true;
		esperandoHoraShow.signalAll();
		
		System.out.println("PILETA - Una de las piletas comenzo");
		lock.unlock();
		

		reloj.utilizarTiempoEvento();
		
		}

	public void terminarShow() {
		lock.lock();
		System.out.println("PILETA - Ya se pueden ir los visitantes");
		salirDelShow.signalAll();
		comenzoShow = false;
		lock.unlock();
	}
	
	
	
}
