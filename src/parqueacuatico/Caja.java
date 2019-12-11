/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parqueacuatico;

import java.util.ArrayList;

/**
 *
 * @author Carolina
 */
class Caja {

    private ArrayList items = new ArrayList();

    public void poner(Object x) {

        System.out.println("Se esta poniendo el item " + (this.items.size() + 1));

        this.items.add(x);

        System.out.println("Se ha puesto el item " + (this.items.size()));

    }

    public int posUltimoPuesto() {
        return this.items.size();
    }

    public boolean esVacio() {
        return this.items.isEmpty();
    }

//	public int posAct() {
//		return posActual;
//	}
//	public int cantItems() {
//		return items.length;
//	}
    public void sacar() {

        System.out.println("Se esta SACANDO el item " + this.posUltimoPuesto());
//		this.items[posActual] = null;
//		this.posActual = this.posActual - 1;
//				Thread.sleep(100);//simula tiempo
        this.items.remove(this.items.size() - 1);

        System.out.println("Se esta SACÓ el item " + (this.posUltimoPuesto() + 1));
        //System.out.println("Se ha sacado un item");
    }
}
