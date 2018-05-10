package Main;

import Controladores.Singleton;

/**
 *
 * @author Jorge
 */
public class Main {

    public static void main(String[] args) {
        Singleton s = Singleton.getInstance();

        System.out.println("Servidor Corriendo...");

        System.exit(0);
    }

}
