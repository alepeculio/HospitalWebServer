package Controladores;

import Clases.EstadoTurno;
import Clases.HorarioAtencion;
import Clases.Turno;
import java.time.LocalDateTime;
import java.util.Calendar;
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

        if (ha.getEstado().equals(EstadoTurno.PENDIENTE)) {
            int dia = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            String diaString = "";
            switch (dia) {
                case Calendar.MONDAY:
                    diaString = "Lunes";
                    break;
                case Calendar.TUESDAY:
                    diaString = "Martes";
                    break;
                case Calendar.WEDNESDAY:
                    diaString = "Miercoles";
                    break;
                case Calendar.THURSDAY:
                    diaString = "Jueves";
                    break;
                case Calendar.FRIDAY:
                    diaString = "Viernes";
                    break;
                case Calendar.SATURDAY:
                    diaString = "Sabado";
                    break;
                case Calendar.SUNDAY:
                    diaString = "Domingo";
                    break;
            }
            if (!ha.getDia().equals(diaString)) {
                return "errDia";
            }
            ha.setEstado(EstadoTurno.INICIADO);
            r = "firstTime";
        }

        int turnosFinalizados = 0;
        for (Turno turno : turnos) {
            if (turno.getId() == Long.valueOf(idTurno)) {
                turno.setEstado(estado);
                if (estado == EstadoTurno.FINALIZADO) {
                    ha.setClienteActual(0);
                } else {
                    ha.setClienteActual(turno.getNumero());
                }
            }
            if (turno.getEstado() == EstadoTurno.FINALIZADO) {
                turnosFinalizados++;
            }
        }

        if (turnosFinalizados == turnos.size()) {
            ha.setEstado(EstadoTurno.FINALIZADO);
            r = "lastTime";
        }

        if (Singleton.getInstance().merge(ha)) {
            if ("ERR".equals(r)) {
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
        ha.setClienteActual(0);
        return Singleton.getInstance().merge(ha);
    }
}
