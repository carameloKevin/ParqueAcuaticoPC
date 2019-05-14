
package parqueacuatico;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
//import java.util.concurrent.SynchronousQueue;
/**
 *
 * @author Kevin
 */

    public class FaroMirador{
    /*La escalera es una linkedList para poder implementar sincronizacion alrededor
    Podria haber usar la SynchronousQueue*/
    private LinkedList escalera;
    /*Uso un integer porque creo que seria mejor solo verificar si toboganesOcupados != 2
    en vez de hacer la clase y poner ocupado (es casi lo mismo)*/ 
    private Integer tobaganesOcupados;
    private Lock subirEscaleraLock;
    
    public FaroMirador() {
        
        this.escalera = new LinkedList();
        this.tobaganesOcupados = 0;
        subirEscaleraLock = new ReentrantLock();
    }
    
    public void subirEscalera(Visitante unVisit)
    {
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
    
    public void tirarsePorTobogan(Visitante unVisit)
    {
        
    }
        
    }
