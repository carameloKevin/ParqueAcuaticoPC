package parqueacuatico;

import java.util.LinkedList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Carrera {
	private int cantMinArrancar = 3;
	
	private AtomicBoolean yaComenzo = new AtomicBoolean(false);
	private AtomicInteger posicionCarrera = new AtomicInteger(0);
	private CyclicBarrier barrera = new CyclicBarrier(cantMinArrancar);
	
	public Carrera(int cantMin)
	{
		barrera = new CyclicBarrier(cantMin);
	}
	
	public void agregarGomonASalida(Gomon unGomon)
	{
		//Se tiene que fijar afuera del metodo de que la carrera no haya comenzado.
		
		System.out.println(unGomon.getNombre() + " - Llego al inicio de la carrera");
		unGomon.setCarreraParticipa(this);
		try {
			barrera.await(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			System.out.println("Llegaron todos a la barrera y salieron");
		} catch (TimeoutException e) {
			System.out.println("Se cansaron de esperar y se largo la carrera! <---------------");
		}
		yaComenzo.set(true);
		System.out.println(unGomon.getNombre() + " - Arranco la carrera!");
	}
	
	public boolean getYaComenzo()
	{
		return yaComenzo.get();
	}
	
	public int getPosicion()
	{
		return posicionCarrera.addAndGet(1);
	}
}
