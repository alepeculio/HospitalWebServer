/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Clases.Administrador;
import Clases.Usuario;
import java.util.List;

/**
 *
 * @author Jorge
 */
public class CAdministradores {
    
    public static List<Administrador> obtenerAdministradores () {
        List<Administrador> lista = null;
        Singleton.getInstance().getEntity().getTransaction().begin();
        try {
            lista = Singleton.getInstance().getEntity().createNativeQuery ("SELECT * FROM administrador", Administrador.class).getResultList ();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {
            Singleton.getInstance().getEntity().getTransaction().rollback();
        }
        return lista;
    }
    
    public static void agregarAdminGeneral (Usuario u) {
        Administrador a = new Administrador ();
        a.setAdminGeneral (true);
        a.setUsuario (u);
        Singleton.getInstance ().persist (a);
    }
}
