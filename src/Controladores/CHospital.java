package Controladores;

import Clases.Administrador;
import Clases.Cliente;
import Clases.Empleado;
import Clases.EstadoSuscripcion;
import Clases.EstadoTurno;
import Clases.HorarioAtencion;
import Clases.Hospital;
import Clases.Suscripcion;
import Clases.TipoTurno;
import Clases.Turno;
import Clases.Usuario;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import sun.util.calendar.Gregorian;

/**
 *
 * @author Jorge
 */
public class CHospital {

    //TODO: Poner tildes
    private static final String[] DIAS = {"Domingo", "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado"};

    private static int opd(String dia) {
        for (int i = 0; i < DIAS.length; i++) {
            if (DIAS[i].equals(dia)) {
                return i;
            }
        }
        return 0;
    }

    public static String obtenerDiasNoDisponibles(long idEmpleado, long idHospital, TipoTurno tipo) {
        List<HorarioAtencion> hs = CHospital.obtenerHorariosAtencion(idEmpleado, idHospital);

        String dias = "";

        for (HorarioAtencion h : hs) {
            if (h.getTipo() == tipo) {
                dias += h.getDia();
            }
        }

        String res = "";

        for (String dia : DIAS) {
            if (!dias.contains(dia)) {
                res += opd(dia) + ",";
            }
        }

        if (res.charAt(res.length() - 1) == ',') {
            res = res.substring(0, res.length() - 1);
        }

        return res;
    }

    public static String obtenerFechasOcupadasJorge(long idEmpleado, long idHospital, TipoTurno tipo) {
        // dias guarda String: Nombre del dia, Integer: cantidad de horarios que tiene ese dia
        HashMap<String, Integer> dias = new HashMap<>();
        // fechas guarda String: fecha#id_horario, Integer: cantidad de turnos vendidos para esa fecha en ese horario
        HashMap<String, Integer> fechas = new HashMap<>();

        List<HorarioAtencion> hs = obtenerHorariosAtencion(idEmpleado, idHospital);

        for (HorarioAtencion h : hs) {
            // Si el tipo de horario no es del especificado no se cuenta
            if (h.getTipo() != tipo) {
                continue;
            }

            // Por cada "Lunes", "Martes", etc. voy guardando cuantos horarios de atencion tiene
            if (dias.get(h.getDia()) == null) {
                dias.put(h.getDia(), 1);
            } else {
                dias.put(h.getDia(), dias.get(h.getDia()) + 1);
            }

            // Si ese horario tiene algun turno ocupado
            if (h.getTurnos() != null && h.getTurnos().size() > 0) {
                // Los recorro a todos y guardo por cada turno#id_horario cuantos tiene en el HashMap fechas
                for (Turno t : h.getTurnos()) {
                    Date date = t.getFecha();
                    // Aca es el fecha#id_horario
                    String turno_por_horario = new SimpleDateFormat("yyyy-MM-dd").format(date) + "#" + h.getId();

                    if (fechas.get(turno_por_horario) == null) {
                        fechas.put(turno_por_horario, 1);
                    } else {
                        fechas.put(turno_por_horario, fechas.get(turno_por_horario) + 1);
                    }
                }
            }
        }

        // horariosOcupados guarda String: fecha, Integer: cantidad de horarios ocupados en esa fecha
        HashMap<String, Integer> horariosOcupados = new HashMap<>();

        // Recorro todos los pares fechas#id_horario
        Iterator i = fechas.entrySet().iterator();
        while (i.hasNext()) {
            // p: cada cosa en el HashMap (el p.getKey me da lo de la izq y el p.getValue lo de la der)
            Map.Entry<String, Integer> p = (Map.Entry<String, Integer>) i.next();

            // A la key la separo en la parte de la fecha y la parte del id_horario
            String parteKeyFecha = p.getKey().split("#")[0];
            int parteKeyIdHA = Integer.valueOf(p.getKey().split("#")[1]);
            // Obtengo el horario de atencion
            HorarioAtencion ha = null;
            for (HorarioAtencion asdasf : hs) {
                if (asdasf.getId() == parteKeyIdHA) {
                    ha = asdasf;
                }
            }
            // Si el valor de esa fecha (la cantidad de turnos vendidos es igual (o mayor por las dudas) a la cantidad max del turno entonces agrego uno al numero de horarios ocupados en esa fecha)
            // IMPORTANTE: sumo 1 a la cantidad de horarios ocupados en esa fecha
            if (ha != null && p.getValue() >= ha.getClientesMax()) {
                if (horariosOcupados.get(parteKeyFecha) == null) {
                    horariosOcupados.put(parteKeyFecha, 1);
                } else {
                    horariosOcupados.put(parteKeyFecha, horariosOcupados.get(parteKeyFecha) + 1);
                }
            }
        }

        List<String> fechasOcupadas = new ArrayList<>();

        // Recorro toda la lista de turnos ocupados
        Iterator i2 = horariosOcupados.entrySet().iterator();
        while (i2.hasNext()) {
            // Recordatorio p: en String: fecha, Integer: horarios ocupados
            Map.Entry<String, Integer> p = (Map.Entry<String, Integer>) i2.next();

            try {
                // Obtengo el nombre del dia de esa fecha
                String dia = obtenerDiaEspanol(new SimpleDateFormat("yyyy-MM-dd").parse(p.getKey()));

                // Si la cantidad de horarios ocupados en esa fecha es igual a la cantidad de turnos en ese dia (Ver que contiene el HashMap dia mas arriba)
                // Entonces significa que esa fecha en particular esta completamente vendida
                if (dias.get(dia) != null && p.getValue() >= dias.get(dia)) {
                    // Se agrega a las fechas ocupadas
                    fechasOcupadas.add(p.getKey());
                }
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        }

        // Se separan por # como el rompe huevo de Luis queria
        String coso = "";
        for (int j = 0; j < fechasOcupadas.size(); j++) {
            coso += fechasOcupadas.get(j) + (j != fechasOcupadas.size() - 1 ? "#" : "");
        }
        return coso;
    }

    public static String obtenerDiaEspanol(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        int dia = cal.get(Calendar.DAY_OF_WEEK);
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
        return diaString;
    }

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
        return obtenerHorariosAtencion(idEmpleado, idHospital);
    }

    public static List<HorarioAtencion> obtenerHorariosAtencion(long idEmpleado, long idHospital) {
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
    

    public static Hospital obtenerHospital(long idHosp) {
        List<Hospital> hospitales = obtenerHospitales();

        for (Hospital h : hospitales) {
            if (h.getId() == idHosp) {
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

    public static List<Suscripcion> obtenerSuscripcionesbyUsuarioAdminHospital(Long idAdminHospital) {
        Administrador admin = CAdministradores.getAdminByUsuario(idAdminHospital);
        return admin.getHospital().getSuscripciones();
    }
    
    public static boolean actualizarSuscripcion (long idSus, EstadoSuscripcion estado) {
        Suscripcion s = obtenerSuscripcion (idSus);
        s.setEstado (estado);
        if (estado == EstadoSuscripcion.ACTIVA) {
            Date fechaC = new GregorianCalendar ().getTime ();

            s.setFechaContratada (fechaC);

            Calendar cal = Calendar.getInstance ();
            cal.setTime (fechaC);
            cal.add (Calendar.MONTH, s.getCantMeses ());

            s.setFechaVencimiento (cal.getTime ());
        }
        return Singleton.getInstance ().merge (s);
    }
    
    public static Suscripcion obtenerSuscripcion (long idSus) {
        EntityManager em = Singleton.getInstance().getEntity();
        em.getTransaction().begin();
        Suscripcion lista = null;

        try {
            lista = (Suscripcion) em.createNativeQuery("SELECT * FROM suscripcion WHERE id = :idSus", Suscripcion.class)
                    .setParameter("idSus", idSus)
                    .getSingleResult();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("No se encontro la suscripcion");
        }
        return lista;
    }
    
    public static void agregarSuscripcion (long idCli, long idHosp, int cantMeses) {
        Suscripcion s = new Suscripcion ();
        Cliente c = CCliente.getCliente (idCli);
        Hospital h = CHospital.obtenerHospital (idHosp);
        
        s.setCliente (c);
        s.setHospital (h);
        s.setEstado (EstadoSuscripcion.PENDIENTE);
        s.setCantMeses (cantMeses);
        
        Singleton.getInstance ().persist (s);
    }
    
    public static Suscripcion tieneSuscripcion (long idCli, long idHosp, EstadoSuscripcion tipo) {
        EntityManager em = Singleton.getInstance().getEntity();
        em.getTransaction().begin();
        Suscripcion lista = null;

        try {
            lista = (Suscripcion) em.createNativeQuery("SELECT * FROM suscripcion WHERE cliente_id = :idCli AND hospital_id = :idHosp AND estado = :estado", Suscripcion.class)
                    .setParameter("idCli", idCli)
                    .setParameter("idHosp", idHosp)
                    .setParameter("estado", tipo)
                    .getSingleResult();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("No se encontraro la suscripcion");
        }
        return lista;
    }
    
    public static Suscripcion obtenerEstadoDeSuscripcion (long idCli, long idHosp) {
        EntityManager em = Singleton.getInstance().getEntity();
        em.getTransaction().begin();
        List<Suscripcion> lista = null;

        try {
            lista = (List<Suscripcion>) em.createNativeQuery("SELECT * FROM suscripcion WHERE cliente_id = :idCli AND hospital_id = :idHosp", Suscripcion.class)
                    .setParameter("idCli", idCli)
                    .setParameter("idHosp", idHosp)
                    .getResultList ();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("No se encontraro la suscripcion");
        }
        
        if (lista == null || lista.size() == 0)
            return null;
        
        if (suscripcionesContiene (lista, EstadoSuscripcion.ACTIVA) != null)
            return suscripcionesContiene (lista, EstadoSuscripcion.ACTIVA);
        if (suscripcionesContiene (lista, EstadoSuscripcion.PENDIENTE) != null)
            return suscripcionesContiene (lista, EstadoSuscripcion.PENDIENTE);
        if (suscripcionesContiene (lista, EstadoSuscripcion.VENCIDA) != null)
            return suscripcionesContiene (lista, EstadoSuscripcion.VENCIDA);
        if (suscripcionesContiene (lista, EstadoSuscripcion.RECHAZADA) != null)
            return suscripcionesContiene (lista, EstadoSuscripcion.RECHAZADA);
        if (suscripcionesContiene (lista, EstadoSuscripcion.ELIMINADA) != null)
            return suscripcionesContiene (lista, EstadoSuscripcion.ELIMINADA);
        
        return null;
    }
    
    private static Suscripcion suscripcionesContiene (List<Suscripcion> sus, EstadoSuscripcion estado) {
        for (Suscripcion s : sus)
            if (s.getEstado () == estado)
                return s;
        return null;
    }
}
