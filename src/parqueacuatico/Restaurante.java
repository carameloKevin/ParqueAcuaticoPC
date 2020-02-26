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
	private String tipoComida = "Almorzar";
	
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
		//Antes de usar este metodo se tiene que estar seguro que ya no comio aca, esta clase no se hace responsable de eso
		
		//Verifico si ya comio antes o no, para definir si almuerza o merenda
		//**Tambien lo podria haber hecho con los tickets**
		if(unVisitante.getUltimoRestaurante() >= 0)
		{
			tipoComida = "MERENDAR";
		}else {
			tipoComida = "ALMORZAR";
		}
		
		System.out.println(unVisitante.getNombreCompleto() + " - Quiere entrar a " + tipoComida + " a " + this.getNombreCompleto());
		System.out.println(unVisitante.getNombreCompleto() + " - Esta haciendo fila para entrar");
		
		//Este if se puede cambiar por un acquire si queres que se quede siempre parado ahi
		try {
			
				capacidad.acquire();
				System.out.println(unVisitante.getNombreCompleto() + " - Pudo entrar al Restaurante sin problema y comenzo a " + tipoComida);
				
				Thread.sleep(1000);
				
				unVisitante.setUltimoRestaurante(numero);
				capacidad.release();
				System.out.println(unVisitante.getNombreCompleto() + " -  Se esta yendo del restaurante despues de " + tipoComida);
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
