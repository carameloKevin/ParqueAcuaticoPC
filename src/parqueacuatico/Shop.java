/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin
 */

//@Kevin
public class Shop {

	private LinkedBlockingQueue<Visitante> fila = new LinkedBlockingQueue<Visitante>();
	private int CANTCAJERAS = 1;

	
	public Shop()
	{
		inicializarCajeras();
	}
	
	public void inicializarCajeras()
	{
		for(int i = 0; i < CANTCAJERAS; i++)
		{
			new Thread(new Cajera(""+i, this)).start();;
		}
	}
	
	/*
	 * INICIO METODOS VISITANTE
	 */
	
	public void entrarAComprar(Visitante unVisit) {
		try {
			System.out.println(unVisit.getNombreCompleto() + " SHOP - Entro a la tienda y esta mirando");
			Thread.sleep(1000);
			System.out.println(
					unVisit.getNombreCompleto() + " SHOP - Ya termino de comprar. Yendo a hacer fila para pagar");
		} catch (InterruptedException ex) {
			Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public synchronized void pagarCompra(Visitante unVisitante) {
		System.out.println(unVisitante.getNombreCompleto() + " - Esta por empezar a hacer fila para comprar");

		fila.offer(unVisitante);	//Se pone en la fila

			while (!unVisitante.getEsPrimeroFila()) {
				System.out.println(unVisitante.getNombreCompleto() + " - Estoy esperando a ser atendido");
				
				notify(); //Le avisa a la cajera (puede pasar que le avise a otro visitante)
				
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println(unVisitante.getNombreCompleto() + " - Me llamaron, me acerco a la caja");
			unVisitante.setEsPrimeroFila(false);
			unVisitante.setEstaEnCaja(true);
			System.out.println(unVisitante.getNombreCompleto() + " - Esta siendo atendido");
		
			notify();
		while(unVisitante.getEstaEnCaja())
		{
			try {
				
				System.out.println(unVisitante.getNombreCompleto() + " - Todavia me estan atendiendo");
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		notify();
		System.out.println(unVisitante.getNombreCompleto() + " - Termino las compras");
	}

	public void realizarShop(Visitante unVisitante) {

		entrarAComprar(unVisitante);
		pagarCompra(unVisitante);

	}
	
	/*
	 * FIN METODOS VISITANTES
	 */
	
	/*
	 * INICIO METODOS CAJERA
	 */
	public void atenderCaja(Cajera unaCajera) {
		Visitante aux;
		synchronized(this)
		{
		//Si no hay nadie
		while(fila.isEmpty())
		{
			System.out.println(unaCajera.getNombreCompleto() + " - No hay trabajo");
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
		System.out.println(unaCajera.getNombreCompleto() + " - Hay trabajo! le digo al siguiente visitante que pase");
		
		//Lo saco y le aviso que es el primero
		aux = fila.poll();
		aux.setEsPrimeroFila(true);
		
		
		//AtenderCaja
		notify();
		while(!aux.getEstaEnCaja())
		{
			System.out.println(unaCajera.getNombreCompleto() + " - Esperando que se acerque a la caja " + aux.getNombreCompleto());
			try {
				
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		}
		System.out.println(unaCajera.getNombreCompleto() + " - Esta atendiendo a un cliente");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(unaCajera.getNombreCompleto() + " - Esta terminando de atender a un cliente");
		
		//LiberarCaja y liberar visitante
		aux.setEstaEnCaja(false);
		synchronized(this)
		{
			notify();
		}
		System.out.println(unaCajera.getNombreCompleto() + " - Termino de atender al cliente " + aux.getNombreCompleto());
	}
}
