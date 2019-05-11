/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tpfinalpc;

/**
 *
 * @author Carolina
 */
public class Visitante implements Runnable {

    private String nombre;
    private boolean colectivo = false;
    private boolean shop = false;//si es false, va directo a las actividades que ingresa por teclado
    // private Shop elShop; -----> no, el parque tiene el shop
    private Parque elParque;
    private Colectivo elCole;
    private int actividadDeseada;
//¿¿¿PODRIA USAR SOLO UN CONSTRUCTOR??? CREO QUE SI..HMM RE PENSAR

    public Visitante(String nom, Parque ecopcs, int bus, int compra) { //cole indica si va en cole true o false //shop indica si va al shop true o false(en ese caso va a las act)
        this.nombre = nom;
        this.elParque = ecopcs;
        if (bus == 1) {//1=true, 0=false;
            this.colectivo = true; //no pongo el else porque colectivo=false por default
        }
//        else{
//            this.colectivo=false;
//        }
        if (compra == 1) {
            this.shop = true;
        }

        if (this.shop == false) {
            this.actividadDeseada = (int) (Math.random() * 4 + 1);
        }
    }

//    public Visitante(String nom, Parque ecopcs) {
//        this.nombre = nom;
//        this.elParque = ecopcs;
//    }
    public void setCole(Colectivo c) {
        this.elCole = c;
    }

    public boolean getVaEnCole() {
        return this.colectivo;
    }
    //el parque tiene el cole, el cole tiene el parque o los tomo como independientes???
    //el visitante sabe el cole que tiene que tomar para ir al parque
    //el visitante sabe a que parque ir
    //no debo verificar que el cole vaya al parque

    public void run() {
        if (this.colectivo) {
            try {
                this.elCole.subir(this.nombre);
                Thread.sleep(1750);
                this.elCole.bajar(this.nombre);

            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
        System.out.println("El visitante " + this.nombre + " ya ha llegado al parque");
        //this.elParque.elijeShop(shop);
        if (shop) {//luego no podria hacer las actividades o si elije las actividades no podria ir al shoppin jejejeje
            System.out.println("El visitante " + this.nombre + " decidio ir al shopping");
        } else {
            System.out.println("El visitante " + this.nombre + " decidio disfrutar de las actividades");//se deberia hacer un while
            this.elParque.mostrarActividades();
            System.out.println("El visitante " + this.nombre + " decidio por la actividad " + this.actividadDeseada);
//            Object actividad;
//            actividad=this.elParque.actividadSeleccionada(actividadDeseada);
            actividadSeleccionada(actividadDeseada);
        }

    }

    //CREO QUE ESTO NO ES RESPONSABILIDAD DEL VISITANTE
    public void actividadSeleccionada(int act) {
        Object acti = null;
        switch (act) {
            case 1:
                acti = this.elParque.getActSnorkel();
                //this.getActSnorkel();
                break;
            case 2:
                acti = this.elParque.getActRestaurante();
                //this.getActRestaurante();
                break;
            case 3:
                acti = this.elParque.getActFaro();
                //this.getActFaro();
                break;
            case 4:
                acti = this.elParque.getActGomones();
                //this.getActGomones();
                break;
        }
        //return acti;
    }

    public void actividadSnorkel() {

    }

    public void actividadRestaurante() {

    }

    public void actividadFaro() {

    }

    public void actividadGomones() {

    }
}
