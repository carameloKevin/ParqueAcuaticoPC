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

    private String nom;
    private Colectivo elCole;

    public ConductorCole(String n, Colectivo c) {
        this.nom = n;
        this.elCole=c;
    }

    public void run() {
        while (true) {
            try {
                this.elCole.ir();
                Thread.sleep(2000);
                this.elCole.volver();
            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
    }
}
