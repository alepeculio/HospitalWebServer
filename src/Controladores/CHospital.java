/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Clases.Administrador;
import Clases.Cliente;
import Clases.Empleado;
import Clases.HorarioAtencion;
import Clases.Hospital;
import Clases.Usuario;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jorge
 */
public class CHospital {
    
    public static boolean agregaHorarioAtencion (Usuario u, int idEmpleado, HorarioAtencion ha) {
        /*List<Administrador> administradores = CAdministradores.obtenerAdministradores ();
        Hospital h = null;
        
        if (administradores == null)
            return false;
        
        System.out.println("CHOSPITAL: 1");
        
        for (Administrador a : administradores)
            if (a.getUsuario ().getId () == idEmpleado && !a.isAdminGeneral ()) {
                Singleton.getInstance ().refresh (a);
                h = a.getHospital ();
                break;
            }*/
        
        System.out.println("CHOSPITAL: 2");
        Hospital h = CAdministradores.obtenerHospitalAdministrador (u.getCi ());
        if (h == null)
            return false;
        System.out.println("CHOSPITAL: 3 " + h.getId ());
        
        List<Cliente> clientes = CCliente.obtenerClientes ();
        Empleado e = null;
        System.out.println("CHOSPITAL: 4");
        
        for (Cliente c : clientes)
            if (c instanceof Empleado)
                if (c.getUsuario ().getId () == idEmpleado) {
                    e = (Empleado) c;
                    break;
                }
        System.out.println("CHOSPITAL: 5");
        
        if (e == null)
            return false;
        System.out.println("CHOSPITAL: 6");
        
        if (!Singleton.getInstance ().persist (ha))
            return false;
        System.out.println("CHOSPITAL: 7");
        
        h.agregarHA (ha);
        e.agregarHA (ha);
        ha.setEmpleado (e);
        ha.setHospital (h);
        
        if (!Singleton.getInstance ().merge (h))
            return false;
        System.out.println("CHOSPITAL: 8");
        
        if (!Singleton.getInstance ().merge (e))
            return false;
        System.out.println("CHOSPITAL: 9");
        
        return true;
    }
    
    public static void borrarAdministrador (String nomHospital, String ciAdmin) {
        Hospital h = obtenerHospital (nomHospital);
        h.removerAdministrador (ciAdmin);
        Singleton.getInstance ().merge (h);
    }
    
    public static void modificarAdministrador (String nomHospital, Usuario u) {
        List<Administrador> administradores = obtenerHospital (nomHospital).getAdministradores ();
        
        for (Administrador a : administradores)
            if (a.getUsuario ().getCi ().equals (u.getCi ())) {
                a.setUsuario (u);
                Singleton.getInstance ().merge (a);
            }
    }
    
    public static List<Usuario> obtenerAdministradoresHospital (String nomHospital) {
        List<Administrador> administradores = obtenerHospital (nomHospital).getAdministradores ();
        
        if (administradores == null)
            return null;
        
        List<Usuario> usuarios = new ArrayList<> ();
        
        for (Administrador a : administradores)
            usuarios.add (a.getUsuario ());
        
        return usuarios.size () == 0 ? null : usuarios;
    }
    
    public static String agregarAdministrador (String nomHospital, Usuario u) {
        Hospital h = obtenerHospital (nomHospital);
        List<Administrador> admins = h.getAdministradores ();
        
        if (admins != null)
            for (Administrador a : admins)
                if (a.getUsuario ().getCi ().equals (u.getCi ()))
                    return "C.I. ya existe";
                else if (a.getUsuario ().getCorreo ().equals (u.getCorreo ()))
                    return "Correo ya existe";
        
        h.agregarAdministrador (u);
        Singleton.getInstance ().merge (h);
        return "";
    }
    
    public static void modificarHospital (String nombre, Hospital h) {
        Hospital viejo = obtenerHospital (nombre);
        viejo.setNombre (h.getNombre ());
        viejo.setDirectora (h.getDirectora ());
        viejo.setPublico (h.isPublico ());
        viejo.setCorreo (h.getCorreo ());
        viejo.setTelefono (h.getTelefono ());
        viejo.setDepartamento (h.getDepartamento ());
        viejo.setCalle (h.getCalle ());
        viejo.setNumero (h.getNumero ());
        viejo.setLatitud (h.getLatitud ());
        viejo.setLongitud (h.getLongitud ());
        Singleton.getInstance ().merge (viejo);
    }
    
    public static void borrarHospital (String nombre) {
        Hospital h = obtenerHospital (nombre);
        
        if (h != null)
            Singleton.getInstance ().remove (h);
    }
    
    public static Hospital obtenerHospital (String nombre) {
        List<Hospital> hospitales = obtenerHospitales ();
        
        for (Hospital h : hospitales)
            if (h.getNombre ().equals (nombre))
                return h;
        
        return null;
    }
    
    public static List<Hospital> obtenerHospitales () {
        List<Hospital> lista = null;
        Singleton.getInstance().getEntity().getTransaction().begin();
        try {
            lista = Singleton.getInstance().getEntity().createNativeQuery ("SELECT * FROM hospital", Hospital.class).getResultList ();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {
            Singleton.getInstance().getEntity().getTransaction().rollback();
        }
        return lista;
    }
}
