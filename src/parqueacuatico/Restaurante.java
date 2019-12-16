/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Carolina
 */
class Restaurante {
    
	private int numero = -1;
	private Semaphore capacidad = new Semaphore(30, true);
	
	public Restaurante(int numRes)
	{
		this.numero = numRes;	
	}
	
	public boolean comioEnRestaurante(Visitante unVisitante)
	{
		return this.numero == unVisitante.getUltimoRestaurante();
	}
	
	public void comerRestaurante(Visitante unVisitante)
	{
		//Antes de usar este metodo se tiene que estar seguro que ya no comio aca, este metodo no se hace responsable de eso
		System.out.println(unVisitante.getNombreCompleto() + " - Quiere entrar a comer a " + this.getNombreCompleto());
		System.out.println(unVisitante.getNombreCompleto() + " - Esta haciendo fila para entrar");
		
		//Este if se puede cambiar por un acquire si queres que se quede siempre parado ahi
		try {
			if(capacidad.tryAcquire(1000, TimeUnit.MILLISECONDS)) {
				
				System.out.println(unVisitante.getNombreCompleto() + " - Pudo entrar al Restaurante sin problema y comenzo a comer");
				
				Thread.sleep(1000);
				
				unVisitante.setUltimoRestaurante(numero);
				capacidad.release();
				System.out.println(unVisitante.getNombreCompleto() + " -  Se esta yendo del restaurante despues de comer");
			}else {
				System.out.println(unVisitante.getNombreCompleto() + " - Se canso de esperar y se fue");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println(unVisitante.getNombreCompleto() + " - Termino la actividad de Restaurante");
	}
	
	public String getNombreCompleto()
	{
		return "RESTAURANTE " + numero;
	}
}
