/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

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
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date horaInicio;
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date horaFin;
    private int clientesMax;
    private int clienteActual;

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date horaFin) {
        this.horaFin = horaFin;
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }

    public void setTurnos(List<Turno> turnos) {
        this.turnos = turnos;
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
