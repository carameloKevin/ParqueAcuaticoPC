
package parqueacuatico;
import java.util.LinkedList;
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
    //Cada semaforo representa un tobogan
    private Semaphore toboganes,subirEscalera, mutex;
    
    /*
    Decidi hacerlo con Semaphore porque queda medio feo con integer
    /*Uso un integer porque creo que seria mejor solo verificar si toboganesOcupados != 2
    en vez de hacer la clase y poner ocupado (es casi lo mismo) / 
    private Integer tobaganesOcupados;
    */
    
    public FaroMirador() {
        
        this.escalera = new LinkedList();
        this.toboganes = new Semaphore(2,true);
        this.subirEscalera = new Semaphore(1,true);
        this.mutex = new Semaphore(1);
    }
    
    public void subirEscalera(Visitante unVisit)
    {
        //Implementado con Semaforo
        //Se sube un visitante a la escalera del faro
        System.out.println(unVisit.getNombre() + " FARO - Visitate Nro " + unVisit.getNombre() +" INTENTA subir al faro");
        try {
            subirEscalera.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(FaroMirador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(unVisit.getNombre() + " DEBUG - FARO - Visitate Nro " + unVisit.getNombre() +" TOMO el semaforo exitosamente");
        
        try{
            //Se sube a la escalera
            escalera.add(unVisit);
        }finally{
            System.out.println(unVisit.getNombre() + " DEBUG - FARO - Visitante Nro " + unVisit.getNombre() + "  LOGRO subir y SOLTO el Lock exitosamente");
            subirEscalera.release();
        }
    }
    
    public void intentoTirarTobogan(Visitante unVisit)
    {
        /*!!!!!!Tengo problemas con esto. Si un visitante quiere tirarse, tengo que verificar que sea el primero. Si no lo es
        tiene que esperar, el tema es que la unica forma que conozco es con el metodo wait, o sea monitores
        */
        
        //Verifico que el visitante que se quiere tirar sea el primero en la fila
        //Verifico en un metodo afuera para evitar hacer el synchronized con el Thread.sleep() de tirarseTobogan
        System.out.println(unVisit.getNombre() + " FARO - Soy el primero para tirarme?");
        verificarTobogan(unVisit);
        
        //Se tira por el tobogan
        //!!NOTA - Yo puse un el metodo Thread.Sleep() aca dentro. Cuando va a este metodo, sigue el syncronized? mas que nada por el tema que no haga un Sleep mientras esta sincronizado
        System.out.println(unVisit.getNombre() + " FARO - Es mi turno de tirarme!");
        tirarseTobogan(unVisit);
        
        
    }

    private synchronized void verificarTobogan(Visitante unVisit){
            while(!(escalera.getFirst() != null && escalera.getFirst() == unVisit))
        {
            //Si no es el primero, espera.
            try{
             //   System.out.println(unVisit.getNombre() + " FARO - No es mi turno de tirarme. Me voy a dormir");
                //Notifico a otro que vea si el puede tirarse
                this.notifyAll();
                //Pongo a dormir este thread
                this.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(FaroMirador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void tirarseTobogan(Visitante unVisit)
    {
        try {
            //Solo toma un semaforo, simula que se tira y suelta el semaforo
            System.out.println(unVisit.getNombre() + " FARO - Intento obtener el semaforo del tobogan");
            toboganes.acquire();
            //Lo quito de la cola de la escalera
            mutex.acquire();
                escalera.remove(unVisit);
            mutex.release();
            System.out.println(unVisit.getNombre() + " FARO - Obtuve el semaforo del tobogan, me tiro /duermo 200 mili <---------------------");
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(FaroMirador.class.getName()).log(Level.SEVERE, null, ex);
        }
        toboganes.release();
        System.out.println(unVisit.getNombre() + " FARO - Ya me tire y termine, solte el semaforo ----------------->");
    }
        
    }
