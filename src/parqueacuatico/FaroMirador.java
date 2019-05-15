
package parqueacuatico;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.concurrent.SynchronousQueue;
/**
 *
 * @author Kevin
 */

    public class FaroMirador{
    /*La escalera es una linkedList para poder implementar sincronizacion alrededor
    Podria haber usar la SynchronousQueue*/
    private LinkedList escalera;  
    private Lock subirEscaleraLock;
    //Cada semaforo representa un tobogan
    private Semaphore toboganes;
    
    /*
    Decidi hacerlo con Semaphore porque queda medio feo con integer
    /*Uso un integer porque creo que seria mejor solo verificar si toboganesOcupados != 2
    en vez de hacer la clase y poner ocupado (es casi lo mismo) / 
    private Integer tobaganesOcupados;
    */
    
    public FaroMirador() {
        
        this.escalera = new LinkedList();
        this.toboganes = new Semaphore(2);
        subirEscaleraLock = new ReentrantLock();
    }
    
    public void subirEscalera(Visitante unVisit)
    {
        //Implementado con LOCK
        //Se sube un visitante a la escalera del faro
        System.out.println("FARO - Visitate Nro " + unVisit.getNombre() +" INTENTA subir al faro");
        subirEscaleraLock.lock();
        System.out.println("DEBUG - FARO - Visitate Nro " + unVisit.getNombre() +" TOMO el Lock exitosamente");
        try{
            //Se sube a la escalera
            escalera.add(unVisit);
            System.out.println("FARO - Visitante Nro " + unVisit.getNombre() + " LOGRO subir");
        }finally{
            System.out.println("DEBUG - FARO - Visitante Nro " + unVisit.getNombre() + "SOLTO el Lock exitosamente");
            subirEscaleraLock.unlock();
        }
    }
    
    public synchronized void intentoTirarTobogan(Visitante unVisit)
    {
        /*!!!!!!Tengo problemas con esto. Si un visitante quiere tirarse, tengo que verificar que sea el primero. Si no lo es
        tiene qu esperar, el tema es que la unica forma que conozco es con el metodo wait, o sea monitores
        */
        
        //Verifico que el visitante que se quiere tirar sea el primero en la fila
        while(!(escalera.getFirst() != null && escalera.getFirst() == unVisit))
        {
            //Si no es el primero, espera.
            try{
                //Notifico a otro que vea si el puede tirarse
                this.notify();
                //Pongo a dormir este thread
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(FaroMirador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //Se tira por el tobogan
        //!!NOTA - Yo puse un el metodo Thread.Sleep() aca dentro. Cuando va a este metodo, sigue el syncronized? mas que nada por el tema que no haga un Sleep mientras esta sincronizado
        this.tirarseTobogan(unVisit);
        
        
    }
    
    public void tirarseTobogan(Visitante unVist)
    {
        try {
            //Solo toma un semaforo, simula que se tira y suelta el semaforo
            toboganes.acquire();
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(FaroMirador.class.getName()).log(Level.SEVERE, null, ex);
        }
        toboganes.release();
    }
        
    }
