package parqueacuatico;

import java.util.concurrent.CyclicBarrier;

public class Camioneta implements Runnable{
	
	//Cuidado con cambiar esta cantidad. Tiene que ser igual que la CyclicBarrier de carreraDeGomones
	private final int CANT_MIN_BOLSOS = 3;
	
	private String nombre = this.getClass().toString();
	private int ultPos = 0;
	private int cantidadBolsosSubidos = 0;
	private boolean[] espacio;
	private boolean estaOrigen, estaDestino;
	
	
	public Camioneta(String nombre, int capacidad)
	{
		espacio = new boolean[capacidad];
		for(int i = 0; i < capacidad; i++)
		{
			espacio[i] = false;
		}
		
		this.nombre = nombre;
		estaOrigen = true;
		estaDestino = false;
	}
	
	public synchronized void guardarBolso(Visitante unVisitante)
	{	
		
		System.out.println(unVisitante.getNombreCompleto() + " <<<<<<<<<<<<<");
		//si no hay espacio, o no esta en el lugar, espero
		System.out.println(unVisitante.getNombreCompleto() + " - Esperando que haya lugar o que vuelva la camioneta");
		while(cantidadBolsosSubidos >= espacio.length || !estaOrigen)
		{
			
			try {
				notify();
				wait();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		
		//si hay lugar
			//guardo la mochila
			espacio[ultPos] = true;
			//le digo que se guardo correctamente/ no es necesario
			//le doy la llave al visitante
			unVisitante.dejarEquipamiento(ultPos);
			//digo cuantas mochilas hay/ la ultima pos;
			ultPos++;
			cantidadBolsosSubidos++;
			//INTENTO notificar al chofer
			notify();
			System.out.println(unVisitante.getNombreCompleto() + " >>>>>>>>>>");
	}

	public synchronized void recuperarBolso(Visitante unVisitante)
	{
		System.out.println(unVisitante.getNombreCompleto() + " - Esperando a la camioneta en la meta/final");
		
		while(!this.estaDestino)
		{
			try {
				System.out.println("Esperando camioneta");
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//busca su bolso
		//cambia su variable a true
		espacio[unVisitante.getLlave()] = false;
		unVisitante.recuperarBolso();
		ultPos--;
		cantidadBolsosSubidos--;
		System.out.println(unVisitante.getNombreCompleto() + " - Pudo bajar su bolso de la camioneta");
		notify();
	}
	
	public void run() {
		while(true)
		{
			esperarEnOrigen();
			viajar();
			esperarEnDestino();
			viajar();
		}
	}

	private synchronized void esperarEnDestino() {
		estaDestino = true;
		System.out.println("CAMIONETA - Esperando que saquen todos los bolsos Si o Si");
		notifyAll();
		while(cantidadBolsosSubidos > 0){
			System.out.println("-------------------- " +cantidadBolsosSubidos + " ----------------------------");
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("CAMIONETA - Ya bajaron todos los bolsos. Me vuelvo para arriba");
		estaDestino = false;
	}

	private void viajar() {
		System.out.println("CAMIONETA - Viajando");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private synchronized void esperarEnOrigen() {
		estaOrigen = true;
		System.out.println("CAMIONETA - Esperando a que haya aunque sea un bolso");
		while(cantidadBolsosSubidos == 0)
		{
			System.out.println("-----------------------////////////////");
			notify();
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("CAMIONETA - Saliendo para abajo/el destino de los gomones");
		estaOrigen = false;
	}
	


	
	
}
