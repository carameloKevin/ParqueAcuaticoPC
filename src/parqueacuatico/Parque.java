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
public class Parque {

    private Shop elShop;
    private Restaurante elRestaurante = new Restaurante();
    private MainSnorkel actSnorkel;
    private FaroMirador elFaroTobogan = new FaroMirador();
    private CarreraGomones laCarreraGomones = new CarreraGomones();
    private boolean irAShop;

    public Parque(Shop elShopping) {
        this.elShop = elShopping;
    }

    public void elijeShop(boolean ir) {
        this.irAShop = ir;
    }

    public void mostrarActividades() {
        System.out.println("La actividad 1 es para ir a hacer snorkel a la laguna");
        System.out.println("La actividad 2 es para ir al restaurante");

        System.out.println("La actividad 3 es para ir al faro mirador");

        System.out.println("La actividad 4 es para hacer la carrera de gomones \n");

    }

//    public Object actividadSeleccionada(int act) {
//        Object acti=null;
//        switch (act) {
//            case 1:
//               acti= this.actSnorkel;
//                //this.getActSnorkel();
//                break;
//            case 2:
//                acti=this.elRestaurante;
//                //this.getActRestaurante();
//                break;
//            case 3:
//                acti=this.elFaroTobogan;
//                //this.getActFaro();
//                break;
//            case 4:
//                acti=this.laCarreraGomones;
//                //this.getActGomones();
//                break;
//        }
//        return acti;
//    }
//
    public Object getActSnorkel() {
        return this.actSnorkel;
    }

    public Object getActRestaurante() {
        return this.elRestaurante;
    }

    public Object getActFaro() {
        return this.elFaroTobogan;
    }

    public Object getActGomones() {
        return this.laCarreraGomones;
    }
}
