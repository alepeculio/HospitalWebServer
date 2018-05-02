/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Clases.Cliente;
import Clases.Empleado;
import Clases.HorarioAtencion;
import Clases.Suscripcion;
import Clases.Turno;
import Clases.Usuario;
import Clases.Vacunacion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author Ale
 */
public class CUsuario {

    Singleton s = Singleton.getInstance();

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

        if (empleado != null) {

            List<HorarioAtencion> ha = null;
            em.getTransaction().begin();
            try {
                ha = (List<HorarioAtencion>) em.createNativeQuery("SELECT * FROM horarioatencion WHERE empleado_id=" + empleado.getId(), HorarioAtencion.class)
                        .getResultList();
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
            }
            empleado.setHorariosAtencions(ha);

            List<Suscripcion> s = null;
            em.getTransaction().begin();
            try {
                s = (List<Suscripcion>) em.createNativeQuery("SELECT * FROM suscripcion WHERE cliente_id=" + empleado.getId(), Suscripcion.class)
                        .getResultList();
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
            }
            empleado.setSuscripciones(s);

        }

        return empleado;
    }

    public Cliente getCliente(long id) {
        EntityManager em = s.getEntity();

        Cliente cliente = null;
        em.getTransaction().begin();
        try {
            cliente = (Cliente) em.createNativeQuery("SELECT * FROM cliente WHERE usuario_id=" + id, Cliente.class)
                    .getSingleResult();
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            System.err.println("No se puedo encontrar el empleado relacionado al usuario con id: " + id);
        }

        if (cliente != null) {

            List<Cliente> hijos = null;
            em.getTransaction().begin();
            try {
                hijos = (List<Cliente>) em.createNativeQuery("SELECT * FROM cliente_cliente WHERE cliente_id=" + cliente.getId(), Cliente.class)
                        .getResultList();
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
            }
            cliente.setHijos(hijos);
//
            List<Suscripcion> s = null;
            em.getTransaction().begin();
            try {
                s = (List<Suscripcion>) em.createNativeQuery("SELECT * FROM suscripcion WHERE cliente_id=" + cliente.getId(), Suscripcion.class)
                        .getResultList();
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
            }
            cliente.setSuscripciones(s);

            List<Vacunacion> vacunas = null;
            em.getTransaction().begin();
            try {
                vacunas = (List<Vacunacion>) em.createNativeQuery("SELECT * FROM cliente_vacunacion WHERE cliente_id=" + cliente.getId(), Vacunacion.class)
                        .getResultList();
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
            }
            cliente.setVacunacion(vacunas);

            List<Turno> turnos = null;
            em.getTransaction().begin();
            try {
                turnos = (List<Turno>) em.createNativeQuery("SELECT * FROM turno WHERE cliente_id=" + cliente.getId(), Turno.class)
                        .getResultList();
                em.getTransaction().commit();
            } catch (Exception e) {
                em.getTransaction().rollback();
            }
            cliente.setTurnos(turnos);

        }

        return cliente;
    }
}
