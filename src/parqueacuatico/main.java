package parqueacuatico;

import java.util.ArrayList;

/**
 *
 * @author Carolina
 */
public class main {
	/*
	CREAR ARREGLO DE THREADS Y MANDARLE TODO LOS OBJETOS QUE SON HILOS
	*/

    public static void main(String[] args) {
        //verificar si la cantidad de visitantes que quieren ir en cole es <=25
        //si la cant es mayor, suben de a 25 hasta que quede el ultimo monto
        //el ultimomonto es <=25

        //Inicializo los datos 
        Shop shoppingParque = new Shop();//esto creo que no va acá
        Parque elParque = new Parque(shoppingParque);

        //LLEGADA AL PARQUE
	//Inicializo los visitantes, son entre 10 y 80
        int cantVisitantes = (int) (Math.random() * 70) + 10; 
	//Cargo los visitantes en threads mas dos hilos mas para los colectivos
        Thread[] hilos = new Thread[(cantVisitantes + 2)];
        System.out.println("MAIN - Cantidad de visitantes "+hilos.length);
        Visitante[] losVisitantes = new Visitante[cantVisitantes];

        for (int i = 0; i < losVisitantes.length; i++) {
		System.out.println("DEBUG -- MAIN - Cargando visitante nro: "+(i+1));
		losVisitantes[i] = new Visitante(("visitante " + (i+1)), elParque, (int) (Math.random() * 2), (int) (Math.random() * 2)); //directamente puedo poner "visitantes" +i en la parte del constructor que se refiere al nombre
        }//(int) Math.random()*2+1 da de 1-2

        //variable que sea multiplo de la cantidad de visitantes que van en cole
        //sino se queda esperando como en el transbordador
        // Visitante [] visitantesEnCole;
        //separo visitantes que van en cole de los que no
        ArrayList <Visitante> visitantesCole = new ArrayList<>(); //¿?¿?¿?
        ArrayList <Visitante> visitantesNoCole = new ArrayList<>();
        int posVisitCole=0;
        int posVisitNoCole=0;
        for (int i = 0; i < losVisitantes.length; i++) {
            System.out.println("LA POSICION es en visitantes es "+i);
            if (losVisitantes[i].getVaEnCole()) {
                System.out.println("ENTRO en que SIIIII va en cole");
                visitantesCole.add(posVisitCole,losVisitantes[i]);
                System.out.println("Visitantes EN COLE eran "+posVisitCole);
                posVisitCole++;
               System.out.println("Visitantes EN COLE ahora SON "+posVisitCole);
               
            } else {
                System.out.println("ENTRO en que NOOOOO va en cole");
                visitantesNoCole.add(posVisitNoCole,losVisitantes[i]);
                System.out.println("Visitantes NOOOOO COLE eran "+posVisitCole);
                posVisitNoCole++;
                System.out.println("Visitantes NOOOOO COLE ahora SON "+posVisitCole);
            }
            hilos[i] = new Thread(losVisitantes[i]);
        }
        
        System.out.println("PASA POR ACAAAAAAAA");
        
        
        
        
        
        
        
        
        
        
        
        
        //saco la cantidad de asientos que se van a ocupar
        //divido y me fijo cuantos coles de 25 pasajeros voy a usar y luego creo un cole con <25 pasajeros
        Colectivo elColeQueVaLleno = new Colectivo(25);

        ConductorCole choferRoberto = new ConductorCole("Chofer Roberto", elColeQueVaLleno);

        hilos[cantVisitantes] = new Thread(choferRoberto);
        
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
                arrVisitEnCole[i].setCole(elColeConAsientosLibres);
            }
            
        }
        
        
        
        
        
        
        
        
        
        
        //hasta acá es la llegada al parque

        for (int i = 0; i < hilos.length; i++) {
            System.out.println("Empieza el HILO NUMERO "+i);
            hilos[i].start();
        }

    }

}
