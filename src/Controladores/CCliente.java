/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Clases.Cliente;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Jorge
 */
public class CCliente {

    public static List<Cliente> obtenerClientes() {
        List<Cliente> lista = null;
        try {
            if (!Singleton.getInstance().getEntity().getTransaction().isActive()) {
                Singleton.getInstance().getEntity().getTransaction().begin();
            }
            lista = Singleton.getInstance().getEntity().createNativeQuery("SELECT * FROM cliente WHERE activo = 1", Cliente.class)
                    .getResultList();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Controladores.CCliente.obtenerClientes(): Error en la transaccion");
            if (Singleton.getInstance().getEntity().getTransaction().isActive()) {
                Singleton.getInstance().getEntity().getTransaction().rollback();
            }
        }
        if (lista != null) {
            return lista;
        }
        return new ArrayList<>();
    }

    public static List<Cliente> obtenerClientesNoEmpleados() {
        List<Cliente> lista = null;

        try {
            if (!Singleton.getInstance().getEntity().getTransaction().isActive()) {
                Singleton.getInstance().getEntity().getTransaction().begin();
            }
            lista = Singleton.getInstance().getEntity().createNativeQuery("SELECT * FROM cliente WHERE DTYPE = 'Cliente' AND activo = 1", Cliente.class)
                    .getResultList();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Controladores.CCliente.obtenerClientes(): Error en la transaccion");
            if (Singleton.getInstance().getEntity().getTransaction().isActive()) {
                Singleton.getInstance().getEntity().getTransaction().rollback();
            }

        }
        if (lista != null) {
            return lista;
        }
        return new ArrayList<>();
    }

    public List<Cliente> obtenerNoHijosCliente(String idCliente) {
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
        Cliente cliente = null;
        try {
            Singleton.getInstance().getEntity().getTransaction().begin();
            cliente = (Cliente) Singleton.getInstance().getEntity().createNativeQuery("SELECT * FROM cliente WHERE id=" + id, Cliente.class)
                    .getSingleResult();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {
            Singleton.getInstance().getEntity().getTransaction().rollback();
            System.err.println("No se puedo encontrar el cliente con id: " + id);
        }
        return cliente;
    }

    public static Cliente getClientebyUsuario(long idUsuario) {
        Cliente cliente = null;
        try {
            Singleton.getInstance().getEntity().getTransaction().begin();
            cliente = (Cliente) Singleton.getInstance().getEntity().createNativeQuery("SELECT * FROM cliente WHERE usuario_id=" + idUsuario, Cliente.class)
                    .getSingleResult();
            Singleton.getInstance().getEntity().getTransaction().commit();
        } catch (Exception e) {
            Singleton.getInstance().getEntity().getTransaction().rollback();
            System.err.println("No se puedo encontrar el cliente relacionado al usuario con id: " + idUsuario);
        }
        return cliente;
    }

    public static boolean vincularHijoCliente(String idHijo, String idPadre) {
        Cliente padre = getCliente(Long.valueOf(idPadre));
        Cliente hijo = getCliente(Long.valueOf(idHijo));
        if (hijo.getHijos() == null || hijo.getHijos() != null && !hijo.getHijos().contains(padre)) {
            padre.getHijos().add(hijo);
            return Singleton.getInstance().merge(padre);
        } else {
            return false;
        }

    }

    public static boolean altaCliente(Cliente cliente) {
        return Singleton.getInstance().persist(cliente);
    }

    public static boolean bajaCliente(String idCliente) {
        Cliente cliente = getCliente(Long.valueOf(idCliente));
        cliente.setActivo(false);
        return Singleton.getInstance().merge(cliente);
    }

    public static Cliente obtenerCliente(String nombre) {

        List<Cliente> clientes;
        clientes = obtenerClientes();

        for (Cliente c : clientes) {
            if (c.getNombre().equals(nombre)) {
                return c;
            }
        }

        return null;
    }

    public static void RegistrarHijoPlanVacunacion(Cliente padre, Cliente hijo) {

    }

    public static void ReservarTurnoVacunacion() {

    }

    public static void ModificarTurnoVacunacion(String[] args) {

    }

    public static void CancelarTurnoVacunacion(String[] args) {

    }

    public static List<Cliente> edad(Cliente c, int edad, String en) {
        List<Cliente> hijos = c.getHijos();
        List<Cliente> hijosXedad = null;
        for (Cliente hijo : hijos) {
            String Nac = String.valueOf(hijo.getDiaNacimiento()) + "/" + String.valueOf(hijo.getMesNacimiento()) + "/" + String.valueOf(hijo.getAnioNacimiento());
            Date F = new Date();
            F.setYear(hijo.getAnioNacimiento() - 1900);
            F.setMonth(hijo.getMesNacimiento() - 1);
            F.setDate(hijo.getDiaNacimiento());
            SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/yyyy");
            String fecha = formatoDeFecha.format(F);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaNac = LocalDate.parse(fecha, fmt);
            LocalDate ahora = LocalDate.now();
            Period periodo = Period.between(fechaNac, ahora);
            if ("m".equals("en")) {
                if (periodo.getMonths() == edad) {
                    hijosXedad.add(hijo);
                } else {
                    return null;
                }
            } else {
                if (periodo.getYears() == edad) {
                    hijosXedad.add(hijo);
                } else {
                    return null;
                }
            }
        }
        return hijosXedad;

    }

}
