package parqueacuatico;

import java.util.LinkedList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;


public class Gomon implements Runnable {
	
	AtomicInteger posCarrera;
	CyclicBarrier barrera;
	private String nombre = "00";
	private int capacidad;
	private Semaphore lugaresLibres, lugaresOcupados, puedeVolverGomonBase, terminoBajadaGomon;
	private LinkedList<Visitante> pasajeros = new LinkedList<>();
	
	public Gomon(String nombre, CyclicBarrier barrier, int capacidad, AtomicInteger posCarrera) 
	{
		this.nombre = nombre;
		this.barrera = barrier;
		this.capacidad = capacidad;
		this.posCarrera = posCarrera;
		lugaresLibres = new Semaphore(capacidad);
		lugaresOcupados = new Semaphore(0);
		puedeVolverGomonBase = new Semaphore(0);
		terminoBajadaGomon = new Semaphore(0);
		
	}
	
	
	@Override
	public void run() {
		while(true)
		{
		int aux = -1;
		try {
			//Si no estan todos los asientos ocupados, no sale
			System.out.println(this.getNombreCompleto() + " - Esta esperando que se ocupen los asientos");
			lugaresOcupados.acquire(capacidad);
			System.out.println(this.getNombreCompleto() + " - Esta esperando que se largue la carrera");
			
			barrera.await(5, TimeUnit.SECONDS);
		} catch (BrokenBarrierException | TimeoutException e) {
			System.out.println(this.getNombreCompleto() + " - Se canso de esperar y se largo solo");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			System.out.println(this.getNombreCompleto() + " - Se largo el gomon");		
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			aux = posCarrera.getAndAdd(1);
			System.out.println(this.getNombreCompleto() + " - Llego el gomon en Posicion: " + aux);
			
			terminoBajadaGomon.release(capacidad);	//Para que cada pasajero tome uno
			try {
				puedeVolverGomonBase.acquire(capacidad);//Para asegurarme que no quedo nadie arriba del gomon
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	

		}
	}
	
	public boolean subirPasajero(Visitante unPasajero)
	{
		boolean aux = lugaresLibres.tryAcquire();
		if(aux)
		{
			System.out.println(unPasajero.getNombreCompleto() + " - Se subio al gomon " + this.nombre + " Que tiene capacidad para: " + this.capacidad);
			lugaresOcupados.release();
		}
		return aux;
	}
	
	public void bajarPasajero(Visitante unPasajero)
	{
		System.out.println(unPasajero.getNombreCompleto() + " - Se quiere bajar del gomon");
		try {
			terminoBajadaGomon.acquire();
			lugaresOcupados.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		lugaresLibres.release();
		puedeVolverGomonBase.release();
		System.out.println(unPasajero.getNombreCompleto() + " - Se bajo del gomon y ya casi termina la actividad");
	}
			
	
	public String getNombreCompleto() {
		return "GOMON " + this.nombre;
	}
	
}
