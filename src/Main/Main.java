package Main;

import Controladores.CHospital;
import Controladores.Singleton;
import java.text.ParseException;
import java.util.Timer;

/**
 *
 * @author Jorge
 */
public class Main {

    public static void main(String[] args) throws InterruptedException, ParseException {
        Singleton.getInstance().getEntity().getTransaction();
    }

}
