package parqueacuatico;

import java.util.concurrent.Semaphore;

public class Pileta implements Runnable{
	private int tamanoPileta;
	private Semaphore lugares;
	boolean enPlenoShow = false;
	NadoDelfines eventoDelfines;
	
	public Pileta(int tamano, NadoDelfines eventoDelfines)
	{
		this.eventoDelfines = eventoDelfines;
		this.tamanoPileta = tamano;
		lugares = new Semaphore(tamano);
	}
	
	public boolean reservarLugar()
	{
		boolean aux = false;
		
		if(!enPlenoShow)
		{
			aux = lugares.tryAcquire();
		}
		return aux;
	}
	
	public void liberarLugar()
	{
		lugares.release();
	}

	@Override
	public void run() {
		while(true)
		{
			eventoDelfines.abrirRegistros();
			eventoDelfines.comenzarShow();
			eventoDelfines.terminarShow();
		}
	}
	
}
