package parqueacuatico;

import java.util.Random;

/**
 *
 * @author Carolina
 */
public class Visitante implements Runnable {

	private Reloj elReloj;
	private String nombre;
	private boolean vaEnColectivo = false;
	private boolean vaShoping = false;// si es false, va directo a las actividades que ingresa por teclado
	private Parque elParque;
	private Transporte elColectivo;
	private int actividadDeseada, llave;
	private boolean tieneMochila, dejoMochila;

	private Random random = new Random();

	public Visitante(String nom, Parque ecopcs, Transporte unColectivo) {

		this.nombre = nom;
		this.elParque = ecopcs;
		elReloj = ecopcs.getReloj();
		tieneMochila = random.nextBoolean();
		dejoMochila = false;
		this.vaEnColectivo = random.nextBoolean();
		this.vaShoping = random.nextBoolean();
		this.elColectivo = unColectivo;
	}

	public void run() {
		while (true) {
			
			while (!elParque.estaAbierto()) {
				System.out.println(this.getNombreCompleto() + " - El parque esta cerrado, vuelvo mañana");
				elParque.getReloj().esperarUnaHora();
			}
			

			System.out.println(this.getNombreCompleto() + " - COMIENZO");

			if (vaEnColectivo && this.elColectivo != null) {
				elColectivo.subirPasajero(this);
				elColectivo.bajarPasajero(this);
			} else {
				System.out.println(this.getNombreCompleto() + " - Fue por en su propio medio");
			}

			System.out.println(this.getNombreCompleto() + " - Llego al parque");

			elParque.realizarActividades(this);
		}
	}


	public String getNombreCompleto() {
		return "VISITANTE " + nombre;
	}

	public void setTransporte(Transporte unColectivo) {
		this.elColectivo = unColectivo;
	}

	public boolean getVaEnCole() {
		return this.vaEnColectivo;
	}

	public void dejarEquipamiento(int numLlave) {
		tieneMochila = false;
		dejoMochila = true;
		this.llave = numLlave;
	}

	public boolean getTieneMochila() {
		return tieneMochila;
	}

	public int getLlave() {
		return llave;
	}

	public void recuperarBolso() {
		tieneMochila = true;
		dejoMochila = false;
		this.llave = -1;
	}

	public boolean getDejoMochila() {
		return this.dejoMochila;
	}

}
