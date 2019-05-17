package parqueacuatico;

/**
 *
 * @author caramelokevin
 */
public class mainGenericoTest {
    
    public static void main(String[] args){
        //Genero unos cuantos visitantes y los mando al juego que yo quiera (hardcodeado)
        Thread[] hilos = new Thread[20];
        //Un parque solo para rellenar campos
        Parque unParque = new Parque();
        
        //Creo y cargo los visitantes (No los ejecuto)
        for(int i = 0; i < 20; i++){
            hilos[i] = new Visitante(i,unParque, true, true);
        }
        
    }
}
