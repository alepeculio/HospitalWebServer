/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

/**
 *
 * @author Ale
 */
@Entity
public class Empleado extends Cliente implements Serializable {

    private String[] especialidades;
    private String[] titulos;
    private String tipo;
    @OneToMany(mappedBy = "empleado")
    private List<HorarioAtencion> horariosAtencions;

    public String[] getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(String[] especialidades) {
        this.especialidades = especialidades;
    }

    public String[] getTitulos() {
        return titulos;
    }

    public void setTitulos(String[] titulos) {
        this.titulos = titulos;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<HorarioAtencion> getHorariosAtencions() {
        return horariosAtencions;
    }

    public void setHorariosAtencions(List<HorarioAtencion> horariosAtencions) {
        this.horariosAtencions = horariosAtencions;
    }
    
    
}
