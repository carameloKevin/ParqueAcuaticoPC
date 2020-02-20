package parqueacuatico;

import java.util.ArrayList;

/**
 *
 * @author Carolina, Kevin
 */
public class main {

    public static void main(String[] args) {
    	
    	Reloj unReloj = new Reloj();
    	new Thread(unReloj).start();
    	
        Parque elParque = new Parque(unReloj);

        int cantColectivos = 2;
        //int cantVisitantes = (int) (Math.random() * 70) + 10; 
        int cantVisitantes = 2;
        
        //Cargo Colectivos y colectiveros
        int cantAsientosColectivo = 25;
        TransporteHora[] colectivos   = new TransporteHora[cantColectivos];
        Thread[] colectiveros = new Thread[cantColectivos];
        
        
        //Horarios de salida de los colectivos
        int[] horarios = {10 ,11, 12, 14, 16};
        
        //Inicializo los colectivos
        for(int i = 0; i < cantColectivos; i++)
        {
        	colectivos[i] = new TransporteHora("COLECTIVO " + i, cantAsientosColectivo, unReloj, horarios);
        	colectiveros[i] = new Thread(new Chofer("CHOFER_COLECTIVO " + i, colectivos[i]));
        	colectiveros[i].start();
        }
        
        //Cargo los visitantes en threads
        Thread[] hilos = new Thread[cantVisitantes];
        Visitante[] losVisitantes = new Visitante[cantVisitantes];

        for (int i = 0; i < cantVisitantes; i++) {
            //Cargo los visitantes con sus variables
            losVisitantes[i] = new Visitante(""+i, elParque, colectivos[i%cantColectivos]);
            hilos[i] = new Thread(losVisitantes[i]);
            hilos[i].start();
        }
    }

}
