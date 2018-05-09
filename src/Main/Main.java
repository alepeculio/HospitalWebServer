package Main;

import Controladores.Singleton;

/**
 *
 * @author Jorge
 */
public class Main {

    public static void main(String[] args) {
        Singleton.getInstance();
        System.out.println("Servidor Corriendo...");
    }
}
