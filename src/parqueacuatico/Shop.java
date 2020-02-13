/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carolina
 */
/**En el caso de ir al shop, éste sería el recurso compartido
 * donde las colas de cada una de las cajas estaría representado mediante
 * 2 variables de condición, una para cada caja.
 *En el shop se pueden adquirir suvenires de distinta clase,
 * los cuales se pueden abonar en una de las dos cajas disponibles.*/


//@Kevin
public class Shop {
    /*-----Funcionamiento basico------
    - Entra un visitante y visita un minimo de 200mili (thread.sleep). De hay depende de cuando se despierte
    - Va a la cola. Hay una cola para dos cajas. Es igual que el faro mirador
    - Tengo que hacerlo con Lock, porque si no es lo exactamente igual que el faroMirador
    */
	
    private Semaphore cajas = new Semaphore(2, true);
    
    public void entrarAComprar(Visitante unVisit)
    {
        try {
            System.out.println(unVisit.getNombreCompleto() + " SHOP - Entro a la tienda y esta mirando");
            Thread.sleep(200);
            System.out.println(unVisit.getNombreCompleto() + " SHOP - Ya termino de comprar. Yendo a hacer fila para pagar");
        } catch (InterruptedException ex) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
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
