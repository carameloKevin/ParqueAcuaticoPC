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
	 * 18.02.20
	 * El problema para el segundo wait fue asi. Cuando se hace la hora para entrar
	 * los visitantes entran a la pileta porque es la hora, y pasan el countdown, o no, no se, pero el tema es que
	 * cuando llegan al while(showEstaSucediendo), eso esta en false porque el pobre encargadoPileta no lo
	 * pudo setear en true. Entonces entran y lo pasan por arriba. Por eso agregue un shoNoComenzo o lo que sea,
	 * para que lleguen, tengan que pasar primero eso y despues el otro. Siempre hay uno en true y el otro en false
	 */
	
	
	
	
	
	
	

	/*
	 * Nado con delfines (FOTOCOPIA): para realizarla se dispone de 4 piletas. Es
	 * necesario que el visitante elija un horario para realizar la actividad entre
	 * los horarios preestablecidos de la misma. Se conforman grupos de 10 personas
	 * por pileta. En cada pileta nadaran dos delfines y la actividad dura
	 * aproximadamente 45 minutos. La política del parque es que en cada horario
	 * puede haber solo 1 grupo incompleto (de las 4 piletas)
	 */

	private final int CANT_PILETAS = 4;
	private final int CANT_ESPACIO_PILETA = 10; // cantidad gente que entra en una pileta
	private final int MINIMA_GENTE_EMPEZAR = 30;//CANT_ESPACIO_PILETA * (CANT_PILETAS - 1);
	private final int MAXIMA_GENTE_PILETA = 40;
	
	private int ultimoShow = -1;
	private int cantHorarios = 0;
	private int[] horariosNados;
	private CountDownLatch latchMinGente = new CountDownLatch(MINIMA_GENTE_EMPEZAR);
	private Reloj reloj;
	boolean showEstaSucediendo = false;
	private boolean showNoComenzo = true;
	private AtomicInteger[] cantGenteAnotadaPorTurno;

	public NadoDelfines(Reloj reloj, int[] horarios) {
		this.reloj = reloj;
		this.horariosNados = horarios;
		this.cantHorarios = horarios.length;
		this.cantGenteAnotadaPorTurno = new AtomicInteger[horarios.length];
		new Thread(new EncargadoPileta(this, reloj)).start();
		inicializarAtomicInteger();
	}
	
	private void inicializarAtomicInteger() {
		for(int i = 0; i < cantHorarios; i++)
		{
			cantGenteAnotadaPorTurno[i] = new AtomicInteger(0);
		}
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
			// Lo intento anotar en el nado con delfines
			int pos = 0;
			
			//Por como lo programe tengo que tener cuidado de no meterlo en una lista de un evento que ya paso
			//Este while no decide en que horario entra, solo verifica que no se meta en uno que ya paso
			while(pos < cantHorarios && horariosNados[pos] <= reloj.getHoraActual())
			{
				//Supuse que los show vienen ordenados de mayor a menor
				pos++;
			}
			
			//Me fijo si lo puedo meter en alguno
			while (pos < cantHorarios && this.cantGenteAnotadaPorTurno[pos].get() >= MAXIMA_GENTE_PILETA) {
				//Si hay mas de 40 personas, paso al siguiente. Si todos los turnos estan llenos
				//en el siguiente if no va a entrar y no va a modificar la variable del visitante
				pos++;
			}
			
			synchronized(this)
			{
				/*Esto esta sincronizado porque pueden llegar a meterse 2 o mas hilos
				al if y incrementar en +1 ambos, lo cual en un caso muy particular
				puede llegar a dejar la cant de gente anotada en 41 o mas. No es que este mal
				si no que se pasa del limite de gente aceptada. Igual funciona si esta por arriba del limite
				*/
			if (pos < cantHorarios) {
				unVisitante.setTurnoDelfines(pos);
				unVisitante.setHoraDelfines(horariosNados[pos]);
				this.cantGenteAnotadaPorTurno[pos].incrementAndGet();
				// Este SOUT es mas para debug. Despues cambiarlo a "Se anoto exitosamente"
				System.out.println(unVisitante.getNombreCompleto() + " - Se anoto en el turno de las " + horariosNados[pos]);
			}else {
				// Si no se pudo anotar, se va no mas
				System.out.println(unVisitante.getNombreCompleto() + " - No se pudo anotar. Se retiro");
			}
			}
			
			
			
			
		} else {
			// Vino porque lo llamaron o por su cuenta para ver si empezo el show
			int horarioTurnoVisitante = unVisitante.getHoraDelfines();
			if (horarioTurnoVisitante == reloj.getHoraActual()
					&& this.cantGenteAnotadaPorTurno[unVisitante.getTurnoDelfines()].get() >= MINIMA_GENTE_EMPEZAR) {
				
				
				// Si es la hora de su nado, se mete
				// Decrementa en uno la cantidad de gente que entro
				this.latchMinGente.countDown();
				
				System.out.println(unVisitante.getNombreCompleto() + " - Entro a la pileta");
				
				synchronized (this) {
					while(showNoComenzo)
					{
						try {
							System.out.println(unVisitante.getNombreCompleto() + " - En 5 empieza el show");
							wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					
					while (showEstaSucediendo) {
						try {
							wait();
							System.out
									.println(unVisitante.getNombreCompleto() + " - Me quiero ir del nado de delfines ");
						} catch (InterruptedException e) {
						}
					}
				}
				
				//Le cambio las variables para que no siga entrando a nadar
				this.cantGenteAnotadaPorTurno[unVisitante.getTurnoDelfines()].decrementAndGet();	//Lo saco
				unVisitante.setTurnoDelfines(-1);
				unVisitante.setHoraDelfines(-1);
				
				System.out.println(unVisitante.getNombreCompleto() + " - Por fin se pudo ir del nado de delfines <------ FIN NadoDelfines");

			} else {
				if (horarioTurnoVisitante < reloj.getHoraActual()) {
					//Si no hay un minimo de gente y pasa de largo en if de arriba, va a pasar una hora y va a entrar aca y se va a ir.
					
					System.out.println(unVisitante.getNombreCompleto() + " - Se le paso su horario del show y se fue. Puede haber sido que no habia gente suficiente para el show anterior tambien");

					// Lo saco de la lista en la que estaba y despues le cambio el ticket

					this.cantGenteAnotadaPorTurno[unVisitante.getTurnoDelfines()].decrementAndGet();
					unVisitante.setTurnoDelfines(-1);
					unVisitante.setHoraDelfines(-1);
				}
				else {
				// Si no, es va
				//System.out.println(unVisitante.getNombreCompleto() + " - Se fue porque no era su turno de delfines");
				}	
			}
		}
	}


	public void comenzarShow() {

		while (reloj.getHoraActual() == ultimoShow || !esHorarioShow()) {
			// No hice un metodo que esperara hasta cierto horario porque me iba a quedar
			// bastante parecido
			reloj.esperarUnaHora();
		}
		ultimoShow = reloj.getHoraActual();
		
		System.out.println("NADODELFIN - Esperando que llegue gente / CountDownLatch");
		// Creo un latch para asegurarme que pasa la gente
		this.latchMinGente = new CountDownLatch(MINIMA_GENTE_EMPEZAR);

		// Primero tiene que cambiar esta variable asi cuando la gente llega al show
		// queda en el wait;
		showEstaSucediendo = true;
		showNoComenzo = false;
		
		synchronized(this)
		{
			notifyAll();
		}
		
		//avisarGente(horarioActual);

		// espera un minimo y despues sale si no llegaron todos
		try {
			latchMinGente.await(1000, TimeUnit.MILLISECONDS);
			System.out.println("NADO DELFINES - Empezando un show");
			reloj.utilizarTiempoEvento();	//simulo el tiempo
		} catch (InterruptedException e) {
			// Estoy usando un catch como if, lo cual es horrible
			System.out.println("NADO DELFINES - No llego la suficiente gente en tiempo y se cancelo el show");
		}
		
		showNoComenzo = true;
		this.showEstaSucediendo = false;
		
		System.out.println("NADO DELFINES - Termino el show");
		
		synchronized (this) {
			notifyAll();
		}
	}

	public boolean esHorarioShow() {
		int posicion = -1;	//apenas entra al horario pasa a ser 0.
		boolean esHorario = false;

		while (!esHorario && posicion < cantHorarios-1) {
			posicion++;
			esHorario = horariosNados[posicion] == reloj.getHoraActual();
		}
		return esHorario;
	}

}
