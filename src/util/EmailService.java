package util;

import java.util.Properties;

import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailService {

	private static final String FROM_EMAIL =
	        "mosratilina35@gmail.com";

	private static final String APP_PASSWORD =
	        "ryggsoofddmhdgqj";

    public static void envoyerCode(String toEmail, String login, String password, String code) {

        try {
            Properties props = new Properties();

            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.port", "587");

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );

            message.setSubject("Vérification de votre compte OmniSport");

            message.setText(
                    "Bienvenue sur OmniSport\n\n"
                    + "Votre login : " + login + "\n"
                    + "Votre mot de passe temporaire : " + password + "\n"
                    + "Votre code de vérification : " + code + "\n\n"
                    + "Veuillez saisir ce code dans l'application pour activer votre compte."
            );

            Transport.send(message);

            System.out.println("Email réellement envoyé à : " + toEmail);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}