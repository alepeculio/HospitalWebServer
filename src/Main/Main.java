package Main;

import Controladores.Singleton;
import java.util.Date;

/**
 *
 * @author Jorge
 */
public class Main {

    public static void main(String[] args) {
        Singleton.getInstance().getEntity().getTransaction();
       // CCorreo.enviar ("alejandropeculio@gmail.com", "Prueba", "Testeando\nJejejejeje\nhttps://www.google.com/");
    }

}
