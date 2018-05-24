package Main;

import Clases.Administrador;
import Clases.Cliente;
import Clases.Cliente;
import Clases.HorarioAtencion;
import Clases.TipoTurno;
import Clases.Turno;
import Controladores.CAdministradores;
import Controladores.CCliente;
import Controladores.CHospital;
import Controladores.Singleton;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Jorge
 */
public class Main {

    public static void main(String[] args) {
        List<String> yo = CHospital.obtenerFechasOcupadasyo(4, 1, TipoTurno.VACUNACION);
        for (String t : yo) {
            System.out.println(t);
        }

        Singleton.getInstance().getEntity().getTransaction();
    }

}
