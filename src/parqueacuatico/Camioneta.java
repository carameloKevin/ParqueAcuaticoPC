package parqueacuatico;

public class Camioneta implements Runnable{
	
	//Cuidado con cambiar esta cantidad. Tiene que ser igual que la CyclicBarrier de carreraDeGomones
	private final int CANT_MIN_BOLSOS = 3;
	
	private String nombre = this.getClass().toString();
	private int ultPos = 0;
	private boolean[] espacio;
	private boolean estaOrigen, estaDestino;
	
	
	public Camioneta(String nombre, int capacidad)
	{
		espacio = new boolean[capacidad];
		this.nombre = nombre;
		estaOrigen = true;
		estaDestino = false;
	}
	
	public synchronized boolean guardarBolso(Visitante unVisitante)
	{	
		boolean res = ultPos < espacio.length;
		
		while(!res && !estaOrigen)
		{
			System.out.println(unVisitante.getNombreCompleto() + " - Esperando que haya lugar o que vuelva la camioneta");
			notify();
			try {
				wait();
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		
		if(res )
		{
			res = espacio[ultPos];
			unVisitante.dejarEquipamiento(ultPos);
			ultPos++;
		}
		
		return res;
	}

	public synchronized void recuperarBolso(Visitante unVisitante)
	{
		System.out.println(unVisitante.getNombreCompleto() + " - Esperando a la camioneta en la meta/final");
		while(!this.estaDestino)
		{
			notify();
			try {
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
	}
	
	public void run() {
		esperarEnOrigen();
		viajar();
		esperarEnDestino();
		viajar();
	}

	private synchronized void esperarEnDestino() {
		estaDestino = true;
		System.out.println("CAMIONETA - Esperando que saquen todos los bolsos Si o Si");
		while(ultPos > 0){
			notify();
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
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private synchronized void esperarEnOrigen() {
		estaOrigen = true;
		System.out.println("CAMIONETA - Esperando a que haya aunque sea 5/laCantidadDeGenteQueVaEnGomones bolsos como minimo");
		while(ultPos < CANT_MIN_BOLSOS)
		{
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
