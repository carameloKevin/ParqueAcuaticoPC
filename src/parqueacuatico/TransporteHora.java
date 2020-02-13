package parqueacuatico;

public class TransporteHora extends Transporte {

	private int[] horarios = new int[5];
	private int ultimaSalida = -1;
	private Reloj elReloj;
	
	
	public TransporteHora(String nro, int cantAsientosLibres, Reloj unReloj, int[] horarios)
	{
		super(nro, cantAsientosLibres);
		this.horarios = horarios;
		elReloj = unReloj;
	}
	
	
	public void esperarSubidaPasajeros() {
		System.out.println(nombreTransporte + " - Comienza esperarSubidaPasajero");
		lock.lock();
		estaEstacion = true;
		
		//Chequeo que ya no haya salido en esta hora para que durante la hora de salida no salga y venga y salga y venga y salga y venga constantemente
		while(ultimaSalida == elReloj.getHoraActual() || !esHoraSalir()) {
			System.out.println(nombreTransporte + "  - Espero a que se suba alguien. Pasajeros actuales: " + cantPasajeros);
			subirse.signalAll();
			lock.unlock();
			elReloj.esperarUnaHora();
			lock.lock();
		}
		System.out.println(nombreTransporte + " - Me estoy yendo de la estacion");
		estaEstacion = false;
		ultimaSalida = elReloj.getHoraActual();
		lock.unlock();
	}
	
	private boolean esHoraSalir()
	{
		boolean valor = false;
		int i = 0;
		
		while(!valor && i < horarios.length)
		{
			valor = (horarios[i] == elReloj.getHoraActual());
			i++;
		}
		
		return valor;
	}
}
