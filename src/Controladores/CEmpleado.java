package Controladores;

import Clases.EstadoTurno;
import Clases.HorarioAtencion;
import Clases.Turno;
import java.util.List;

public class CEmpleado {

    public static Turno obtenerTurno(long id) {
        Turno turno = null;
        try {
            if (!Singleton.getInstance().getEntity().getTransaction().isActive()) {
                Singleton.getInstance().getEntity().getTransaction().begin();
            }
            turno = (Turno) Singleton.getInstance().getEntity().createNativeQuery("SELECT * FROM turno WHERE id=" + id, Turno.class)
                    .getSingleResult();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {
            if (Singleton.getInstance().getEntity().getTransaction().isActive()) {
                Singleton.getInstance().getEntity().getTransaction().rollback();
            }
            System.err.println("No se puedo encontrar el turno con id: " + id);
        }
        return turno;
    }

    public static HorarioAtencion obtenerHorarioAtencion(long id) {
        HorarioAtencion horarioAtencion = null;
        try {
            if (!Singleton.getInstance().getEntity().getTransaction().isActive()) {
                Singleton.getInstance().getEntity().getTransaction().begin();
            }
            horarioAtencion = (HorarioAtencion) Singleton.getInstance().getEntity().createNativeQuery("SELECT * FROM horarioatencion WHERE id=" + id, HorarioAtencion.class)
                    .getSingleResult();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {
            if (Singleton.getInstance().getEntity().getTransaction().isActive()) {
                Singleton.getInstance().getEntity().getTransaction().rollback();
            }
            System.err.println("No se puedo encontrar el horario de atencion con id: " + id);
        }
        return horarioAtencion;
    }

   /* public static boolean setEstadoTurno(String idTurno, EstadoTurno estado) {
        Turno turno = obtenerTurno(Long.valueOf(idTurno));
        turno.setEstado(estado);
        return Singleton.getInstance().merge(turno);
    }*/

    /*public static String setClienteActualHA(String idHa, int numeroActual) {
        HorarioAtencion horarioAtencion = obtenerHorarioAtencion(Long.valueOf(idHa));
        horarioAtencion.setClienteActual(numeroActual);
        String r = "ERR";
        if (horarioAtencion.getEstado().equals(EstadoTurno.PENDIENTE)) {
            horarioAtencion.setEstado(EstadoTurno.INICIADO);
            r = "firstTime";
        }

        if (Singleton.getInstance().merge(horarioAtencion)) {
            if (!r.equals("firstTime")) {
                r = "OK";
            }
        }

        return r;
    }*/

    public static String actualizarHA(String idHA, String idTurno, EstadoTurno estado) {
        HorarioAtencion ha = obtenerHorarioAtencion(Long.valueOf(idHA));
        List<Turno> turnos = ha.getTurnos();
        String r = "ERR";

        for (Turno turno : turnos) {
            if (turno.getId() == Long.valueOf(idTurno)) {
                turno.setEstado(estado);
                ha.setClienteActual(turno.getNumero());
            }
        }
        if (ha.getEstado().equals(EstadoTurno.PENDIENTE)) {
            ha.setEstado(EstadoTurno.INICIADO);
            r = "firstTime";
        }

        if (Singleton.getInstance().merge(ha)) {
            if (!r.equals("firstTime")) {
                r = "OK";
            }
        }
        return r;
    }

    public static boolean finalizarHA(String idHA) {
        HorarioAtencion ha = obtenerHorarioAtencion(Long.valueOf(idHA));
        List<Turno> turnos = ha.getTurnos();
        for (Turno turno : turnos) {
            turno.setEstado(EstadoTurno.FINALIZADO);
        }
        ha.setTurnos(turnos);
        ha.setEstado(EstadoTurno.FINALIZADO);
        return Singleton.getInstance().merge(ha);
    }
}
