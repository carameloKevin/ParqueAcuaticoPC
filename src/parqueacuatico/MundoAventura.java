package parqueacuatico;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;

public class MundoAventura {

	//LinkedBlockingQueue<Visitante> filaCuerdas, filaTirolesa, filaSalto;
	Semaphore filaCuerdas, filaTirolesa, filaSalto, tirolesaEste, tirolesaOeste, mutex;
	int esperandoEste = 0;
	int esperandoOeste = 0;
	boolean pasoUltOeste;
	Random random = new Random();
	int genteEste = 0, genteOeste = 0;
	Reloj reloj;
	
	public MundoAventura(Reloj elReloj)
	{
		this.filaCuerdas = new Semaphore(1, true);
		this.filaTirolesa = new Semaphore(2, true);
		this.filaSalto = new Semaphore(2, true);
		
		this.tirolesaEste = new Semaphore(0);
		this.tirolesaOeste = new Semaphore(1);
		this.mutex = new Semaphore(1);
		reloj = elReloj;
	}
	
	public void realizarMundoAventura(Visitante unVisitante) {
		boolean salioForzado = true;
		
		System.out.println(unVisitante.getNombreCompleto() + " - Comenzo Mundo Aventura");
		
		//Siempre verifica la hora que es para no pasarse de las 18
		
		
		if(reloj.getHoraActual() < 17)
		{
			this.hacerCuerdas(unVisitante);	
			
			if(reloj.getHoraActual() < 18)
			{
				this.tirarseTirolesa(unVisitante);
				
				if(reloj.getHoraActual() < 18)
				{
					this.tirarseTobogan(unVisitante);
					salioForzado = false;
				}
			}
		}
		
		if(salioForzado)
		{
			System.out.println(unVisitante.getNombreCompleto() + " - Fue hechado del Mundo Aventura porque estan cerrando el parque");
		}
		System.out.println(unVisitante.getNombreCompleto() + " - Termino el mundo aventura");
	}

	
	
	private void hacerCuerdas(Visitante unVisitante) {
		System.out.println(unVisitante.getNombreCompleto() + " - Esta haciendo fila para las cuerdas de Mundo Aventura");
		try {
			filaCuerdas.acquire();
			
			System.out.println(unVisitante.getNombreCompleto() + " - Esta haciendo las cuerdas en Mundo Aventura");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		filaCuerdas.release();
		System.out.println(unVisitante.getNombreCompleto() +  " - Termino las cuerdas");
		
	}

	private void tirarseTirolesa(Visitante unVisitante) {
		boolean seTiraPorLadoEste = random.nextBoolean();
		
		System.out.println(unVisitante.getNombreCompleto() + " - Comenza a hacer fila para las tirolesas");
//		try {
			//filaTirolesa.acquire();
			
			
			if(seTiraPorLadoEste)
			{
				tirarseLadoEste(unVisitante);
			}else {
				tirarseLadoOeste(unVisitante);
			}

//		} catch (InterruptedException e) {
//		e.printStackTrace();
//		}

//		filaTirolesa.release();
		
	}

	private void tirarseLadoOeste(Visitante unVisitante) {
		
		try {
			mutex.acquire();
			
			System.out.println(unVisitante.getNombreCompleto() + " - LLego por el OESTE");
			esperandoOeste++;
			
			if(esperandoEste == 0 && pasoUltOeste)
			{
				System.out.println(unVisitante.getNombreCompleto() + " - No hay nadie del lado ESTE, asi que volvio ESTE -> OESTE");
				tirolesaOeste.release();
				tirolesaEste.acquire();
			}
		}catch(InterruptedException e)
		{
		}
		
		mutex.release();
		
		try {
			tirolesaOeste.acquire();
			System.out.println(unVisitante.getNombreCompleto() + " Se esta tirando por OESTE -> ESTE");
			Thread.sleep(1000);
			System.out.println(unVisitante.getNombreCompleto() + " Termine de pasar");
			tirolesaEste.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			mutex.acquire();
			pasoUltOeste = true;
			esperandoOeste--;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mutex.release();
	}

	private void tirarseLadoEste(Visitante unVisitante) {
		
		try {
			mutex.acquire();
			
			System.out.println(unVisitante.getNombreCompleto() + " - LLego por el ESTE");
			esperandoEste++;
			
			if(esperandoOeste == 0 && !pasoUltOeste)
			{
				System.out.println(unVisitante.getNombreCompleto() + " - No hay nadie del lado ESTE, asi que volvio OESTE -> ESTE");
				tirolesaEste.release();
				tirolesaOeste.acquire();
			}
		}catch(InterruptedException e)
		{
		}
		
		mutex.release();
		
		try {
			tirolesaEste.acquire();
			System.out.println(unVisitante.getNombreCompleto() + " Se esta tirando por ESTE -> OESTE");
			Thread.sleep(1000);
			System.out.println(unVisitante.getNombreCompleto() + " Termine de pasar");
			tirolesaOeste.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			mutex.acquire();
			pasoUltOeste = false;
			esperandoEste--;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		mutex.release();
	}

	private void tirarseTobogan(Visitante unVisitante) {
		System.out.println(unVisitante.getNombreCompleto() + " - Comenzo a hacer fila para tirarse por un tobogan");
		try {
			filaSalto.acquire();
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(unVisitante.getNombreCompleto() + " - Esta haciendo el salto");

		filaSalto.release();
		System.out.println(unVisitante.getNombreCompleto() + " - Termino el salto");
	}	
}







