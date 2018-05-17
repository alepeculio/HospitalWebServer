package Main;

import Clases.Administrador;
import Clases.Cliente;
import Clases.Cliente;
import Clases.HorarioAtencion;
import Controladores.CAdministradores;
import Controladores.CCliente;
import Controladores.CHospital;
import Controladores.Singleton;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Jorge
 */
public class Main {

    public static void main(String[] args) {
        List<HorarioAtencion> horarios = CHospital.obtenerHorariosHospital(1);
        HorarioAtencion horario = new HorarioAtencion();
        for (HorarioAtencion h : horarios) {
            System.out.println(h.getId());
            if (h.getId() == 1) {
                horario = h;
                break;
            }
        }


        Singleton.getInstance().getEntity().getTransaction();
    }


}
