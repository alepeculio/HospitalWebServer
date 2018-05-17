package Controladores;

import Clases.EstadoTurno;
import Clases.HorarioAtencion;
import Clases.Turno;

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

    public static boolean setEstadoTurno(String idTurno, EstadoTurno estado) {
        Turno turno = obtenerTurno(Long.valueOf(idTurno));
        turno.setEstado(estado);
        return Singleton.getInstance().merge(turno);
    }

    public static boolean setClienteActualHA(String idHa, int numeroActual) {
        HorarioAtencion horarioAtencion = obtenerHorarioAtencion(Long.valueOf(idHa));
        horarioAtencion.setClienteActual(numeroActual);
        return Singleton.getInstance().merge(horarioAtencion);
    }

}
