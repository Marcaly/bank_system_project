package marcal.bank.services.impl;

import marcal.bank.entities.records.EmailDetails;
import marcal.bank.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String emailSender;

    @Override
    public void sendEmailAlert(EmailDetails emailDetails) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(emailSender);
            mailMessage.setTo(emailDetails.receiver());
            mailMessage.setText(emailDetails.message());
            mailMessage.setSubject(emailDetails.subject());

            javaMailSender.send(mailMessage);
            System.out.println("Mail sent successfully");

        } catch (MailException e) {
            throw new RuntimeException(e);
        }
    }
}
