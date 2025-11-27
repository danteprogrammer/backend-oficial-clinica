package com.saludvida.api.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    // Tu correo autorizado en Brevo
    private String remitente = "danter9462@gmail.com";
    
    // La URL de tu aplicación (para no escribirla muchas veces)
    private String frontendUrl = "https://frontend-oficial-clinica-3lrb.vercel.app";

    public void enviarCredenciales(String destinatario, String username, String password) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            // El 'true' indica que es un mensaje multipart (necesario para HTML)
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(remitente);
            helper.setTo(destinatario);
            helper.setSubject("Bienvenido a Clínica SaludVida - Credenciales de Acceso");

            // Construimos el mensaje en HTML
            String htmlContent = """
                <div style='font-family: Arial, sans-serif; padding: 20px; color: #333;'>
                    <h2 style='color: #005792;'>Bienvenido a Clínica SaludVida</h2>
                    <p>Hola,</p>
                    <p>Su cuenta ha sido creada exitosamente.</p>
                    <p>Sus credenciales de acceso son:</p>
                    <ul>
                        <li><b>Usuario:</b> %s</li>
                        <li><b>Contraseña Temporal:</b> %s</li>
                    </ul>
                    <p>Por seguridad, el sistema le pedirá cambiar esta contraseña al ingresar.</p>
                    <br>
                    <a href='%s/login' style='background-color: #005792; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Ingresar al Sistema</a>
                    <br><br>
                    <p><small>Si el botón no funciona, copie y pegue este enlace: %s/login</small></p>
                    <p>Saludos,<br>Equipo Clínica SaludVida</p>
                </div>
                """.formatted(username, password, frontendUrl, frontendUrl);

            helper.setText(htmlContent, true); // 'true' activa el modo HTML
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo de credenciales", e);
        }
    }

    public void enviarEnlaceRecuperacion(String destinatario, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            String link = frontendUrl + "/reset-password?token=" + token;

            helper.setFrom(remitente);
            helper.setTo(destinatario);
            helper.setSubject("Recuperación de Contraseña");

            String htmlContent = """
                <div style='font-family: Arial, sans-serif; padding: 20px; color: #333;'>
                    <h2 style='color: #005792;'>Recuperación de Contraseña</h2>
                    <p>Hola,</p>
                    <p>Hemos recibido una solicitud para restablecer su contraseña.</p>
                    <br>
                    <a href='%s' style='background-color: #fca311; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Restablecer Contraseña</a>
                    <br><br>
                    <p>O copie y pegue el siguiente enlace en su navegador:</p>
                    <p><a href='%s'>%s</a></p>
                    <p><i>Este enlace expirará en 1 hora.</i></p>
                    <p>Saludos,<br>Equipo Clínica SaludVida</p>
                </div>
                """.formatted(link, link, link);

            helper.setText(htmlContent, true);
            mailSender.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar correo de recuperación", e);
        }
    }
}