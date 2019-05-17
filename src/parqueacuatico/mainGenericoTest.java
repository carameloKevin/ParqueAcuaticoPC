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
   //     Parque unParque = new Parque();
        FaroMirador faro = new FaroMirador();
        
        //Creo y cargo los visitantes (No los ejecuto)
        for(int i = 0; i < 20; i++){
            //cargo los threads al arreglo
            hilos[i] = new Thread(new Visitante("V"+i,faro));
            hilos[i].start();
        }
        
    }
}
