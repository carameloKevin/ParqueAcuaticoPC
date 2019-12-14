package parqueacuatico;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class MundoAventura {

	//LinkedBlockingQueue<Visitante> filaCuerdas, filaTirolesa, filaSalto;
	Semaphore filaCuerdas, filaTirolesa, filaSalto, tirolesaEste, tirolesaOeste;
	Random random = new Random();
	int genteEste = 0, genteOeste = 0;
	Reloj reloj;
	
	public MundoAventura(Reloj elReloj)
	{
		this.filaCuerdas = new Semaphore(1, true);
		this.filaTirolesa = new Semaphore(2, true);
		this.filaSalto = new Semaphore(2, true);
		this.tirolesaEste = new Semaphore(1);
		this.tirolesaOeste = new Semaphore(0);
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
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		filaCuerdas.release();
		System.out.println(unVisitante.getNombreCompleto() +  " - Termino las cuerdas");
		
	}

	private void tirarseTirolesa(Visitante unVisitante) {
		boolean seTiraPorLadoEste = random.nextBoolean();
		
		System.out.println(unVisitante.getNombreCompleto() + " - Comenza a hacer fila para las tirolesas");
		try {
			filaTirolesa.acquire();
			if(seTiraPorLadoEste)
			{
				tirarseLadoEste(unVisitante);
			}else {
				tirarseLadoOeste(unVisitante);
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		filaTirolesa.release();
		
	}

	private void tirarseLadoOeste(Visitante unVisitante) {
		System.out.println(unVisitante.getNombreCompleto() + " - Se quiere tirar por el lado Oeste");
		try {
			if(!tirolesaOeste.tryAcquire(100, TimeUnit.MILLISECONDS))
			{
				tirolesaEste.acquire();
				System.out.println(unVisitante.getNombreCompleto() + " - Parece que nadie viene, asi que la tirolesa fue movida de ESTE -> OESTE");
			}
			System.out.println(unVisitante.getNombreCompleto() + " - Se tiro por la tirolesa");
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(unVisitante.getNombreCompleto() + " - Termino de tirarse por la tirolesa. La tirolesa ahora esta en el Este");
		tirolesaEste.release();
	}

	private void tirarseLadoEste(Visitante unVisitante) {
		System.out.println(unVisitante.getNombreCompleto() + " - Se quiere tirar por el lado Este");
		try {
			if(!tirolesaEste.tryAcquire(100, TimeUnit.MILLISECONDS))
			{
				tirolesaOeste.acquire();
				System.out.println(unVisitante.getNombreCompleto() + " - Parece que nadie viene, asi que la tirolesa fue movida de OESTE -> ESTE");
			}
			System.out.println(unVisitante.getNombreCompleto() + " - Se tiro por la tirolesa");
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(unVisitante.getNombreCompleto() + " - Termino de tirarse por la tirolesa. La tirolesa ahora esta en el Oeste");
		tirolesaOeste.release();
		
	}

	private void tirarseTobogan(Visitante unVisitante) {
		System.out.println(unVisitante.getNombreCompleto() + " - Comenzo a hacer fila para tirarse por un tobogan");
		try {
			filaSalto.acquire();
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(unVisitante.getNombreCompleto() + " - Esta haciendo el salto");

		filaSalto.release();
		System.out.println(unVisitante.getNombreCompleto() + " - Termino el salto");
	}
	
	
}
