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
    private String numCol = "00";
    private int cantAsientos;
    private LinkedList pasajeros;
    private boolean estaEnParque = false; //Significa que esta en donde llegan los pasajeros
    private boolean puedenSubir = false;
    private boolean puedenBajar = false;
    private boolean estoyEnParque = false;
    private final Object monitor = new Object();
    
    public Colectivo(int asientos){
        this.cantAsientos = asientos;
        pasajeros = new LinkedList();
    }
    
    public Colectivo(String nombre, int asientos)
    {
        this.numCol = nombre;
        this.cantAsientos = asientos;
        pasajeros = new LinkedList();
    }
    
    public synchronized void esperarAborde(){
        boolean esperarUnPocoMas = true;
        //El chofer espera que la gente se suba
        //El chofer abre la puerta y espera que suba gente
        System.out.println("COLECTIVO - Llego el cole al " + estoyEnParque +". Abriendo puertas para dejar subir >????????????????????????");
        puedenSubir = true;
        /*Le asigno el tiempo despues de que abra la puerta por si le sacan el control
         a  este thread/chofer */
        
        long tiempoActual = System.currentTimeMillis();
        long tiempoFin = tiempoActual + 1 * 1000; //5 segundos
        //Espera que se llenen los asientos de gente o su tiempo
        while(esperarUnPocoMas)
        {
            try {
                notify();
                wait();            //Veo si puede continuar el chofer
                //Si todavia queda tiempo, que se fije si entran mas pasajeros
                if(System.currentTimeMillis() < tiempoFin)
                {
                    //si entran mas pasajeros, va a ser false y va a tener que seguir
                    //esperando.
                    esperarUnPocoMas = pasajeros.size() < cantAsientos;
                }else{
                    //Si no queda tiempo, se va sin importar nada;
                    esperarUnPocoMas = false;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Colectivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        /*
        
        Esta parte no funciona porque esta mal la parte logica del while,
        fijate con una tabla de verdad cuando se puede ir el colectivero;
        
        while(!(pasajeros.size() < cantAsientos) || tiempoActual < tiempoFin ){
            try {
                //Si no puede salir todavia, notifica a los demas y se vuelve a esperar
                System.out.println("Me voy a dormir");
                notify();
                wait();
            } catch (InterruptedException ex) {
                //Logger.getLogger(Colectivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        */
        //Cierra la puerta para que no pueda subir nadie mas
        System.out.println("NO SE SUBE NADIE MAS");
        puedenSubir = false;
    }
    
    public void conducirColectivo(){
        try {
            //Solamente un thread.sleep() para simular que esta yendo al parque
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Colectivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        //esta variable solo sirve para decir donde esta, va a ir en los sout, pero no hace nada especial
        estoyEnParque = !estoyEnParque;
    }
    
    public synchronized void esperarDecenso(){
        //Espera a que se bajen todos, no se va hasta que esto pase
        //Abre las puertas para que se puedan bajar y notifica a los pasajeros
        System.out.println("COLECTIVO / CHOFER Abriendo puertas para que se bajen >????????????????????????");
        puedenBajar = true;
        while(pasajeros.size() > 0){
            try {
                notify();
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Colectivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("COLECTIVO / CHOFER Cerrando puertas, no se baja nadie mas porque no quedo nadie >??????????????????????????????????????");
        puedenBajar = false;
    }
    
    public synchronized void subirAlColectivo(Visitante unPasajero)
    {
    //    System.out.println("COLECTIVO / PASAJERO "+ unPasajero.getNombre() + " se quiere subir" );
        while(!puedenSubir)
        {
    //        System.out.println("COLECTIVO / PASAJERO "+ unPasajero.getNombre() + " No pudo, espera");
            try {
                //Creo que el notifiy no es necesario
                notify();
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Colectivo.class.getName()).log(Level.SEVERE, null, ex);
            }
    //        System.out.println("COLECTIVO / PASAJERO "+ unPasajero.getNombre() + " Ahora si me puedo subir?");
        }
    //    System.out.println("COLECTIVO / PASAJERO "+ unPasajero.getNombre() + " Se esta subiendo tranquilamente");
        pasajeros.add(unPasajero);
    //    System.out.println("COLECTIVO / PASAJERO "+ unPasajero.getNombre() + "Se subio tranquilamente. " +  pasajeros.size() +"<---------------");
        //Intenta notificarle al chofer, pero si no le notifica a otro pasajero que puede subir
        notify();
    }
    
    public synchronized void bajarseDelColectivo(Visitante unPasajero)
    {
        while(!puedenBajar)
        {
    //        System.out.println("COLECTIVO / PASAJERO "+ unPasajero.getNombre() + " No se pudo bajar, espera");
            try {
                notify();
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Colectivo.class.getName()).log(Level.SEVERE, null, ex);
            }
    //        System.out.println("COLECTIVO / PASAJERO "+ unPasajero.getNombre() + " Ahora si se puede bajar?");
        }
        //Saco a este pasajero por un tema de semantica, pero la verdad que podria
        //sacar a cualquiera. Como por ej. al primero (supongo que seria mas optimo)
    //    System.out.println("COLECTIVO / PASAJERO "+ unPasajero.getNombre() + "se esta bajando");
        pasajeros.remove(unPasajero);
    //    System.out.println("COLECTIVO "+ unPasajero.getNombre() +" se bajo. Quedan " + pasajeros.size() + "<-----------");
        //Notifica que se pueden seguir bajando
        notifyAll();
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
