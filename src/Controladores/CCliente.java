/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Clases.Cliente;
import Clases.Cliente;
import Clases.Empleado;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    public static Cliente obtenerCliente(String nombre) {

        List<Cliente> clientes = obtenerClientes();

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
