
package parqueacuatico;
import java.util.LinkedList;
import java.util.concurrent.locks;
//import java.util.concurrent.SynchronousQueue;
/**
 *
 * @author Kevin
 */

    public class FaroMirador implements Runnable {
    /*La escalera es una linkedList para poder implementar sincronizacion alrededor
    Podria haber usar la SynchronousQueue*/
    private LinkedList escalera;
    /*Uso un integer porque creo que seria mejor solo verificar si toboganesOcupados != 2
    en vez de hacer la clase y poner ocupado (es casi lo mismo)*/ 
    private Integer tobaganesOcupados;
    
    public FaroMirador() {
        this.escalera = new LinkedList();
        this.tobaganesOcupados = 0;
    }
    
    @Override
    public void run() {
        //Recibe visitantes que tienen que entrar a la escalera y esperar su turno
        
        //El de adelante del todo esta esperando que se pueda usar UN tobogan, no me importa cual
        //Cuando se larga, el de atras pasa a esperar para tirarse del tobogan
        
    }
        
    }
