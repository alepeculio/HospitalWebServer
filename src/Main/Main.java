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
        //Timer time = new Timer();
        //TareasProgramadas tp = new TareasProgramadas();
        //time.schedule(tp, 0, 10000);// cada 10seg

        //String s = CHospital.agregarTurno("Hospital Casero", 3, "2018-06-27", 3, "General", "Miércoles : 12:00 - 17:00");
         String s1 = CHospital.agregarTurno("Hospital Casero", 3, "2018-06-27", 3, "Ginecologo", "Miércoles : 12:00 - 17:00");
          //String s2 = CHospital.agregarTurno("Hospital Casero", 7, "2018-06-27", 3, "Ginecologo", "Miércoles : 12:00 - 17:00");
        //System.out.println(s);
        System.out.println(s1);
        //System.out.println(s2);
    }

}
