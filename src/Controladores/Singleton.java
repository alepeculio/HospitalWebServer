package Controladores;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Jorge
 */
public class Singleton {

    private static Singleton INSTANCE;
    private static EntityManagerFactory EMF;
    private static EntityManager EM;

    private Singleton() {
        EMF = Persistence.createEntityManagerFactory("HospitalWebServerPU");
        EM = EMF.createEntityManager();
    }

    public static Singleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singleton();
        }

        return INSTANCE;
    }

    public EntityManager getEntity() {
        return EM;
    }

    public void cerrarSesion() {
        EM.close();
    }

    public boolean persist(Object object) {
        EntityManager em = getEntity();
        em.getTransaction().begin();
        try {
            em.persist(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        }
        return true;
    }

    public boolean remove(Object object) {
        EntityManager em = getEntity();
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }

        em.getTransaction().begin();

        try {
            em.remove(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
        return true;
    }

    public void refresh(Object object) {
        EntityManager em = getEntity();
        em.getTransaction().begin();
        try {
            em.refresh(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    public boolean merge(Object object) {
        EntityManager em = getEntity();
        em.getTransaction().begin();
        try {
            em.merge(object);
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return false;
        }
        return true;
    }
}
