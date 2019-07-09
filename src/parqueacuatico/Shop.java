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
    
    private final ConcurrentLinkedQueue colaCompras = new ConcurrentLinkedQueue();
    /*No se si puedo hacerlo con lock por el tema que tengo 2 cajas
    private ReentrantLock cajas = new ReentrantLock();*/
    private Semaphore cajas = new Semaphore(2);
    
    public void entrarAComprar(Visitante unVisit)
    {
        try {
            System.out.println(unVisit.getNombre() + " SHOP - Entro a la tienda y esta mirando");
            Thread.sleep(200);
            System.out.println(unVisit.getNombre() + " SHOP - Ya termino de comprar. Yendo a hacer fila para pagar");
        } catch (InterruptedException ex) {
            Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.hacerFila(unVisit);
    }
    
    public void hacerFila(Visitante unVisit){
        System.out.println(unVisit.getNombre() + " SHOP - Se esta metiendo en la fila");
        colaCompras.add(unVisit);
        System.out.println(unVisit.getNombre() + " SHOP - Se metio en la fila");
    }
    
    public void pagarCompra(Visitante unVisit){
        //Sincronizo la primer parte del bloque para poder hacer los notifiy y waits y tambien para que pueda ver la colaCompras sin que se este modificando justo cuando la ve
        synchronized(this)
        {
            if(!(colaCompras.peek().equals(unVisit)))
            {
                try {
                    System.out.println(unVisit.getNombre() + " SHOP - No es mi turno de pagar");
                    //Notifico a otro hilo que este esperando y me duermo
                        this.notify();
                        this.wait();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(unVisit.getNombre() + " SHOP - Intento pagar de nuevo");
            }
        }
        System.out.println(unVisit.getNombre() + "SHOP - Hay una caja libre?");
        
        try {
            //Tomo una de las dos cajas
            cajas.acquire();
            System.out.println(unVisit.getNombre() + " SHOP - Hay una caja libre!");
            System.out.println("DEBUG SHOP " + colaCompras.poll());
            Thread.sleep(100);
            System.out.println(unVisit.getNombre() + " SHOP - Ya termino de pagar!");
        } catch (InterruptedException ex) {
        }finally{
            cajas.release();
        }
        System.out.println(unVisit.getNombre() + " SHOP - Ya libero la caja y se fue");
    }
}
