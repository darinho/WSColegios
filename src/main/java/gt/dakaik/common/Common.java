/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.dakaik.common;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author dario.calderon
 */
public class Common {

    static final String TOKEN = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static final Map<String, Long> activeSession = new HashMap<>();
    static final Map<String, Integer> activeTimeSession = new HashMap<>();
    static final Map<Long, String> userSession = new HashMap<>();
    static final Timer validSession = new Timer();
    static final Timer discountTime = new Timer();
    static final int secVerified = 60;
    static final int minsSession = 10;
    static final String sendMailFrom = "dc@e.com.gt";
    static final String userId = "";

    public static void verifiedSessions() {
        validSession.schedule(new TimerTask() {

            @Override
            public void run() {
                activeTimeSession.entrySet().stream().forEach((e) -> {
                    int mins = e.getValue() - 1;
                    if (mins == 0) {
                        String token = e.getKey();
                        Long idUser = activeSession.get(token);
                        activeSession.remove(token);
                        activeTimeSession.remove(token);
                        userSession.remove(idUser);
                    }
                    e.setValue(mins);
                });
            }
        }, secVerified * 1000);
    }

    public static String getToken() {
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(32);
        for (int i = 0; i < 32; i++) {
            sb.append(TOKEN.charAt(rnd.nextInt(TOKEN.length())));
        }

        return sb.toString();
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;
        while (date.before(endDate)) {
            date.add(Calendar.DAY_OF_MONTH, 1);
            daysBetween++;
        }
        return daysBetween;
    }

    public static String setTokenLocal(String token, Long idUser) {
        activeSession.put(token, idUser);
        activeTimeSession.put(token, minsSession);
        userSession.put(idUser, token);
        return token;
    }

    public static void cleanSessionPrev(Long idUser) {
        String token = userSession.get(idUser);
        activeSession.remove(token);
        activeTimeSession.remove(token);
    }

    public static boolean validSession(String token, Long idUser) {
        Long i = activeSession.getOrDefault(token, new Long(-1));

        if (Objects.equals(i, idUser)) {
            activeTimeSession.replace(token, minsSession);
            return true;
        } else {
            return false;
        }
    }
    public static final String getTemplateSendEmailRegister(String user, String profile, String school, String lic, String pass) {
        return ""
                + "<div>"
                + " <h1>Colegios</h1>"
                + " <h3>Bienvenido a Colegios GT</h3>"
                + " <p>Este correo fue enviado de forma automática por Colegios. Su cuenta ha sido creada exitosamente.</p>"
                + " <br><br>"
                + " Usuario: <b>"
                + user
                + "</b><br>"
                + " Perfil: <b>"
                + profile
                + "</b><br>"
                + " Colegio: <b>"
                + school
                + "</b><br>"
                + " Licencia: <b>"
                + lic
                + "</b><br>"
                + " Contraseña: <b>"
                + pass
                + "</b><br><br>"
                + " <p>Su suscripción esta sujeta a CopyRight®</p>"
                + "</div>";
    }

    public static MimeMessage createEmail(String to, String subject, String bodyText) throws MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);

        email.setFrom(new InternetAddress(sendMailFrom));
        email.addRecipient(javax.mail.Message.RecipientType.TO,
                new InternetAddress(to));
        email.setSubject(subject);
        email.setText(bodyText);
        return email;
    }

    public static Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        email.writeTo(bytes);
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes.toByteArray());
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    public static void sendMessage(Gmail service, String userId, MimeMessage email)
            throws MessagingException, IOException {
        
        Message message = createMessageWithEmail(email);
        message = service.users().messages().send(userId, message).execute();

        System.out.println("Message id: " + message.getId());
        System.out.println(message.toPrettyString());
    }
}
