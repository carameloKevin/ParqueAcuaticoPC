package parqueacuatico;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Transporte {
	
	protected String nombreTransporte = this.getClass().toString();
	protected int cantAsientos;
	protected int cantPasajeros;
	protected int cantMinimaGente = 1;
	protected boolean estaEstacion, estaDestino;
	protected Lock lock = new ReentrantLock();
	protected Condition subirse = lock.newCondition();
	protected Condition bajarse = lock.newCondition();
	protected Condition arrancar = lock.newCondition();	
	
	public Transporte(String nro, int cantAsientosLibres) {
		this.nombreTransporte=nro;
		this.cantAsientos = cantAsientosLibres;
		estaEstacion = false;
		estaDestino = false;
	}
	
	public Transporte(String nro, int cantAsientosLibres, int cantMinimaGente) {
		this.nombreTransporte=nro;
		this.cantAsientos = cantAsientosLibres;
		estaEstacion = false;
		estaDestino = false;
		this.cantMinimaGente = cantMinimaGente;
	}

	public void esperarSubidaPasajeros() {
		System.out.println(nombreTransporte + " - Comienza esperarSubidaPasajero");
		lock.lock();
		estaEstacion = true;
		
		while(cantPasajeros < cantMinimaGente) {
			System.out.println(nombreTransporte + "  - Espero a que se suban " + cantMinimaGente + " pasajeros. Pasajeros actuales: " + cantPasajeros);
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
			Thread.sleep(500);
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
		
		System.out.println(nombreTransporte + "  - Se bajaron todos. Vuelvo al inicio");
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
