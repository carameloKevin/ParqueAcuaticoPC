/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carolina
 */
public class Cajera implements Runnable{
    private int id;
    private Caja laCaja;
    
    public Cajera(int id, Caja caja){
        this.id=id;
        this.laCaja=caja;
    }
    
    public void run(){
        while(true){
            try {
                //this.elBuffer.sacar();
                Thread.sleep((int) (Math.random() * 2500) +500 );
            } catch (InterruptedException ex) {
                Logger.getLogger(Cajera.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
