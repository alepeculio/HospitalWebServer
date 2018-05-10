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
import Clases.Suscripcion;
import Clases.Usuario;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Ale
 */
public class CUsuario {

    Singleton s = Singleton.getInstance();

    public static String obtenerTipo(Usuario u) {
        List<Administrador> admins = CAdministradores.obtenerAdministradores();

        if (admins != null) {
            for (Administrador a : admins) {
                if (a.getUsuario().getCi().equals(u.getCi())) {
                    if (a.isAdminGeneral()) {
                        return "General";
                    } else {
                        return "Hospital";
                    }
                }
            }
        }

        return "Cliente";
    }

    public Usuario login(String ci, String contrasenia) {
        EntityManager em = s.getEntity();
        em.getTransaction().begin();
        Usuario u = null;

        try {
            u = (Usuario) em.createQuery("SELECT u FROM Usuario u WHERE ci= :cedula AND contrasenia= :pass", Usuario.class)
                    .setParameter("cedula", ci)
                    .setParameter("pass", contrasenia)
                    .getSingleResult();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.out.println("No se encontró el usuario");

        }
        return u;
    }

    public Empleado getEmpleado(long id) {
        EntityManager em = s.getEntity();

        Empleado empleado = null;
        em.getTransaction().begin();
        try {
            empleado = (Empleado) em.createNativeQuery("SELECT * FROM cliente WHERE usuario_id=" + id, Empleado.class)
                    .getSingleResult();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("No se puedo encontrar el empleado relacionado al usuario con id: " + id);
        }
        return empleado;
    }

    public boolean altaCliente(Cliente cliente) {
        return s.persist(cliente);
    }
}
