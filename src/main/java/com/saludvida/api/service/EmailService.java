package com.saludvida.api.service;

import org.springframework.beans.factory.annotation.Autowired;
/*import org.springframework.beans.factory.annotation.Value;*/
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    /*@Value("${spring.mail.username}")
    private String remitente;*/
    private String remitente = "danter9462@gmail.com";

    public void enviarCredenciales(String destinatario, String username, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(remitente);
        message.setTo(destinatario);
        message.setSubject("Bienvenido a Clínica SaludVida - Credenciales de Acceso");
        message.setText("Hola,\n\n" +
                "Su cuenta ha sido creada exitosamente.\n" +
                "Usuario: " + username + "\n" +
                "Contraseña Temporal: " + password + "\n\n" +
                "Por seguridad, el sistema le pedirá cambiar esta contraseña al ingresar.\n\n" +
                "Saludos,\nEquipo Clínica SaludVida");
        
        mailSender.send(message);
    }

    public void enviarEnlaceRecuperacion(String destinatario, String token) {
        String url = "https://frontend-oficial-clinica-3lrb.vercel.app/reset-password?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(remitente);
        message.setTo(destinatario);
        message.setSubject("Recuperación de Contraseña");
        message.setText("Hola,\n\n" +
                "Solicitó restablecer su contraseña.\n" +
                "Haga clic aquí: " + url + "\n\n" +
                "El enlace expira en 1 hora.\n\n" +
                "Saludos,\nEquipo Clínica SaludVida");

        mailSender.send(message);
    }
}