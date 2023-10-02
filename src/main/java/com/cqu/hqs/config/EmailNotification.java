package com.cqu.hqs.config;

import lombok.Data;
import org.apache.commons.mail.DefaultAuthenticator;
import org.springframework.stereotype.Component;
import org.apache.commons.mail.HtmlEmail;

@Component
@Data
public class EmailNotification {

    private String from;
    private String[] to;
    private String subject;
    private String body;

    static String smtpServer= "smtp.mailtrap.io";
    static String smtpPort="587";
    static String username = "50ca49e274c05e";
    static String password ="f512be36c3f125";

    public void sendEmail() {

        try {
            HtmlEmail mail = new HtmlEmail();
            System.out.println(smtpServer);
            mail.setHostName(smtpServer);

            mail.setSmtpPort(Integer.parseInt(smtpPort));
            mail.setAuthenticator(new DefaultAuthenticator(username, password));

            mail.setFrom(from, subject);
            for (String recipient : to) {
                mail.addTo(recipient);
            }

            mail.setSubject(subject);
            mail.setHtmlMsg(body);
            mail.send();
            System.out.println("EMAIL SENT SUCCESSFULLY.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
