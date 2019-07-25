package parqueacuatico;

import java.util.ArrayList;

/**
 *
 * @author Carolina, Kevin
 */
public class main {
	/*
	CREAR ARREGLO DE THREADS Y MANDARLE TODO LOS OBJETOS QUE SON HILOS
	*/

    public static void main(String[] args) {
        //verificar si la cantidad de visitantes que quieren ir en cole es <=25
        //si la cant es mayor, suben de a 25 hasta que quede el ultimo monto
        //el ultimo monto es <=25

        //Inicializo los datos 
        //El parque se crea solo, el shop va dentro de parque
        Parque elParque = new Parque();

        //LLEGADA AL PARQUE
	//Inicializo los visitantes, son entre 10 y 80
        int cantColectivos = 2;
        int cantVisitantes = (int) (Math.random() * 70) + 10; 
	//Cargo los visitantes en threads mas dos hilos mas para los colectivos
        Thread[] hilos = new Thread[(cantVisitantes + cantColectivos)];
        System.out.println("MAIN - Cantidad de visitantes "+ hilos.length);
        Visitante[] losVisitantes = new Visitante[cantVisitantes];

        for (int i = 0; i < losVisitantes.length; i++) {
            System.out.println("DEBUG -- MAIN - Cargando visitante nro: "+(i));
            //Cargo los visitatnes con sus variables
            // 1/3 de los visitantes van en cole y 1/4 van al Shop apenas llegan
            losVisitantes[i] = new Visitante(("Visitante Nro. " + (i)), elParque, (i%3 == 0), (i%4 == 0));
        }
        
        //separo visitantes que van en cole de los que no
        ArrayList <Visitante> visitantesCole = new ArrayList<>();
        
        //es necesario tener esta lista?
        ArrayList <Visitante> visitantesNoCole = new ArrayList<>();
        int posVisitCole=0;
        
        //lo mismo con esto. Este numero es igual a (totalVisit - posVisitCole)
        int posVisitNoCole=0;
        for (int i = 0; i < losVisitantes.length; i++) {
            System.out.println("LA POSICION en visitantes es "+i);
            if (losVisitantes[i].getVaEnCole()) {
                System.out.println("Este visitante SI va en cole");
                visitantesCole.add(posVisitCole,losVisitantes[i]);
                System.out.println("Visitantes TOTAL en el cole eran "+ posVisitCole);
                posVisitCole++;
               System.out.println("Visitantes TOTAL en el cole ahora son"+posVisitCole);
               
            } else {
                /*
             
                La verdad que creo que todo esto puede desaparecer
                
                System.out.println("ENTRO en que NOOOOO va en cole");
                visitantesNoCole.add(posVisitNoCole,losVisitantes[i]);
                System.out.println("Visitantes NOOOOO COLE eran "+posVisitCole);
                posVisitNoCole++;
                System.out.println("Visitantes NOOOOO COLE ahora SON "+posVisitCole);

            */            }
            
            //pongo el visitante en un hilo
            hilos[i] = new Thread(losVisitantes[i]);
        }
        
        System.out.println("DEBUG - MAIN - Se termino la primer parte del main------------------------");
        
        

        //Creo dos colectivos que pueden llevar cualquier cantidad de pasajeros
        //Salen cuando se llenan o despues de un tiempo a su destino
        
        int cantAsientosColectivo = 25;
        Colectivo[] colectivos   = new Colectivo[cantColectivos];
        ConductorCole[] choferes = new ConductorCole[cantColectivos];
        
        //le asigno los colectivos a los choferes
        for (int i = cantColectivos; i >= 0; i++) {
            colectivos[i] = new Colectivo(cantAsientosColectivo);
            choferes[i] = new ConductorCole("Chofer " + i, colectivos[i]);
            hilos[cantVisitantes + i] = new Thread(choferes[i]);
        }
        
        //--------------------->Falta arreglar de aca para abajo<-----------------
        
        System.out.println("YYYY   TAMBIEN POR ACA");
        if (visitantesCole.size() > 25) {
            //obtengo el cociente entre la cant de visitantes y 25 para saber cant de coles
            int cantCole25 = visitantesCole.size() / 25;

            int cantPasajerosMenor25 = visitantesCole.size() % 25;
            Colectivo elColeConAsientosLibres = new Colectivo(cantPasajerosMenor25);
            ConductorCole choferPatricio = new ConductorCole("Chofer Patricio", elColeConAsientosLibres);
            hilos[cantVisitantes + 1] = new Thread(choferPatricio);
        }
        //le otorgo el cole a cada visitante que va en cole, primero lo paso a un arreglo
       // Object [] visitEnCole= visitantesCole.toArray();
       
       //VER COMO METO ESTO ARRIBA, EN LA PARTE DEL IF!!!!!!
       
       Visitante [] arrVisitEnCole= new Visitante[visitantesCole.size()] ;
       int largoDeVisitantesCole=visitantesCole.size();
        for(int i=0; i<largoDeVisitantesCole;i++){
            arrVisitEnCole[i]=visitantesCole.get(i);
            if(largoDeVisitantesCole-25>=0){
                arrVisitEnCole[i].setCole(elColeQueVaLleno);
            }else{
 //               arrVisitEnCole[i].setCole(elColeConAsientosLibres);
            }
        
          
        */
        
        }
        
        
        
        
        
        
        
        
        
        
        //hasta acá es la llegada al parque

        for (int i = 0; i < hilos.length; i++) {
            System.out.println("Empieza el HILO NUMERO "+i);
            hilos[i].start();
        }

    }

}
