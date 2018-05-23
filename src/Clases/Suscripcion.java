package Clases;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author Ale
 */
@Entity
public class Suscripcion implements Serializable {

    @ManyToOne
    private Hospital hospital;

    @Expose
    @ManyToOne
    private Cliente cliente;

    private static final long serialVersionUID = 1L;
    @Expose
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Expose
    private int diaContratada;
    @Expose
    private int mesContratada;
    @Expose
    private int anioContratada;
    @Expose
    private int diaVencimiento;
    @Expose
    private int mesVencimiento;
    @Expose
    private int anioVencimiento;
    @Expose
    private EstadoSuscripcion estado;

    public Suscripcion() {
        this.estado = EstadoSuscripcion.PENDIENTE;
    }

    public EstadoSuscripcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoSuscripcion estado) {
        this.estado = estado;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public int getDiaContratada() {
        return diaContratada;
    }

    public void setDiaContratada(int diaContratada) {
        this.diaContratada = diaContratada;
    }

    public int getMesContratada() {
        return mesContratada;
    }

    public void setMesContratada(int mesContratada) {
        this.mesContratada = mesContratada;
    }

    public int getAnioContratada() {
        return anioContratada;
    }

    public void setAnioContratada(int anioContratada) {
        this.anioContratada = anioContratada;
    }

    public int getDiaVencimiento() {
        return diaVencimiento;
    }

    public void setDiaVencimiento(int diaVencimiento) {
        this.diaVencimiento = diaVencimiento;
    }

    public int getMesVencimiento() {
        return mesVencimiento;
    }

    public void setMesVencimiento(int mesVencimiento) {
        this.mesVencimiento = mesVencimiento;
    }

    public int getAnioVencimiento() {
        return anioVencimiento;
    }

    public void setAnioVencimiento(int anioVencimiento) {
        this.anioVencimiento = anioVencimiento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Suscripcion)) {
            return false;
        }
        Suscripcion other = (Suscripcion) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Logica.Suscripcion[ id=" + id + " ]";
    }

}
