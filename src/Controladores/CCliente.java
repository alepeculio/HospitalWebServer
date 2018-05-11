/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Clases.Cliente;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jorge
 */
public class CCliente {

    public static List<Cliente> obtenerClientes() {
        List<Cliente> lista = null;
        Singleton.getInstance().getEntity().getTransaction().begin();
        try {
            lista = Singleton.getInstance().getEntity().createNativeQuery("SELECT * FROM cliente", Cliente.class).getResultList();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {
            Singleton.getInstance().getEntity().getTransaction().rollback();
        }
        return lista;
    }

    public static List<Cliente> obtenerNoHijosCliente(String idCliente) {
        List<Cliente> clientes = obtenerClientes();
        List<Cliente> hijos = new ArrayList<>();
        List<Cliente> noHijos = new ArrayList<>();
        long id = Long.valueOf(idCliente);

        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                hijos = cliente.getHijos();
            }
        }

        for (Cliente cliente : clientes) {
            if (cliente.getId() != id) {
                if (hijos != null && !hijos.contains(cliente)) {
                    noHijos.add(cliente);
                } else if (hijos == null) {
                    noHijos.add(cliente);
                }
            }
        }

        return noHijos;
    }

    public static Cliente getCliente(long id) {
        Singleton.getInstance().getEntity().getTransaction().begin();
        Cliente cliente = null;
        try {
            cliente = (Cliente) Singleton.getInstance().getEntity().createNativeQuery("SELECT * FROM cliente WHERE id=" + id, Cliente.class)
                    .getSingleResult();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {
            Singleton.getInstance().getEntity().getTransaction().rollback();
            System.err.println("No se puedo encontrar el cliente relacionado al usuario con id: " + id);
        }
        return cliente;
    }

    public static boolean vincularHijoCliente(String idHijo, String idPadre) {
        Cliente padre = getCliente(Long.valueOf(idPadre));
        Cliente hijo = getCliente(Long.valueOf(idHijo));
        if (!hijo.getHijos().contains(padre)) {
            padre.getHijos().add(hijo);
            return Singleton.getInstance().merge(padre);
        } else {
            return false;
        }

    }
}
