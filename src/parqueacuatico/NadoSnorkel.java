/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

//import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
//import parqueacuatico.EquipoSnorkel;

/**
 *
 * @author Carolina
 */
class NadoSnorkel {
	private final int CANT_ASISTENTES = 3;
	private final int CANT_EQUIPOS = 2;
	private LinkedBlockingQueue<EquipoSnorkel> colaDisponibleStand; // hace referencia a una cola de equipos de snorkel
																	// completos disponibles en el stand//si uso
																	// exchanger creo que debe ser sychronizedqueue
	private LinkedBlockingQueue<EquipoSnorkel> colaEnUso;// hace referencia a una cola de equipos de snorkel completos
															// en uso
	private LinkedBlockingQueue<Visitante> filaEspera;
	private boolean hayColaPorEquipo;

	public NadoSnorkel() {
		EquipoSnorkel aux;

		this.filaEspera = new LinkedBlockingQueue<Visitante>();
		this.colaDisponibleStand = new LinkedBlockingQueue<EquipoSnorkel>(CANT_EQUIPOS);
		this.colaEnUso = new LinkedBlockingQueue<EquipoSnorkel>(CANT_EQUIPOS);

		for (int i = 0; i < CANT_EQUIPOS; i++) {
			
			aux = new EquipoSnorkel();
			this.colaDisponibleStand.add(aux);
		}

		this.hayColaPorEquipo = false;
	}

	// le avisa al asistente que hay alguien para atender

	// saco el elemento de la cola, pero no se si eliminarlo o si sacar el objeto
	// creo que en realidad me conviene sacar el objeto, que el asistente se lo pase
	// al visitante
	// que el visitante lo use, y luego que se lo devuelva al asistente asi
	// luego el asistente puede devolver el equipo a la queue que corresponde y
	// sigue siendo el mismo objeto
	// esto me hace pensar que puedo usar un exchanger pero kcioooo

	public synchronized void entregarEquipo(Asistente unAsistente) {

		System.out.println(unAsistente.getNombreCompleto() + " - Estoy esperando para entregar un Snorkel");
		while(filaEspera.isEmpty() || colaDisponibleStand.isEmpty()) {	//Ver tabla de verdad para entender esto
	//		System.out.println(unAsistente.getNombreCompleto() + " " + filaEspera.isEmpty() + " -- " + colaDisponibleStand.isEmpty());
			
			notify();
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println(unAsistente.getNombreCompleto() + " - Estoy entregando un Snorkel a un visitante");
		// Saco un snorkel de lo que estan disponibles y lo pongo en la lista de esta en uso
		EquipoSnorkel aux = colaDisponibleStand.poll();
		colaEnUso.add(aux);
		// Le digo al primer visitante que estaba que tiene su equipo y lo saco de la fila
		
		filaEspera.poll().setTieneEquipoSnorkel(true);
		notify();
		System.out.println(unAsistente.getNombreCompleto() + " - Listo, entregue el equipo");
	}

	public synchronized void solicitarEquipo(Visitante unVisitante) {
		filaEspera.add(unVisitante);
		
		System.out.println(unVisitante.getNombreCompleto() + " - Ya se anoto a la lista de espera. Esperando recibir equipo");
		while (!unVisitante.getTieneEquipoSnorkel()) {
			
			System.out.println(unVisitante.getNombreCompleto() + " Eseprando entreguen snorkel");
			notifyAll();
			
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized void devolverEquipo(Visitante unVisitante) {
		// Cualquier visitante retorna cualquier Snorkel y lo devuelve directamente, no
		// pasa por la manos de un asistente
		this.colaDisponibleStand.add(this.colaEnUso.poll());
		unVisitante.setTieneEquipoSnorkel(false);
		notifyAll();
	}

	public void realizarNadoSnorkel(Visitante unVisitante) {
		System.out.println(unVisitante.getNombreCompleto() + " - Comenzo actividad Nado con Snorkel");

		solicitarEquipo(unVisitante);

		System.out.println(unVisitante.getNombreCompleto() + " - Ya recibio su equipo. Comenzo a nadar");
		try {
			Thread.sleep(700);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println(unVisitante.getNombreCompleto() + " - ya terminó de hacer Snorkel y va a devolver el equipo");

		devolverEquipo(unVisitante);

		System.out.println(unVisitante.getNombreCompleto() + " - ya devolvió el equipo. Fin Nado con Snorkel");

	}
}
