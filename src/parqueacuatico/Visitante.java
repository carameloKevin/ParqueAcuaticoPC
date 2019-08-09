package parqueacuatico;

/**
 *
 * @author Carolina
 */
public class Visitante implements Runnable {

    private String nombre;
    private boolean vaEnColectivo = false;
    private boolean vaShoping = false;//si es false, va directo a las actividades que ingresa por teclado
    private Shop unShop;
    private Parque elParque;
    private Colectivo elColectivo;
    private int actividadDeseada;
    
    public Visitante(String nom, Parque ecopcs, boolean vaEnBus, boolean vaDeCompra){ //cole indica si va en cole true o false //shop indica si va al shop true o false(en ese caso va a las act)
        this.nombre = nom;
        this.elParque = ecopcs;
        this.unShop = ecopcs.getShop();
        //Modificado Bus para que llegue un boolean directamente
        this.vaEnColectivo = vaEnBus;
        //Idem Bus pero para Shop
        this.vaShoping = vaDeCompra;

        if (this.vaShoping) {
            this.actividadDeseada = (int) (Math.random() * 4 + 1);
        }
    }
    
    //Constructor debug
    public Visitante(String nombre, Colectivo unColectivo)
    {
        this.nombre = nombre;
        this.elColectivo = unColectivo;
        this.vaEnColectivo = true;
    }
    
    public void setCole(Colectivo unColectivo) {
        this.elColectivo = unColectivo;
    }

    public boolean getVaEnCole() {
        return this.vaEnColectivo;
    }
    //el parque tiene el cole, el cole tiene el parque o los tomo como independientes???
    //el visitante sabe el cole que tiene que tomar para ir al parque
    //el visitante sabe a que parque ir
    //no debo verificar que el cole vaya al parque
/*
    public void run() {
        if (this.colectivo) {
            try {
                this.elCole.subir(this.nombre);
                Thread.sleep(1750);
                this.elCole.bajar(this.nombre);

            } catch (InterruptedException ex) {
                System.out.println(ex.toString());
            }
        }
        System.out.println("El visitante " + this.nombre + " ya ha llegado al parque");
        //this.elParque.elijeShop(shop);
        if (shop) {//luego no podria hacer las actividades o si elije las actividades no podria ir al shoppin jejejeje
            System.out.println("El visitante " + this.nombre + " decidio ir al shopping");
        } else {
            System.out.println("El visitante " + this.nombre + " decidio disfrutar de las actividades");//se deberia hacer un while
            this.elParque.mostrarActividades();
            System.out.println("El visitante " + this.nombre + " decidio por la actividad " + this.actividadDeseada);
//            Object actividad;
//            actividad=this.elParque.actividadSeleccionada(actividadDeseada);
            actividadSeleccionada(actividadDeseada);
        }

    }
*/
        //RUN DE DEBUG, AL HACER MERGE ESTE METODO SE TIENE QUE IR
    public void run(){
        int i = 2;
        while(i > 0){
            //Este if podria no ir porque esta el del while. Solo uno de los dos es necesario
            if(vaEnColectivo && this.vaEnColectivo)
            {
//                System.out.println("VISITANTE -- " + this.nombre + " Estoy queriendome SUBIR al colectivo");
                elColectivo.subirAlColectivo(this);
//                System.out.println("VISITANTE -- " + this.nombre + " Estoy queriendome BAJAR del colectivo");
                elColectivo.bajarseDelColectivo(this);
            }
        }
    }
    
    public String getNombre() {
        return nombre;
    }
    //CREO QUE ESTO NO ES RESPONSABILIDAD DEL VISITANTE
    public void actividadSeleccionada(int act) {
        Object acti = null;
        switch (act) {
            case 1:
                acti = this.elParque.getActSnorkel();
                //this.getActSnorkel();
                break;
            case 2:
                acti = this.elParque.getActRestaurante();
                //this.getActRestaurante();
                break;
            case 3:
                acti = this.elParque.getActFaro();
                //this.getActFaro();
                break;
            case 4:
                acti = this.elParque.getActGomones();
                //this.getActGomones();
                break;
        }
        //return acti;
    }

    public void irShopping(){
                //Dejo los comentarios de debug
//                System.out.println(this.getNombre() + " VISITANTE - Entrando a comprar      <------(1)");
                unShop.entrarAComprar(this);
//                System.out.println(this.getNombre() + " VISITANTE - Empezando a hacer fila  <------(2)");
                unShop.hacerFila(this);
//                System.out.println(this.getNombre() + " VISITANTE - Yendo a pagar las compras        <------(3)");
                unShop.pagarCompra(this);
//                System.out.println(this.getNombre() + " VISITANTE - Termine con las compras           <------(4)");
    }
    
    public void actividadSnorkel() {

    }

    public void actividadRestaurante() {

    }

    /*
    
    //Estaba para debugear con un faro local. Tendria que hacer que tome el faro del
    //parque acuatico
    
    public void actividadFaro() {
        System.out.println(this.nombre + " VISITANTE - Me voy al faro");
        faro.subirEscalera(this);
        System.out.println(this.nombre + " VISITANTE - Ya me subi a la escalera");
        faro.intentoTirarTobogan(this);
        System.out.println(this.nombre + " VISITANTE - Ya me tire y termine");
    }
    */
    public void actividadGomones() {

    }
}
