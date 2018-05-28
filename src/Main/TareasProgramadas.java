/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;

import Clases.HorarioAtencion;
import Clases.Hospital;
import Clases.TipoTurno;
import Clases.Turno;
import Controladores.CCorreo;
import Controladores.CHospital;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Luis
 */
public class TareasProgramadas extends TimerTask {

    Date hoy;

    @Override
    public void run() {

        hoy = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(hoy);
        c.add(Calendar.DATE, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        hoy = c.getTime();

        List<Hospital> hospitales = CHospital.obtenerHospitales();

        if (!hospitales.isEmpty()) {

            for (Hospital h : hospitales) {

                List<HorarioAtencion> horarios = h.getHorarioAtencions();

                if (!horarios.isEmpty()) {

                    for (HorarioAtencion ha : horarios) {
                        List<Turno> turnos = ha.getTurnos();

                        if (!turnos.isEmpty()) {

                            for (Turno t : turnos) {
                                System.out.println("no entra al if");
                                if (t.getFecha().compareTo(hoy) == 0) {

                                    DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                                    DateFormat fechaFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Format f = new SimpleDateFormat("MMMM");
                                    String mes = f.format(t.getFecha());
                                    String fecha = fechaFormat.format(t.getFecha());
                                    String[] array = fecha.split("-");
                                    String dia = array[2];
                                    String anio = array[0];
                                    String hora = dateFormat.format(t.getHora());
                                    System.out.println("ENVIA CORREO");
                                    CCorreo.enviarReserva(t.getCliente(), t.getHorarioAtencion().getEmpleado(), t.getHorarioAtencion().getHospital().getNombre(), t.getTipo() == TipoTurno.ATENCION ? "Atencíon" : "Vacunación", t.getEspecialidad(), dia, mes, anio, hora, "Recordatorio");
                                    System.out.println("TERMINO DE ENVIAR");
                                }

                            }

                        }

                    }

                }

            }

        }
    }

}
