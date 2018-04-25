/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

/**
 *
 * @author Brian
 */
@Entity
public class HorarioAtencion implements Serializable {

    @ManyToOne
    private Empleado empleado;

    @ManyToOne
    private Hospital hospital;

    @OneToMany(mappedBy = "horarioAtencion")
    private List<Turno> turnos;
    
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String dia;
    private int horaInicio;
    private int minInicio;
    private int horaFin;
    private int minFin;
    private int clientesMax;
    private int clienteActual;
    
    
    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public int getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(int horaInicio) {
        this.horaInicio = horaInicio;
    }

    public int getMinInicio() {
        return minInicio;
    }

    public void setMinInicio(int minInicio) {
        this.minInicio = minInicio;
    }

    public int getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(int horaFin) {
        this.horaFin = horaFin;
    }

    public int getMinFin() {
        return minFin;
    }

    public void setMinFin(int minFin) {
        this.minFin = minFin;
    }

    public int getClientesMax() {
        return clientesMax;
    }

    public void setClientesMax(int clientesMax) {
        this.clientesMax = clientesMax;
    }

    public int getClienteActual() {
        return clienteActual;
    }

    public void setClienteActual(int clienteActual) {
        this.clienteActual = clienteActual;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!(object instanceof HorarioAtencion)) {
            return false;
        }
        HorarioAtencion other = (HorarioAtencion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.HorarioAtencion[ id=" + id + " ]";
    }
    
}
