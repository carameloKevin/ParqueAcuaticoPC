package parqueacuatico;

import java.util.concurrent.CyclicBarrier;

public class Camioneta{
	
	//Cuidado con cambiar esta cantidad. Tiene que ser igual que la CyclicBarrier de carreraDeGomones
	private final int CANT_MIN_BOLSOS = 3;
	private final int LIMITE = 20;
	
	private String nombre = this.getClass().toString();
	private int cantidadBolsosSubidos = 0;
	private boolean estaOrigen, estaDestino;
	private Reloj elReloj;
	
	public Camioneta(String nombre, int capacidad, Reloj unReloj)
	{
		elReloj = unReloj;
		this.nombre = nombre;
		estaOrigen = true;
		estaDestino = false;
	}
	
	public synchronized void guardarBolso(Visitante unVisitante)
	{	
		
		//si no hay espacio, o no esta en el lugar, espero
		System.out.println(unVisitante.getNombreCompleto() + " - Esperando que haya lugar o que vuelva la camioneta");
		notify();
		while(cantidadBolsosSubidos >= LIMITE || !estaOrigen)
		{
			try {
				wait();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		
		//si hay lugar
			//guardo la mochila
			//le digo que se guardo correctamente/ no es necesario
			//le doy la llave al visitante
			unVisitante.dejarEquipamiento(cantidadBolsosSubidos);
			//digo cuantas mochilas hay/ la ultima pos;
			cantidadBolsosSubidos++;
			//INTENTO notificar al chofer
			notifyAll();
			}

	public synchronized void recuperarBolso(Visitante unVisitante)
	{
		System.out.println(unVisitante.getNombreCompleto() + " - Esperando a la camioneta en la meta/final");
		notify();
		while(!this.estaDestino)
		{
			try {
				//System.out.println("Esperando camioneta");
				//notify();	//Le intenta notificar a la camioneta
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//busca su bolso
		//cambia su variable a true
		unVisitante.recuperarBolso();
		cantidadBolsosSubidos--;
		System.out.println(unVisitante.getNombreCompleto() + " - Pudo bajar su bolso de la camioneta " + cantidadBolsosSubidos);
		notifyAll();	//Le intenta notificar a la camioneta que ya bajo su bolso
	}


	public synchronized void esperarEnDestino() {
		estaDestino = true;
		System.out.println("CAMIONETA - Esperando que saquen todos los bolsos Si o Si");
		notifyAll();
		//Cant min bolsos es la gente minima para que empiece una carrera
		//es 1 porque puede suceder que 1 persona se suba a uno Duo y no sale hasta que llegue
		//otra persona
		while(cantidadBolsosSubidos > 1){
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("CAMIONETA - Ya bajaron todos (o casi todos) los bolsos. Me vuelvo para arriba");
		estaDestino = false;
	}

	public void viajar() {
		System.out.println("CAMIONETA - Viajando");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public synchronized void esperarEnOrigen() {
		estaOrigen = true;
		System.out.println("CAMIONETA - Esperando a que haya aunque sea " + CANT_MIN_BOLSOS +" bolso");
		notifyAll();
		while(cantidadBolsosSubidos < CANT_MIN_BOLSOS)
		{	
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("CAMIONETA - Saliendo para abajo/el destino de los gomones");
		estaOrigen = false;
	}
	


	
	
}
