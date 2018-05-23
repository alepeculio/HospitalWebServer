package Controladores;

import Clases.Cliente;
import Clases.Usuario;
import java.io.UnsupportedEncodingException;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author Jorge
 */
public class CCorreo {
    
    private static final String NOMBRE = "HospitalWeb";
    private static final String CORREO = "HospitalWebUy@gmail.com";
    private static final String PASS = "rooteo1234";
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";
    
    public static void enviarContrasenia (Usuario u) {
        enviar (u.getCorreo (), "Bienvenido a Hospital Web", "Hola Administrador,\n\nLe damos la bienvenida a Hospital Web y esperamos que disfrute de nuestro servicios.\n\nSu contraseña es: " + u.getContrasenia () + "\nLa misma se puede cambiar ingresando a la pagina.\n\nDisfrútalo.\n\nEl Equipo de HospitalWeb.");
    }
    
    public static void enviarContrasenia (Cliente c) {
        enviar (c.getUsuario ().getCorreo (), "Bienvenido a Hospital Web", "Hola " + c.getNombre () + " " + c.getApellido () + ",\n\nLe damos la bienvenida a Hospital Web y esperamos que disfrute de nuestro servicios.\n\nSu contraseña es: " + c.getUsuario ().getContrasenia () + "\nLa misma se puede cambiar ingresando a la pagina.\n\nDisfrútalo.\n\nEl Equipo de HospitalWeb.");
    }
    
    public static void enviar (String destino, String asunto, String contenido) {
        Properties p = System.getProperties ();
        p.setProperty ("mail.smtp.host", HOST);
        p.setProperty ("mail.smtp.port", PORT);
        p.setProperty ("mail.smtp.starttls.enable", "true");
        p.setProperty ("mail.smtp.auth", "true");
        Session s = Session.getDefaultInstance (p, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                try {
                    return new PasswordAuthentication (CORREO, PASS);
                } catch (Exception ex) {
                    ex.printStackTrace ();
                }
                return null;
            }
        });
        try {
            MimeMessage m = new MimeMessage(s);
            m.setFrom (new InternetAddress (CORREO, NOMBRE));
            m.addRecipient (Message.RecipientType.TO, new InternetAddress (destino));
            m.setSubject (asunto);
            m.setText(contenido);
            Transport.send(m);
        } catch (Exception e) {
            System.err.println("Ups, no se pudo mandar");
        }
    }
}