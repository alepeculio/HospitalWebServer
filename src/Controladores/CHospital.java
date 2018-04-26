/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Clases.Hospital;
import java.util.List;

/**
 *
 * @author Jorge
 */
public class CHospital {
    
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
