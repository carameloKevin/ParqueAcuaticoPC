/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

/**
 *
 * @author Carolina
 */
public class ConductorCole implements Runnable {

    private String nombre;
    private Colectivo elColectivo;

    public ConductorCole(String nombre, Colectivo unColectivo) {
        this.nombre = nombre;
        this.elColectivo=unColectivo;
    }

    @Override
    public void run() {
        //Maneja el colectivo ida y vuelta indeterminadamente. Falta el sistema de tiempo;
        while (true) {
            //Cuando empieza, esta esperando para ir al parque
            //pero para la segunda vuelta esta esperando para irse del parque
            System.out.println("COLECTIVERO " + this.nombre + " Estoy esperando que SUBAN");
            elColectivo.esperarAborde();
            //conducir el cole solo pierde tiempo, es para simular
            System.out.println("COLECTIVERO " + this.nombre + " Estoy MANEJANDO el colectivoe");
            elColectivo.conducirColectivo();
            System.out.println("COLECTIVERO " + this.nombre + " Estoy esperando que se BAJEN todos");
            elColectivo.esperarDecenso();
        }
    }
}
