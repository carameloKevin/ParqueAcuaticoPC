package parqueacuatico;

import java.util.concurrent.Semaphore;

public class Pileta implements Runnable{
	private Semaphore lugares = new Semaphore(10);
	boolean enPlenoShow = false;
	NadoDelfines eventoDelfines;
	
	public Pileta(NadoDelfines eventoDelfines)
	{
		this.eventoDelfines = eventoDelfines;
	}
	
	public boolean reservarLugar()
	{
		boolean aux = enPlenoShow;
		
		if(!aux)
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
			eventoDelfines.terminarShow();
		}
	}
}
