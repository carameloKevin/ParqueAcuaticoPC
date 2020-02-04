package parqueacuatico;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Transporte {
	
	private String nombreTransporte = this.getClass().toString();
	private int cantAsientos;
	private int cantPasajeros;
	private boolean estaEstacion, estaDestino;
	private Lock lock = new ReentrantLock();
	private Condition subirse = lock.newCondition();
	private Condition bajarse = lock.newCondition();
	private Condition arrancar = lock.newCondition();	
	
	public Transporte(String nro, int cantAsientosLibres) {
		this.nombreTransporte=nro;
		this.cantAsientos = cantAsientosLibres;
		estaEstacion = false;
		estaDestino = false;
	}

	public void  esperarSubidaPasajeros() {
		System.out.println(nombreTransporte + " - Comienza esperarSubidaPasajero");
		lock.lock();
		estaEstacion = true;
		//Espera que se suba alguien
		/*
		 * No puse que espere una hora porque signifca sacar el await y dejar que cada una hora (usando Reloj) se fije,
		 * lo cual no me suena muy concurrente. Hacer una mezcla de los dos no se me ocurrio,
		 * hacer que se fije si hay pasajeros, signallALl(), esperar una hora y despues await()? no tiene sentido y sacarle
		 * el await le saca lo concurrente, asi que quedo asi, cuando se sube uno, ya puede arrancar, 
		 * si se subio alguien mas, buenisimo
		 */
		while(cantPasajeros < 1) {
			System.out.println(nombreTransporte + "  - Espero a que se suba alguien. Pasajeros actuales: " + cantPasajeros);
			try {
				subirse.signalAll();
				arrancar.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(nombreTransporte + " - Me estoy yendo de la estacion");
		estaEstacion = false;
		lock.unlock();
	}


	public void viajar() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void esperarBajadaPasajeros() {
		lock.lock();
		System.out.println(nombreTransporte + " - Llegamos al destino. Esperando que se bajen todos. Pasajeros: " + cantPasajeros);
		
		estaDestino = true;
		while(cantPasajeros > 0)
		{
			System.out.println(nombreTransporte + "  - Todavia quedan pasajeros. Pasajeros: " + cantPasajeros);
			try {
				bajarse.signalAll();
				arrancar.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		estaDestino = false;
		
		System.out.println(nombreTransporte + "  - Se bajaron todos. Vuelvo a la estacion");
		lock.unlock();
	}

	public void subirPasajero(Visitante pasajero) {
		lock.lock();
		System.out.println(pasajero.getNombreCompleto() + " - Me estoy queriendo subir al transporte");
		while(!estaEstacion || cantPasajeros >= cantAsientos)
		{
			try {
				subirse.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		cantPasajeros++;
		System.out.println(pasajero.getNombreCompleto() + " - Se subio al transporte "+ this.nombreTransporte);
		arrancar.signal();
		lock.unlock();
	}

	public void bajarPasajero(Visitante pasajero) {
		lock.lock();
		System.out.println(pasajero.getNombreCompleto() + " - Me quiero bajar");
		while(!estaDestino)
		{
			try {
				bajarse.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		cantPasajeros--;
		System.out.println(pasajero.getNombreCompleto() + " - Me pude bajar re contento. Pasajeros restantes: " + cantPasajeros);
		if(cantPasajeros == 0)
		{
			arrancar.signal();
		}
		lock.unlock();
	}

}
