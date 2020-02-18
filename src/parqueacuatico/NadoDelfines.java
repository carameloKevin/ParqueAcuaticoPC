package parqueacuatico;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class NadoDelfines {

	/*
	 * Nado con delfines (FOTOCOPIA): para realizarla se dispone de 4 piletas. Es
	 * necesario que el visitante elija un horario para realizar la actividad entre
	 * los horarios preestablecidos de la misma. Se conforman grupos de 10 personas
	 * por pileta. En cada pileta nadaran dos delfines y la actividad dura
	 * aproximadamente 45 minutos. La política del parque es que en cada horario
	 * puede haber solo 1 grupo incompleto (de las 4 piletas)
	 */
	/*
	 * NADO CON DELFINELS (LO QUE HICE): Hay una CANT_PILETAS, se van registrando
	 * los pasajeros en las piletas (siempre se llenan de la primera a la ultima) y
	 * cuando hay CANT_PILETAS - 1 llenas, empieza el evento en el proximo horario
	 * disponible. LOS VISITANTES SIEMPRE RESERVAN PARA EL PROXIMO EVENTO, NO OTRO.
	 * El proximo evento empieza siempre y cuando este las CANT_PILETAS - 1 llenas o
	 * si son las 17 (Para que termine antes de las 18)
	 */

	private final int CANT_PILETAS = 4;
	private final int CANT_ESPACIO_PILETA = 10; // cantidad gente que entra en una pileta
	private final int MINIMA_GENTE_EMPEZAR = 4;//CANT_ESPACIO_PILETA * (CANT_PILETAS - 1);

	private int ultimoShow = -1;
	private int cantHorarios = 0;
	private int[] horariosNados;
	private CountDownLatch latchMinGente = new CountDownLatch(MINIMA_GENTE_EMPEZAR);
	private Reloj reloj;
	boolean showEstaSucediendo = false;
	private AtomicInteger[] cantGenteAnotadaPorTurno;

	LinkedBlockingQueue<Visitante>[] listasShow;

	public NadoDelfines(Reloj reloj, int[] horarios) {
		this.reloj = reloj;
		this.horariosNados = horarios;
		this.cantHorarios = horarios.length;
		this.cantGenteAnotadaPorTurno = new AtomicInteger[horarios.length];
		System.out.println(horariosNados[0]);
		inicializarListas();
	}

	public void inicializarListas() {
		// Inicializo las listas de visitantes para cada show
		listasShow = new LinkedBlockingQueue[cantHorarios];

		for (int i = 0; i < cantHorarios; i++) {
			listasShow[i] = new LinkedBlockingQueue<Visitante>(CANT_PILETAS * CANT_ESPACIO_PILETA);
		}

		// inicializo al encargado
		new Thread(new EncargadoPileta(this, reloj)).start();
	}

	public void realizarNadoDelfines(Visitante unVisitante) {
		// voy a necesitar un encargado de los turnos
		// Intento anotarlo en algun show. (hace 5 listas con 5 horarios y el usa el
		// mismo numero para meterte en uno y en otro)
		// Se anota en una lista y se va de este metodo.
		// Cuando sea su turno, le aviso con un notify o le cambio una variable y hago
		// que vuelva despues de su actividad actual
		// Se mete a este mismo metodo, pero en vez de venir por aca, pasa derecho a su
		// evento.
		//System.out.println(unVisitante.getNombreCompleto() + " - INICIO NADODELFINES");

		if (!unVisitante.tieneTurnoDelfines()) {
			System.out.println(unVisitante.getNombreCompleto() + " - Me estoy yendo a anotar <--- INICIO Nado Delfines");
			boolean anotado = false;
			// Lo intento anotar en el nado con delfines
			int pos = 0;

			
			//Por como lo programe tengo que tener cuidado de no meterlo en una lista de un evento que ya paso
			while(horariosNados[pos] < reloj.getHoraActual())
			{
				pos++;
			}
			
			while (pos < cantHorarios && !anotado) {
				anotado = listasShow[pos].offer(unVisitante); // offer es como el add/put pero retorna true o false
															// dependiendo si pudo anadirlo
				//Lo pone en la lista y se pasa uno
				pos++;

			}
			
			//vuelvo uno para atras;
			pos--;
			
			if (anotado) {
				unVisitante.setTurnoDelfines(pos);
				unVisitante.setHoraDelfines(horariosNados[pos]);
				// Este SOUT es mas para debug. Despues cambiarlo a "Se anoto exitosamente"
				System.out.println(unVisitante.getNombreCompleto() + " - Se anoto en el turno " + pos);
			}else {
				// Si no se pudo anotar, se va no mas
				System.out.println(unVisitante.getNombreCompleto() + " - No se pudo anotar. Se retiro");
			}
			
		} else {
			// Vino porque lo llamaron o por su cuenta para ver si empezo el show
			int horarioTurnoVisitante = unVisitante.getHoraDelfines();
			if (horarioTurnoVisitante == reloj.getHoraActual()
					&& listasShow[unVisitante.getTurnoDelfines()].size() >= MINIMA_GENTE_EMPEZAR) {
				// Si es la hora de su nado, se mete
				// Decrementa en uno la cantidad de gente que entro
				System.out.println("BARRERAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAaa");
				this.latchMinGente.countDown();

				System.out.println(unVisitante.getNombreCompleto() + " - Entro al show");

				int cantIteraciones = 0;
				
				
				synchronized (this) {
					System.out.println(unVisitante.getNombreCompleto() + " - Paso el synchronized");
					while (showEstaSucediendo) {
						cantIteraciones++;
						try {
							wait();
							System.out
									.println(unVisitante.getNombreCompleto() + " - Me quiero ir del nado de delfines " + cantIteraciones);
						} catch (InterruptedException e) {
						}
					}
				}
				
				//Le cambio las variables para que no siga entrando a nadar
				unVisitante.setTurnoDelfines(-1);
				unVisitante.setHoraDelfines(-1);
				System.out.println(unVisitante.getNombreCompleto() + " - Por fin se pudo ir del nado de delfines <------ FIN NadoDelfin");

			} else {
				if (horarioTurnoVisitante < reloj.getHoraActual()) {
					//Si no hay un minimo de gente y pasa de largo en if de arriba, va a pasar una hora y va a entrar aca y se va a ir.
					
					System.out.println(unVisitante.getNombreCompleto() + " - Se le paso su horario del show y se fue");

					// Lo saco de la lista en la que estaba y despues le cambio el ticket
					//listasShow[unVisitante.getTurnoDelfines()].remove(unVisitante);
					unVisitante.setTurnoDelfines(-1);
				}
				else {
				// Si no, es va
				//System.out.println(unVisitante.getNombreCompleto() + " - Se fue porque no era su turno de delfines");
				}	
			}
		}
	}

	public void avisarGente(int unHorario) {
		// Este metodo esta pensado para que tenga un while true afuera
		boolean avisarGente = false;
		int posicion = -1;

		System.out.println("NADO DELFIN - Buscando visitantes...");
		while (!avisarGente && posicion < this.cantHorarios) {
			posicion++;
			avisarGente = horariosNados[posicion] == unHorario;
		}

		// Esto siempre va a dar true, pero no esta de mas chequearlo
		if (avisarGente) {
			if (!listasShow[posicion].isEmpty())
			{
				System.out.println("NADO DELFIN - Visitantes encontrandos, avisandoles que vengan al show!");
			}
			
			// Si es un horario, le avisa a la gente
			while (!listasShow[posicion].isEmpty()) {
				//listasShow[posicion].poll().setIrAShowDelfines(true);
			}
		} else {
			// Si no hay a quien avisar que espere una hora
			System.out.println("ALGO SALIO MAL, NO DEBERIA LLEGAR ACA -----------------------------------------------------<<<<<<<");
			reloj.esperarUnaHora();
		}

	}

	public void comenzarShow() {

		while (reloj.getHoraActual() == horariosNados[ultimoShow] || !esHorarioShow()) {
			// No hice un metodo que esperara hasta cierto horario porque me iba a quedar
			// bastante parecido
			reloj.esperarUnaHora();
		}
		
		// Creo un latch para asegurarme que pasa la gente
		this.latchMinGente = new CountDownLatch(MINIMA_GENTE_EMPEZAR);

		// Primero tiene que cambiar esta variable asi cuando la gente llega al show
		// queda en el wait;
		showEstaSucediendo = true;
		//avisarGente(horarioActual);

		// espera un minimo y despues sale si no llegaron todos
		try {
			latchMinGente.await(4000, TimeUnit.MILLISECONDS);
			System.out.println("NADO DELFINES - Empezando un show");
			reloj.esperarUnaHora();	//simulo el tiempo
		} catch (InterruptedException e) {
			// Estoy usando un catch como if, lo cual es horrible
			System.out.println("NADO DELFINES - No llego la suficiente gente en tiempo y se cancelo el show");
		}
		
		this.showEstaSucediendo = false;
		System.out.println("NADO DELFINES - Termino el show");
		
		synchronized (this) {
			notifyAll();
		}
	}

	public boolean esHorarioShow() {
		int posicion = 0;
		boolean esHorario = false;

		while (!esHorario && posicion < cantHorarios) {
			esHorario = horariosNados[posicion] == reloj.getHoraActual();
			posicion++;
		}
		
		if(esHorario)
		{
			//Guardo cual va a ser el show que va a arrancar
			ultimoShow = posicion--;
		}

		return esHorario;
	}

}
