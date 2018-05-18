package Main;

import Controladores.Singleton;
import java.util.Date;

/**
 *
 * @author Jorge
 */
public class Main {

    public static void main(String[] args) {
//        Singleton.getInstance().getEntity().getTransaction();
        
        Date hi = new Date(2018, 5, 16, 12, 0);
        Date hf = new Date(2018, 5, 16, 14, 0);
        int cant = 2;
        System.out.println(((hf.getTime() - hi.getTime()) / cant) / 1000 / 60);
    }

}
