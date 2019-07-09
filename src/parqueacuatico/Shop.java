/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

import java.util.concurrent.ConcurrentLinkedQueue;
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
    private ReentrantLock cajas = new ReentrantLock();
    
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
        if(!(colaCompras.peek().equals(unVisit)))
        {
            try {
                System.out.println(unVisit.getNombre() + " SHOP - No es mi turno de pagar");
                notify();
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Shop.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(unVisit.getNombre() + " SHOP - Intento pagar de nuevo");
        }
        
        System.out.println(unVisit.getNombre() + "SHOP - Hay una caja libre?");
        
        
    }
    
    //Entra a comprar
    //Se mete en la fila y espera que sea su turno
    //Como sabe que es su turno? hace un wait hasta que le toque? No puedo despertar a 
}
