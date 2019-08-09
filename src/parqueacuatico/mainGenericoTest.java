package parqueacuatico;

/**
 *
 * @author caramelokevin
 */
public class mainGenericoTest {
    
    public static void main(String[] args){
        //Genero unos cuantos visitantes y los mando al juego que yo quiera (hardcodeado)
        Thread[] hilos = new Thread[21];
        //Un parque solo para rellenar campos
   //     Parque unParque = new Parque();
        Colectivo unColectivo = new Colectivo("31", 25);
        //Creo y cargo los visitantes (No los ejecuto)
        for(int i = 0; i < 2; i++){
            //cargo los threads al arreglo
            hilos[i] = new Thread(new Visitante("V"+i, unColectivo));
            hilos[i].start();
        }
        ConductorCole chofer = new ConductorCole("Jose", unColectivo);
        hilos[20] = new Thread(chofer);
        hilos[20].start();
        
        
    }
}
