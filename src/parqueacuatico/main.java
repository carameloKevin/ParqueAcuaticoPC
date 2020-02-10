package parqueacuatico;

import java.util.ArrayList;

/**
 *
 * @author Carolina, Kevin
 */
public class main {

    public static void main(String[] args) {
    	
        Parque elParque = new Parque();

        int cantColectivos = 2;
        int cantVisitantes = (int) (Math.random() * 70) + 10; 
        //int cantVisitantes = 2;
        
        //Cargo Colectivos y colectiveros
        int cantAsientosColectivo = 25;
        Transporte[] colectivos   = new Transporte[cantColectivos];
        Thread[] colectiveros = new Thread[cantColectivos];
        
        for(int i = 0; i < cantColectivos; i++)
        {
        	colectivos[i] = new Transporte("COLECTIVO " + i, cantAsientosColectivo);
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
