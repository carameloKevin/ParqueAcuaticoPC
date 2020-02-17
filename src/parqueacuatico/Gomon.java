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
	boolean estaListo = false;
	CarreraGomones laBaseGomones;
	Carrera carreraParticipa;
	
	public Gomon(String nro, int cantAsientosLibres, int cantMinimaGente, CarreraGomones baseGomones)
	{
		super(nro, cantAsientosLibres, cantMinimaGente);
		laBaseGomones = baseGomones;
	}
	
	public void esperarSubidaPasajeros()
	{
		super.esperarSubidaPasajeros();
		estaListo = true;
		laBaseGomones.ponerEnSalidaGomon(this);
	}
	
	public void viajar()
	{
		super.viajar();
		posicionFinal = carreraParticipa.getPosicion();
		System.out.println(this.nombreTransporte + " - Llego en " + posicionFinal);
	}
	
	public void setEstaListo(boolean estado)
	{
		this.estaListo = false;
	}
	
	public boolean getEstaListo()
	{
		return estaListo;
	}
	
	public void setCarreraParticipa(Carrera unaCarrera)
	{
		carreraParticipa = unaCarrera;
	}
}
