package Controladores;

import Clases.Administrador;
import Clases.Cliente;
import Clases.Empleado;
import Clases.EstadoTurno;
import Clases.HorarioAtencion;
import Clases.Hospital;
import Clases.TipoTurno;
import Clases.Turno;
import Clases.Usuario;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public static List<HorarioAtencion> obtenerHorariosConTurnosDisp(long idHospital) {
        List<HorarioAtencion> Todos = obtenerHorariosHospital(idHospital);
        List<HorarioAtencion> fin = new ArrayList<>();

        for (HorarioAtencion ha : Todos) {
            if (ha.getClientesMax() != ha.getTurnos().size()) {
                fin.add(ha);
            }

        }
        return fin;
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
        if (!Singleton.getInstance().getEntity().getTransaction().isActive()) {
            Singleton.getInstance().getEntity().getTransaction().begin();
        }

        try {
            lista = Singleton.getInstance().getEntity().createNativeQuery("SELECT * FROM hospital", Hospital.class).getResultList();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {

            if (Singleton.getInstance().getEntity().getTransaction().isActive()) {
                Singleton.getInstance().getEntity().getTransaction().rollback();
            }

        }
        return lista;
    }

    public static String agregarTurno(String hospital, long idUsuario, String dia) throws ParseException {
        Hospital h = obtenerHospital(hospital);
        List<HorarioAtencion> ha = h.getHorarioAtencions();
        List<String> horarios = new ArrayList<>();
        List<Turno> turnosDia = new ArrayList<>();

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dia);
        } catch (ParseException ex) {
            Logger.getLogger(CHospital.class.getName()).log(Level.SEVERE, null, ex);
        }
        Locale spanishLocale = new Locale("es", "ES");
        String dayOfWeek = new SimpleDateFormat("EEEE", spanishLocale).format(date);

        for (HorarioAtencion hs : ha) {

            //aca estaban los horarios
            if (hs.getDia().toLowerCase().equals(dayOfWeek)) {

                List<Turno> turnos = hs.getTurnos();
                Cliente c = CCliente.getClientebyUsuario(idUsuario);
                Turno t = new Turno();
                String[] array = dia.split("-");
                //
                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                Date dd = formato.parse(dia);

                //
                Date d = new Date(Integer.valueOf(array[0]), Integer.valueOf(array[1]), Integer.valueOf(array[2]));
                Format f = new SimpleDateFormat("MMMM");
                String mes = f.format(d);
                //para horarios
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");

                if (turnos.size() == hs.getClientesMax()) {
                    return "NOPE";
                } else if (turnos.isEmpty()) {

                    t.setCliente(c);
                    t.setEstado(EstadoTurno.PENDIENTE);
                    t.setHorarioAtencion(hs);
                    t.setNumero(1);
                    t.setFecha(dd);
                    t.setTipo(TipoTurno.ATENCION);
                    hs.agregarTurno(t);
                    c.agregarTurno(t);
                    //Singleton.getInstance().persist(t);
                    //Singleton.getInstance().merge(hs);
                    //Singleton.getInstance().merge(c);         

                    return "Su turno ha sido reservado para el día " + array[2] + " de " + mes + " del " + array[0] + " a las " + dateFormat.format(hs.getHoraInicio()) + "hs";
                } else {

                    for (Turno ts : turnos) {
                        if (ts.getCliente().getId() == c.getId() && ts.getFecha().compareTo(dd) == 0 && hs.getTipo() == TipoTurno.ATENCION) {
                            return "Usted ya posee un turno para ese día.";
                        }

                        if (ts.getFecha().compareTo(dd) == 0) {
                            turnosDia.add(ts);
                        }
                    }

                    //obtener la hora del turno
                    for (int i = 0; i < hs.getClientesMax(); i++) {
                        Date hi = hs.getHoraInicio();
                        Date hf = hs.getHoraFin();
                        Date hsss = Date.from(Instant.ofEpochMilli(hi.getTime() + ((hf.getTime() - hi.getTime()) / hs.getClientesMax()) * i));

                        horarios.add(dateFormat.format(hsss));

                    }//fin de horarios

                    t.setCliente(c);
                    t.setEstado(EstadoTurno.PENDIENTE);
                    t.setHorarioAtencion(hs);
                    t.setFecha(dd);
                    t.setNumero(turnosDia.size() + 1);
                    t.setTipo(TipoTurno.ATENCION);
                    hs.agregarTurno(t);
                    c.agregarTurno(t);
                    //Singleton.getInstance().persist(t);
                    //Singleton.getInstance().merge(hs);
                    //Singleton.getInstance().merge(c);
                    return "Su turno ha sido reservado para el día " + array[2] + " de " + mes + " del " + array[0] + " a las " + horarios.get(t.getNumero() - 1) + "hs";

                }
            }

        }
        return "";
    }

    public static String horariosOcupados(String hospital, long idUsuario) {
        Hospital h = obtenerHospital(hospital);
        List<HorarioAtencion> ha = h.getHorarioAtencions();
        String resultado = "";

        HashMap<String, List<String>> fechas = new HashMap<>();

        for (HorarioAtencion hs : ha) {

            if (hs.getTipo() == TipoTurno.ATENCION && hs.getEstado() == EstadoTurno.PENDIENTE && hs.getEmpleado().getId() == idUsuario) {

                List<Turno> turnos = hs.getTurnos();
                 List<Turno> semana ;
                

                for (Turno t : turnos) {

                    if (t.getTipo() == TipoTurno.ATENCION) {

                        if (turnos.size() == hs.getClientesMax()) {
                            if (fechas.get("fecha") == null) {
                                fechas.put("fecha", new ArrayList<String>());
                            }

                            DateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                            fechas.get("fecha").add(formato.format(t.getFecha()));
                        }

                    }

                }

            }
        }

        return "";
    }
}
