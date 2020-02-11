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
public class Asistente implements Runnable {

    private String id;
    private NadoSnorkel actSnorkel;
    
    public Asistente(String nombre, NadoSnorkel actSnorkel) {
    	this.actSnorkel = actSnorkel;
    	this.id = nombre;
    }
    
    public void run()
    {
    		System.out.println(this.getNombreCompleto() + " - Se esta por parar en el stand de Snorkel para entregar cosas");
    		
    		while(true)
    		{
    			actSnorkel.entregarEquipo(this);
    		}
    }
    
    public String getNombreCompleto()
    {
    	return "AsistenteSnorkel " + id;
    }
}
