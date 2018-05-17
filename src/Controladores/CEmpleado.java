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

    public static String actualizarHA(String idHA, String idTurno, EstadoTurno estado) {
        HorarioAtencion ha = obtenerHorarioAtencion(Long.valueOf(idHA));
        List<Turno> turnos = ha.getTurnos();
        String r = "ERR";
        int turnosFinalizados = 0;
        for (Turno turno : turnos) {
            if (turno.getId() == Long.valueOf(idTurno)) {
                turno.setEstado(estado);
                ha.setClienteActual(turno.getNumero());
            }
            if (turno.getEstado().equals(EstadoTurno.FINALIZADO)) {
                turnosFinalizados++;
            }
        }

        if (ha.getEstado().equals(EstadoTurno.PENDIENTE)) {
            ha.setEstado(EstadoTurno.INICIADO);
            r = "firstTime";
        }

        if (turnosFinalizados == turnos.size()) {
            ha.setEstado(EstadoTurno.FINALIZADO);
            r = "lastTime";
        }

        if (Singleton.getInstance().merge(ha)) {
            if (!r.equals("firstTime") && !r.equals("lastTime")) {
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
