package Main;

import Clases.Cliente;
import Clases.Empleado;
import Clases.HorarioAtencion;
import Clases.TipoTurno;
import Clases.Turno;
import Clases.Usuario;
import Controladores.Singleton;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jorge
 */
public class Main {

    public static void main(String[] args) {
        Singleton s = Singleton.getInstance();

        Usuario u = new Usuario();
        u.setCi("51117521");
        u.setContrasenia("1");
        u.setCorreo("alejandropeculio@gmail.com");

        s.persist(u);
        s.refresh(u);

        System.out.println("ID usuario: " + u.getId());

        Empleado e = new Empleado();
        e.setNombre("Alejandro");
        e.setApellido("Peculio");
        e.setUsuario(u);
        e.setCalle("R.Nolla");
        e.setNumero(1287);
        e.setDiaNacimiento(25);
        e.setMesNacimiento(07);
        e.setAnioNacimiento(1997);
        String[] especialidades = new String[3];
        especialidades[0] = "Pediatra";
        especialidades[1] = "Cirujano";
        especialidades[2] = "Odontologo";
        e.setEspecialidades(especialidades);

        s.persist(e);
        s.refresh(e);

        System.out.println("ID empleado: " + e.getId());

        HorarioAtencion ha = new HorarioAtencion();
        ha.setDia("Lunes");
        ha.setHoraInicio(12);
        ha.setMinInicio(30);
        ha.setHoraFin(16);
        ha.setMinFin(30);
        ha.setClientesMax(15);
        ha.setClienteActual(1);

        s.persist(ha);
        s.refresh(ha);

        System.out.println("ID horario atencion: " + ha.getId());

        List<HorarioAtencion> hsa = new ArrayList<>();
        hsa.add(ha);
        e.setHorariosAtencions(hsa);
        ha.setEmpleado(e);

        s.merge(e);
        s.merge(ha);
        
        Turno t = new Turno();
        t.setFinalizado(false);
        t.setNumero(1);
        t.setTipo(TipoTurno.ATENCION);
        t.setHorarioAtencion(ha);
        
        s.persist(t);
        s.refresh(t);
        
        List<Turno> ts = new ArrayList<>();
        ts.add(t);
        ha.setTurnos(ts);
        
        s.merge(ha);

        System.out.println("Servidor Corriendo...");

        System.exit(0);
    }
}
