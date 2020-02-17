
package parqueacuatico;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
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
    //private SynchronousQueue<Visitante> escalera;  
    
    	//Cada semaforo representa un tobogan
    	private Semaphore toboganes,subirEscalera;    	
    	private Reloj unReloj;
    
    /*
    Decidi hacerlo con Semaphore porque queda medio feo con integer
    /*Uso un integer porque creo que seria mejor solo verificar si toboganesOcupados != 2
    en vez de hacer la clase y poner ocupado (es casi lo mismo) / 
    private Integer tobaganesOcupados;
    */
    
    public FaroMirador(Reloj elReloj) {
        
        //this.escalera = new SynchronousQueue<Visitante>(true); //true para que sea FIFO
        this.toboganes = new Semaphore(2,true);
        this.subirEscalera = new Semaphore(1,true);
        this.unReloj = elReloj;
    }
    
    public void realizarFaroMirador(Visitante unVisitante){
		System.out.println(unVisitante.getNombreCompleto() + " - Va al faro");
		subirEscalera(unVisitante);
		System.out.println(unVisitante.getNombreCompleto() + " - Ya se subio a la escalera");
		intentoTirarTobogan(unVisitante);
		System.out.println(unVisitante.getNombreCompleto() + " - Ya se tiro y termino");

		System.out.println(unVisitante.getNombreCompleto() + " - Termino Faro Mirador");
    }
    
    public void subirEscalera(Visitante unVisit)
    {
    	
        //Se sube un visitante a la escalera del faro
        System.out.println(unVisit.getNombreCompleto() + " FARO - Visitate Nro " + unVisit.getNombreCompleto() +" INTENTA subir al faro");
        
        try {
            subirEscalera.acquire();
        } catch (InterruptedException ex) {
            Logger.getLogger(FaroMirador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.out.println(unVisit.getNombreCompleto() + " DEBUG - FARO - Visitate Nro " + unVisit.getNombreCompleto() +" TOMO el semaforo exitosamente<-------Esclera");
       
        /*
        try{
            //Se sube a la escalera
            escalera.add(unVisit);
        }finally{
            System.out.println(unVisit.getNombreCompleto() + " DEBUG - FARO - Visitante Nro " + unVisit.getNombreCompleto() + "  LOGRO subir y SOLTO el Lock exitosamente");
            subirEscalera.release();
        }
        */
    }
    
    public void intentoTirarTobogan(Visitante unVisit)
    {
        
        if(unReloj.getHoraActual() > 9 && unReloj.getHoraActual() < 18)
        {
        	System.out.println(unVisit.getNombreCompleto() + " FARO - Es mi turno de tirarme!");
        	tirarseTobogan(unVisit);
        }else {
        	System.out.println(unVisit.getNombreCompleto() + " - Me hecharon del tobogan");
        	subirEscalera.release();
        }
        
    }

    
    private void tirarseTobogan(Visitante unVisit)
    {
        try {
            //Solo toma un semaforo, simula que se tira y suelta el semaforo
        	
            System.out.println(unVisit.getNombreCompleto() + " FARO - Intento obtener el semaforo del tobogan");
            
            toboganes.acquire();
            subirEscalera.release();
            
            System.out.println(unVisit.getNombreCompleto() + " FARO - Obtuve el semaforo del tobogan, me tiro");
            Thread.sleep(800);
        } catch (InterruptedException ex) {
            Logger.getLogger(FaroMirador.class.getName()).log(Level.SEVERE, null, ex);
        }
        toboganes.release();
        System.out.println(unVisit.getNombreCompleto() + " FARO - Ya me tire y termine");
    }
        
    }
