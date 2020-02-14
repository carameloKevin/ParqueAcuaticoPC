package parqueacuatico;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

public class Gomon extends Transporte {

	CyclicBarrier laBarrera;
	AtomicInteger laPos;
	int posicionFinal = -1;
	
	public Gomon(String nro, int cantAsientosLibres, int cantMinimaGente, CyclicBarrier unaBarrera, AtomicInteger unaPos)
	{
		super(nro, cantAsientosLibres, cantMinimaGente);
		laBarrera = unaBarrera;
		laPos = unaPos;
	}
	
	public void esperarSubidaPasajeros()
	{
		super.esperarSubidaPasajeros();
		
			try {
				laBarrera.await(1000, TimeUnit.MILLISECONDS);
				System.out.println(this.nombreTransporte + " - Salio en la carrera!");
			} catch (TimeoutException e) {
				System.out.println(this.nombreTransporte + " - Se canso de esperar y salio en su propia carrera!");
				e.printStackTrace();
			}
			catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			} catch (BrokenBarrierException e) {
			
			e.printStackTrace();
		}
	}
	
	public void viajar()
	{
		super.viajar();
		posicionFinal = laPos.addAndGet(1);
		System.out.println(this.nombreTransporte + " - Llego en " + posicionFinal);
	}
}
