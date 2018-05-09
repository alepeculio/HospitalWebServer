package Main;

import Clases.Administrador;
import Clases.Cliente;
import Clases.Empleado;
import Clases.HorarioAtencion;
import Clases.Hospital;
import Clases.Suscripcion;
import Clases.TipoTurno;
import Clases.Turno;
import Clases.Usuario;
import Clases.Vacunacion;
import Controladores.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
