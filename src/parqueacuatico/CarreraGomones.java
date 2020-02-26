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
	private final int CANT_MIN_ARRANCAR = 5;

	private int cantBicis = 10;
	private int cantGomonesSolo = CANT_GOMONES;
	private int cantGomonesDuo = CANT_GOMONES;
	private int ultPosDuo = -1;
	private int ultPosSolo = 0;
	private boolean yaHabiaAlguienDuo = true;

	private Reloj elReloj;
	private Carrera carreraAux = new Carrera(CANT_MIN_ARRANCAR);
	private Camioneta camioneta;
	private CamionetaBicis camionetaBicis;
	private Transporte trencito;
	private Gomon[] gomonesSolo = new Gomon[cantGomonesSolo];
	private boolean[] estaParaSalirSolo = new boolean[cantGomonesSolo];
	private Gomon[] gomonesDuo = new Gomon[cantGomonesDuo];
	//private boolean[] estaParaSalirDuo = new boolean[cantGomonesDuo];
	// private Chofer[] instructoresGuias = new Chofer[cantGomonesSolo +
	// cantGomonesDuo]; //Los inicialice con los gomones
	private Chofer choferTrencito;
	CyclicBarrier barrera = new CyclicBarrier(3);
	AtomicInteger posicionGomonesCarrera = new AtomicInteger(0);

	public CarreraGomones(Reloj unReloj) {
		elReloj = unReloj;
		trencito = new Transporte("TREN 01", CANT_ASIENTOS_TREN, elReloj);
		choferTrencito = new Chofer("CHOFER_TREN 01", trencito);
		(new Thread(choferTrencito)).start();

		camioneta = new Camioneta("CAMIONETA 01", CANT_ESPACIO_CAMIONETA, elReloj);
		(new Thread(new ChoferCamioneta(camioneta))).start();

		camionetaBicis = new CamionetaBicis("CamionetaBicis 01", this);
		(new Thread(camionetaBicis)).start();
		inicializarGomones();
	}

	private void inicializarGomones() {

		// Podria haber inicializado los dos en el mismo for

		for (int i = 0; i < cantGomonesSolo; i++) {
			gomonesSolo[i] = new Gomon("GomonSolo" + i, 1, 1, this);
			new Thread(new Chofer("INSTRUCTORGOMON 1" + i, gomonesSolo[i])).start();
		}

		for (int i = 0; i < cantGomonesDuo; i++) {
			gomonesDuo[i] = new Gomon("GomonDuo" + i, 2, 2, this);
			new Thread(new Chofer("INSTRUCTORGOMON 2" + i, gomonesDuo[i])).start();
		}
	}

	public void realizarCarreraGomones(Visitante unVisitante) {
		System.out.println(unVisitante.getNombreCompleto() + " - Llego a la base de gomones <-- INICIO Gomones");

		boolean subeEnBici = true;// = random.nextBoolean();
		boolean vaEnDuo;
		Gomon elGomon;

		// Metodo para subir

		
		  if (subeEnBici) { subirEnBici(unVisitante); } else {
		 subirEnTrencito(unVisitante); }

		// Guarda bolso del visitante
		if (unVisitante.getTieneMochila()) {
			System.out.println(unVisitante.getNombreCompleto() + " - Esta intentando dejar la mochila en la camioneta");
			camioneta.guardarBolso(unVisitante);
		}

		// Subida, carrera (adentro de gomones) y descenso de gomones

		vaEnDuo = random.nextBoolean();

		// Este lock esta por elGomon, ultPosDuo/Solo y yaHabiaAlguienDuo. Lo necesito
		// para cuidar esas variables
		// elGomon lo podria sacar y que lo guarde el visitante, pero no me gusto mucho
		// la ideas
		// Hay muchas variables que tienen que cuidar, por eso el synchronized this
		if (vaEnDuo) {
			synchronized(this)
			{
				yaHabiaAlguienDuo = !yaHabiaAlguienDuo;
				if (!yaHabiaAlguienDuo) {
					// estaParaSalirDuo[ultPosDuo] = true;
					ultPosDuo = (ultPosDuo + 1) % cantGomonesDuo;
				} else {
					
				}
				
				elGomon = gomonesDuo[ultPosDuo];
			}

			

		} else {
			synchronized (this) {
				estaParaSalirSolo[ultPosSolo] = true;
				ultPosSolo = (ultPosSolo + 1) % cantGomonesSolo;
				elGomon = gomonesSolo[ultPosSolo];
			}
		}
		elGomon.subirPasajero(unVisitante);
		// La carrera la hacen los gomones por su cuenta entre ellos, compartiendo la
		// cyclicbarrier.
		elGomon.bajarPasajero(unVisitante);

		if (unVisitante.getDejoMochila()) {
			camioneta.recuperarBolso(unVisitante);
		}
		System.out.println(unVisitante.getNombreCompleto() + " - Se esta yendo de la base de gomones <-- FIN Gomones");

	}

	public void subirEnBici(Visitante unVisitante) {
		System.out.println(unVisitante.getNombreCompleto() + " - Esta por subir en bici");
		synchronized (this) {
			while (cantBicis == 0) {
				System.out.println(unVisitante.getNombreCompleto() + " - No pudo salir porque no habia bicis");
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		System.out.println(unVisitante.getNombreCompleto() + " - Empezo a subir en bici");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		camionetaBicis.subirBici();
		System.out.println(unVisitante.getNombreCompleto() + " - Termino de subir hasta los gomones y dejo la bici");
	}

	public void subirEnTrencito(Visitante unVisitante) {
		// Funciona igual que el Colectivo
		trencito.subirPasajero(unVisitante);
		trencito.bajarPasajero(unVisitante);
	}

	public void ponerEnSalidaGomon(Gomon elGomon) {
		// metodo llamado por el gomon
		System.out.println(elGomon.getNombre() + " - Esta esperando para ponerse en la salida");
		synchronized (this) {
			if(elGomon.estaLleno())
			{
			if (carreraAux.getYaComenzo()) {

				// Si la carrera ya comenzo, la descarto (sigue de fondo) y comienzo una nueva
				carreraAux = new Carrera(CANT_MIN_ARRANCAR);
				System.out.println("Generada otra carrera Nueva");
			}
		

		// tiene que soltar el Lock antes de entrar a este metodo porque si no se va a
		// quedar con el lock todo el rato
		carreraAux.agregarGomonASalida(elGomon);
			}
		}
		System.out.println(elGomon.getNombre() + " - No estaba lleno, asi que no salio en la carrera");
		// podria haberle asignado la carrera al gomon y que el gomon llame a ese metodo

	}

	public synchronized void devolverBicis(int cantidadBicis) {
		System.out.println("CARRERAGOMONES - Devolvieron " + cantidadBicis + " bicis");
		this.cantBicis += cantidadBicis;
		notifyAll();

	}

}
