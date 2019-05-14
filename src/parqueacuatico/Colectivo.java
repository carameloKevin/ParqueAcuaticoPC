/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

import java.util.concurrent.Semaphore;

/**
 *
 * @author Carolina
 */
public class Colectivo {

    private int cantPasajeros; //pasajeros ocupando asientos
    //es muy parecido a TransbordadorGeneral (package Parcial pryct 2017)
    private final int capacidad = 25;// no me deja ponerele que sea final static
    private Semaphore vacio;
    private Semaphore arriba = new Semaphore(0);
    private Semaphore mutex1 = new Semaphore(1);
    private Semaphore lleno = new Semaphore(0);
    private Semaphore bajar = new Semaphore(0);

    //VER QUE PUEDEN IR MENOS DE 25 PERSONAS EN EL COLECTIVO
    //NOOO --->//este constructor sirve por si quiero agrandar o achicar la capacidad del colectivo
    //el colectivo tiene capacidad de hasta 25 personas
    //entonces cantPasajeros<=25
    public Colectivo(int nPasajeros) {
        this.cantPasajeros = nPasajeros;
        this.vacio = new Semaphore(nPasajeros);
    }

    //deberia chequear la cantidad de visitantes para saber cuantos asientos se van a ocupar
    public int getCapacidad() {
        return this.capacidad;
    }
    
    //ver si la cant de visitantes es multiplo de 25 (usar mod 25=0)
    //en caso de que no lo sea, dividir en grupos
    //el ultimo grupo modifica la cant de pasajeros arriba del colectivo
    //deberia usar un set creo
    //si modifico con el constructor, estaría creando coles nuevos
    //la cant de pasajeros arriba deberia ser la max entonces debe ser 25 siempre 
    //solo el ultimo grupo puede tener menos de 25

    public void subir(String nombre) {
        try {
            this.margenIzq(nombre);
            this.vacio.acquire();
            this.mutex1.acquire();
            System.out.println("El visitante " + nombre + " esta subiendo al colectivo");
            Thread.sleep(800);
            System.out.println("El visitante " + nombre + " ya esta en el colectivo");
            this.lleno.release();
            this.arriba.release();
            this.mutex1.release();

        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
    }

    public void ir() {
        try {
            //ir.acquire();
            this.arriba.acquire(this.cantPasajeros);
            System.out.println("El colectivo se dirige hacia el parque");

            Thread.sleep(2500);
            System.out.println("El colectivo ha llegado al parque");
            this.bajar.release(this.cantPasajeros);
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
    }

    private void margenIzq(String id) {
        try {
            System.out.println("El auto " + id + " se acerca por el margen izquierdo");
            Thread.sleep(900);
            System.out.println("El auto " + id + " llegó a la orilla y se fija si puede subir al transbordador");
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }

    }

    private void margenDer(String id) {
        try {
            System.out.println("El auto " + id + " se esta yendo por el margen derecho");
            Thread.sleep(900);
            System.out.println("El auto " + id + " se fue tan rapido que ya no se ve");
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }

    }

    public void bajar(String nombre) {
        try {
            bajar.acquire();
            //this.autosArriba.acquire();
            this.lleno.acquire();//lleno va acá?
            this.mutex1.acquire();
            System.out.println("El visitante " + nombre + " esta bajando");
            Thread.sleep(1000);
            System.out.println("El visitante " + nombre + " ha bajado");

            this.margenDer(nombre);
            this.mutex1.release();
            this.arriba.release();

        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
    }

    public void volver() {
        try {
            this.arriba.acquire(this.cantPasajeros);
            System.out.println("El colectivo esta volviendo a su parada inicial");
            Thread.sleep(2000);
            System.out.println("El colectivo ha vuelto a su parada inicial");
            vacio.release(this.cantPasajeros);//si lo hago antes entonces puede subirse uno cuando en realidad no debería
        } catch (InterruptedException ex) {
            System.out.println(ex.toString());
        }
    }
}
