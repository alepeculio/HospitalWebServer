/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Clases.Administrador;
import Clases.Cliente;
import Clases.Empleado;
import Clases.Usuario;
import java.util.ArrayList;
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
        List<Cliente> soloClientes = CCliente.obtenerClientesNoEmpleados();

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

        if (soloClientes != null) {
            for (Cliente c : soloClientes) {
                if (c.getUsuario() == u) {
                    return "Cliente";
                }
            }
        }

        return "Empleado";
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
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("No se encontró el usuario");
        }
        if (u != null) {
            Cliente cliente = CCliente.getClientebyUsuario(u.getId());
            if (cliente != null && !cliente.isActivo()) {
                u = null;
            }
        }
        return u;
    }

    public Empleado getEmpleadobyUsuario(long id) {
        EntityManager em = s.getEntity();

        Empleado empleado = null;
        em.getTransaction().begin();
        try {
            empleado = (Empleado) em.createNativeQuery("SELECT * FROM cliente WHERE usuario_id=" + id, Empleado.class)
                    .getSingleResult();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            e.printStackTrace();

            System.err.println("No se puedo encontrar el empleado relacionado al usuario con id: " + id);
        }
        return empleado;
    }

    public static Empleado getEmpleado(long id) {
        Empleado empleado = null;
        try {
            Singleton.getInstance().getEntity().getTransaction().begin();
            empleado = (Empleado) Singleton.getInstance().getEntity().createNativeQuery("SELECT * FROM cliente WHERE id=" + id, Empleado.class)
                    .getSingleResult();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {
            Singleton.getInstance().getEntity().getTransaction().rollback();
            System.err.println("No se puedo encontrar el empleado con id: " + id);
        }
        return empleado;
    }

    public static List<Empleado> obtenerEmpleados() {
        List<Empleado> lista = null;

        try {
            if (!Singleton.getInstance().getEntity().getTransaction().isActive()) {
                Singleton.getInstance().getEntity().getTransaction().begin();
            }
            lista = Singleton.getInstance().getEntity().createNativeQuery("SELECT * FROM cliente WHERE DTYPE = 'Empleado' AND activo = 1", Empleado.class)
                    .getResultList();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {
            if (Singleton.getInstance().getEntity().getTransaction().isActive()) {
                Singleton.getInstance().getEntity().getTransaction().rollback();
            }
        }

        if (lista != null) {
            return lista;
        }
        return new ArrayList<>();
    }

    public boolean bajaEmpleado(String idEmpleado) {
        Empleado empleado = getEmpleado(Long.valueOf(idEmpleado));
        empleado.setActivo(false);
        return s.merge(empleado);
    }

    public boolean correoExiste(String correo) {
        EntityManager em = s.getEntity();

        Usuario u = null;

        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            u = (Usuario) em.createQuery("SELECT u FROM Usuario u WHERE correo= :c", Usuario.class)
                    .setParameter("c", correo)
                    .getSingleResult();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error en la transaccion, usuario con correo: " + correo);
        }
        return u != null;

    }

    public boolean cedulaExiste(String cedula) {
        EntityManager em = s.getEntity();

        Usuario u = null;

        try {
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
            u = (Usuario) em.createQuery("SELECT u FROM Usuario u WHERE ci= :cedula", Usuario.class)
                    .setParameter("cedula", cedula)
                    .getSingleResult();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Error en la transaccion, usuario con cedula: " + cedula);
        }
        return u != null;

    }
}
