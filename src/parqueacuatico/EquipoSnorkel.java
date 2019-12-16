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
public class EquipoSnorkel {
    private int patasRana;
    private int salvavidas;
    private int snorkel;
    
    public EquipoSnorkel(){
        this.patasRana=2;
        this.salvavidas=1;
        this.snorkel=1;
    }
    //supongo que los siguientes métodos servirían en caso de que se pierda algo del equipo, etc
    public void setPatasRana(int npatas){
        this.patasRana=npatas;
    }
    //en teoría no debería tener más de un salvavidas por eso el if, sino imprime un cartel el método pero no retorna nada
    public void setSalvavidas(int nsalvavidas){
        if(nsalvavidas>1 && nsalvavidas<0){
            System.out.println("Esa cantidad de salvavidas no es posible para un equipo en si");
        }else{
            this.salvavidas=nsalvavidas;
        }
    }
    //idem logica del metodo de arriba
    public void setSnorkel(int nsnorkel){
        if(nsnorkel>1 && nsnorkel<0){
            System.out.println("Esa cantidad de salvavidas no es posible para un equipo en si");
        }else{
            this.snorkel=nsnorkel;
        }
    }
    
    public boolean equipoCompleto(){
        boolean completo=false;
        if(patasRana==2 && salvavidas==1 && snorkel==1){
           completo=true;
        }
        return completo;
    }
}
