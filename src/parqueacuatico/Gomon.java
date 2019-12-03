package parqueacuatico;

import java.util.LinkedList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;


public class Gomon implements Runnable {
	
	AtomicInteger posCarrera;
	CyclicBarrier barrera;
	private String nombre = "00";
	private int capacidad;
	private Semaphore cantLugaresLibres,puedenBajarseGomon , puedeSalirGomon;
	private LinkedList<Visitante> pasajeros = new LinkedList<>();
	
	public Gomon(String nombre, CyclicBarrier barrier, int capacidad, AtomicInteger posCarrera) 
	{
		this.nombre = nombre;
		this.barrera = barrier;
		this.capacidad = capacidad;
		this.posCarrera = posCarrera;
		puedeSalirGomon = new Semaphore(0, true);
		puedenBajarseGomon = new Semaphore(capacidad);
		cantLugaresLibres = new Semaphore(0, true);
	}
	
	
	@Override
	public void run() {
		int aux = -1;
		try {
			//Habilita los lugaares para que se sienten
			cantLugaresLibres.release(capacidad);
			//Si no estan todos los asientos ocupados, no sale
			System.out.println("GOMON "+ this.nombre + " - Esta esperando que se ocupen los asientos");
			puedeSalirGomon.acquire(capacidad);
			System.out.println("GOMON "+ this.nombre + " - Esta esperando que se largue la carrera");
			barrera.await();
		} catch (InterruptedException | BrokenBarrierException e) {
			e.printStackTrace();
		}
			System.out.println("GOMON " + this.nombre + " - Se largo el gomon");		
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			aux = posCarrera.getAndSet(1);
			System.out.println("GOMON " + this.nombre + " - Llego el gomon en Posicion: " + aux);
			puedenBajarseGomon.release(capacidad);
	}
	
	public boolean subirPasajero(Visitante unPasajero)
	{
		boolean aux = cantLugaresLibres.tryAcquire();
		if(aux)
		{
			pasajeros.add(unPasajero);
			puedeSalirGomon.release();
		}
		return aux;
	}
	
	public void bajarPasajero(Visitante unPasajero)
	{
		try {
			puedenBajarseGomon.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		cantLugaresLibres.release();
	}
	
	
}
