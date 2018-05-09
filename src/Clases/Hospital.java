/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import Controladores.Singleton;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Jorge
 */
@Entity
public class Hospital implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String nombre;
    private boolean publico;
    private String departamento;
    private String calle;
    private int numero;
    private double latitud;
    private double longitud;
    @OneToMany(mappedBy = "hospital")
    private List<Suscripcion> suscripciones;
    @OneToMany(mappedBy = "hospital", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Administrador> administradores;
    @OneToMany(mappedBy = "hospital")
    private List<HorarioAtencion> horarioAtencions;
    @ManyToMany
    private List<Empleado> empleados;

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public void setEmpleados(List<Empleado> empleados) {
        this.empleados = empleados;
    }




    public List<HorarioAtencion> getHorarioAtencions() {
        return horarioAtencions;
    }

    public List<Administrador> getAdministradores() {
        return administradores;
    }

    public void setAdministradores(List<Administrador> administradores) {
        this.administradores = administradores;
    }

    public void agregarAdministrador(Usuario u) {
        if (administradores == null) {
            administradores = new ArrayList<>();
        }

        Administrador a = new Administrador();
        a.setAdminGeneral(false);
        a.setUsuario(u);
        a.setHospital(this);
        administradores.add(a);
    }

    public void removerAdministrador(String ciAdmin) {
        if (administradores == null) {
            return;
        }

        for (int i = 0; i < administradores.size(); i++) {
            if (administradores.get(i).getUsuario().getCi().equals(ciAdmin)) {
                administradores.remove(i);
                return;
            }
        }
    }

    public void setHorarioAtencions(List<HorarioAtencion> horarioAtencions) {
        this.horarioAtencions = horarioAtencions;
    }

    public List<Suscripcion> getSuscripciones() {
        return suscripciones;
    }

    public void setSuscripciones(List<Suscripcion> suscripciones) {
        this.suscripciones = suscripciones;
    }

    public boolean isPublico() {
        return publico;
    }

    public void setPublico(boolean publico) {
        this.publico = publico;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Hospital)) {
            return false;
        }
        Hospital other = (Hospital) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.Hospital[ id=" + id + " ]";
    }

}
