package com.oc.microservice.scheduledTasks;

import com.oc.microservice.dao.LoanDao;
import com.oc.microservice.model.Loan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

@Component
public class ScheduledTasks {
    @Autowired
    LoanDao loanDao;

    @Scheduled(fixedRate = 24*60*60*1000)//24h
    public void reportNotReturnedBook(){
        List<Loan> loans = loanDao.findByNotReturnedandAndDateEnd(new Date(Calendar.getInstance().getTime().getTime()));
        String text;
        for(int i=0; i<loans.size();i++) {
            text="Le prêt du livre \"" +
                    loans.get(i).getBook().getTitle()+" - "+ loans.get(i).getBook().getAutor()+
                    "\" est arrivé à sa fin, Pensez à ramener l'ouvrage! \n" +
                    "Emprunté par " +
                    loans.get(i).getUser().getName()+" "+loans.get(i).getUser().getFirstname()+
                    ". \n" +
                    "Date de retour: " +
                    loans.get(i).getDateEnd();
            sendMessage("Mail de Relance : Prêt du Livre",text,"duboisgeoffrey03@gmail.com",loans.get(i).getUser().getMail());
        }
    }
    public static void sendMessage(String subject, String text, String from, String to){
        final String username = "duboisgeoffrey03@gmail.com";
        final String password = "fqfcduosqiiecgiz";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
