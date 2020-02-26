/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kevin
 */


//@Kevin
public class ShopSemaforos {
	
    private Semaphore cajas = new Semaphore(2, true);
    
    public void entrarAComprar(Visitante unVisit)
    {
        try {
            System.out.println(unVisit.getNombreCompleto() + " SHOP - Entro a la tienda y esta mirando");
            Thread.sleep(500);
            System.out.println(unVisit.getNombreCompleto() + " SHOP - Ya termino de comprar. Yendo a hacer fila para pagar");
        } catch (InterruptedException ex) {
            Logger.getLogger(ShopSemaforos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void pagarCompra(Visitante unVisitante)
    {
    	System.out.println(unVisitante.getNombreCompleto() + " - Esta por empezar a hacer fila para comprar");
    	try {
			cajas.acquire();
	    	System.out.println(unVisitante.getNombreCompleto() + " - Esta pagando su compra");
	    	Thread.sleep(500);
	    	cajas.release();
	    	System.out.println(unVisitante.getNombreCompleto() + " - Termino las compras");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    
   	public void realizarShop(Visitante unVisitante) {

		entrarAComprar(unVisitante);
		pagarCompra(unVisitante);
		
	}
}
