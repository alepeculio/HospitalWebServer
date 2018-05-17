package Controladores;

import Clases.Administrador;
import Clases.Cliente;
import Clases.Empleado;
import Clases.HorarioAtencion;
import Clases.Hospital;
import Clases.Turno;
import Clases.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Jorge
 */
public class CHospital {

    public static boolean eliminarHorarioAtencion(int id) {
        EntityManager em = Singleton.getInstance().getEntity();
        em.getTransaction().begin();
        try {
            em.createNativeQuery("DELETE FROM horarioatencion WHERE id = " + id)
                    .executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("No se eimino el horairo de atencion");
            return false;
        }
        return true;
    }

    public static List<HorarioAtencion> obtenerHorariosAtencion(long idEmpleado, Usuario u) {
        long idHospital = CAdministradores.getAdminByUsuario(u.getId()).getHospital().getId();

        EntityManager em = Singleton.getInstance().getEntity();
        em.getTransaction().begin();
        List<HorarioAtencion> lista = new ArrayList<>();

        try {
            lista = (List<HorarioAtencion>) em.createNativeQuery("SELECT ha.* FROM horarioatencion AS ha, cliente AS c, hospital AS h WHERE ha.empleado_id = c.id AND ha.hospital_id AND c.id = :idEmpleado AND h.id = :idHospital", HorarioAtencion.class)
                    .setParameter("idEmpleado", idEmpleado)
                    .setParameter("idHospital", idHospital)
                    .getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("No se encontraron los horairos de atencion");
        }
        return lista;
    }

    public static boolean agregaHorarioAtencion(Usuario u, int idEmpleado, HorarioAtencion ha) {
        Hospital h = CAdministradores.obtenerHospitalAdministrador(u.getCi());
        if (h == null) {
            return false;
        }

        Empleado e = CUsuario.getEmpleado(idEmpleado);
        if (e == null) {
            return false;
        }

        if (!Singleton.getInstance().persist(ha)) {
            return false;
        }

        h.agregarHA(ha);
        e.agregarHA(ha);
        ha.setEmpleado(e);
        ha.setHospital(h);

        if (!Singleton.getInstance().merge(h)) {
            return false;
        }

        if (!Singleton.getInstance().merge(e)) {
            return false;
        }

        return true;
    }

    public static void borrarAdministrador(String nomHospital, String ciAdmin) {
        Hospital h = obtenerHospital(nomHospital);
        h.removerAdministrador(ciAdmin);
        Singleton.getInstance().merge(h);
    }

    public static void modificarAdministrador(String nomHospital, Usuario u) {
        List<Administrador> administradores = obtenerHospital(nomHospital).getAdministradores();

        for (Administrador a : administradores) {
            if (a.getUsuario().getCi().equals(u.getCi())) {
                a.setUsuario(u);
                Singleton.getInstance().merge(a);
            }
        }
    }

    public static List<Usuario> obtenerAdministradoresHospital(String nomHospital) {
        List<Administrador> administradores = obtenerHospital(nomHospital).getAdministradores();

        if (administradores == null) {
            return null;
        }

        List<Usuario> usuarios = new ArrayList<>();

        for (Administrador a : administradores) {
            usuarios.add(a.getUsuario());
        }

        return usuarios.size() == 0 ? null : usuarios;
    }

    public static String agregarAdministrador(String nomHospital, Usuario u) {
        Hospital h = obtenerHospital(nomHospital);
        List<Administrador> admins = h.getAdministradores();

        if (admins != null) {
            for (Administrador a : admins) {
                if (a.getUsuario().getCi().equals(u.getCi())) {
                    return "C.I. ya existe";
                } else if (a.getUsuario().getCorreo().equals(u.getCorreo())) {
                    return "Correo ya existe";
                }
            }
        }

        h.agregarAdministrador(u);
        Singleton.getInstance().merge(h);
        return "";
    }

    public static void modificarHospital(String nombre, Hospital h) {
        Hospital viejo = obtenerHospital(nombre);
        viejo.setNombre(h.getNombre());
        viejo.setDirectora(h.getDirectora());
        viejo.setPublico(h.isPublico());
        viejo.setCorreo(h.getCorreo());
        viejo.setTelefono(h.getTelefono());
        viejo.setDepartamento(h.getDepartamento());
        viejo.setCalle(h.getCalle());
        viejo.setNumero(h.getNumero());
        viejo.setLatitud(h.getLatitud());
        viejo.setLongitud(h.getLongitud());
        Singleton.getInstance().merge(viejo);
    }

    public static void borrarHospital(String nombre) {
        Hospital h = obtenerHospital(nombre);

        if (h != null) {
            Singleton.getInstance().remove(h);
        }
    }

    public static Hospital obtenerHospital(String nombre) {
        List<Hospital> hospitales = obtenerHospitales();

        for (Hospital h : hospitales) {
            if (h.getNombre().equals(nombre)) {
                return h;
            }
        }

        return null;
    }

    public static List<HorarioAtencion> obtenerHorariosHospital(long idHospital) {
        EntityManager em = Singleton.getInstance().getEntity();
        em.getTransaction().begin();
        List<HorarioAtencion> lista = new ArrayList<>();
        try {
            lista = (List<HorarioAtencion>) em.createNativeQuery("SELECT ha.* FROM horarioatencion AS ha,hospital AS h WHERE ha.hospital_id=h.id AND h.id =:idHospital", HorarioAtencion.class)
                    .setParameter("idHospital", idHospital)
                    .getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("No se encontraron los horairos de atencion");
        }
        return lista;
    }

    public static List<Turno> obtenerTurnosDeUnHorario(long idHorario) {
        EntityManager em = Singleton.getInstance().getEntity();
        em.getTransaction().begin();
        List<Turno> lista = new ArrayList<>();
        try {
            lista = (List<Turno>) em.createNativeQuery("SELECT * FROM turno  WHERE horarioAtencion_id=:idHorario", Turno.class)
                    .setParameter("idHorario", idHorario)
                    .getResultList();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("No se encontraron los turnos ");
        }
        return lista;
    }

    public static List<Hospital> obtenerHospitales() {
        List<Hospital> lista = null;
        Singleton.getInstance().getEntity().getTransaction().begin();
        try {
            lista = Singleton.getInstance().getEntity().createNativeQuery("SELECT * FROM hospital", Hospital.class).getResultList();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {
            Singleton.getInstance().getEntity().getTransaction().rollback();
        }
        return lista;
    }
}
