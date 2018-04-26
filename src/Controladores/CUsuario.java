/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Clases.Usuario;
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
    EntityTransaction et = s.getEntity().getTransaction();

    public Usuario login(String ci, String contrasenia) {
        EntityManager em = s.getEntity();
        Usuario u = null;
        em.getTransaction().begin();
        try {
            u = (Usuario) em.createQuery("SELECT u FROM Usuario u WHERE usuario= :u AND contrasenia= :c", Usuario.class)
                    .setParameter("u", ci)
                    .setParameter("c", contrasenia)
                    .getSingleResult();

            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("No se encontró el usuario");
        }
        return u;
    }
}
