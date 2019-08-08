package parqueacuatico;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author caramelokevin
 */
public class Colectivo {
    int cantAsientos;
    LinkedList pasajeros;
    boolean estaEnParque = false; //Significa que esta en donde llegan los pasajeros
    boolean puedenSubir = false;
    boolean puedenBajar = true;
    boolean estoyEnParque = false;
    
    public Colectivo(int asientos){
        this.cantAsientos = asientos;
        pasajeros = new LinkedList();
    }
    
    public synchronized void esperarAborde(){
        //El chofer espera que la gente se suba
        //El chofer abre la puerta y espera que suba gente
        System.out.println("COLECTIVO - Llego el cole al " + estoyEnParque +". Abriendo puertas para dejar subir");
        puedenSubir = true;
        /*Le asigno el tiempo despues de que abra la puerta por si le sacan el control
         a  este thread/chofer */
        
        long tiempoActual = System.currentTimeMillis();
        long tiempoFin = tiempoActual + 5 * 1000; //5 segundos
        //Espera que se llenen los asientos de gente o su tiempo
        while(pasajeros.size() < cantAsientos || tiempoActual < tiempoFin ){
            try {
                //Si no puede salir todavia, notifica a los demas y se vuelve a esperar
                notify();
                wait();
            } catch (InterruptedException ex) {
                //Logger.getLogger(Colectivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Cierra la puerta para que no pueda subir nadie mas
        puedenSubir = false;
    }
    
    public void conducirColectivo(){
        try {
            //Solamente un thread.sleep() para simular que esta yendo al parque
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Colectivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        //esta variable solo sirve para decir donde esta, va a ir en los sout, pero no hace nada especial
        estoyEnParque = !estoyEnParque;
    }
    
    public synchronized void esperarDecenso(){
        //Espera a que se bajen todos, no se va hasta que esto pase
        //Abre las puertas para que se puedan bajar y notifica a los pasajeros
        puedenBajar = true;
        while(pasajeros.size() > 0){
            try {
                notify();
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Colectivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        puedenBajar = false;
    }
    
    public synchronized void subirAlColectivo(Visitante unPasajero)
    {
        while(!puedenSubir)
        {
            try {
                //Creo que el notifiy no es necesario
                notify();
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Colectivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        pasajeros.add(unPasajero);
        //Intenta notificarle al chofer, pero si no le notifica a otro pasajero que puede subir
        notify();
    }
    
    public synchronized void bajarseDelColectivo(Visitante unPasajero)
    {
        while(!puedenBajar)
        {
            try {
                notify();
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Colectivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Saco a este pasajero por un tema de semantica, pero la verdad que podria
        //sacar a cualquiera. Como por ej. al primero (supongo que seria mas optimo)
        pasajeros.remove(unPasajero);
        //Notifica que se pueden seguir bajando
        notify();
    }
}

/* LISTA DE PASOS
    - El chofer espera que se suban, por un tiempo o si se llena
    - La gente se va a subiendo mientras el chofer este esperando
    - El chofer maneja, la gente se queda sentada sin hacer nada
    - El chofer llega, la gente se baja. El chofer espera hasta que no quede nadie
    - El chofer espera un tiempo o a que se llene antes de irse.
    - Vuelve al punto de inicio con gente. Se vacia y despues vuelve a repetir el ciclo
*/
