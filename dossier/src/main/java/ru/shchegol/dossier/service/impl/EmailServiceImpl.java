package ru.shchegol.dossier.service.impl;



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.shchegol.dossier.service.EmailService;
import ru.shchegol.dto.EmailMessageDto;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;


@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Override
    public void send(EmailMessageDto emailMessage, String text){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("shchegolevilya1@gmail.com");
            message.setTo(emailMessage.getAddress());
            message.setSubject(text);
            message.setText(emailMessage.getStatementId());
            emailSender.send(message);
            log.info("Email sent to {} with subject {}", emailMessage.getAddress(), text);
    }

    @Override
    public void sendWithAttachment(EmailMessageDto emailMessage, String text){
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("shchegolevilya1@gmail.com");
            helper.setTo(emailMessage.getAddress());
            helper.setSubject(text);
            helper.setText(emailMessage.getStatementId());
            helper.addAttachment("statement.pdf",
                    new File(getClass().getResource("/static/statement.pdf").getFile()));
            emailSender.send(message);
            log.info("Email sent to {} with subject {}", emailMessage.getAddress(), text);
        }catch (MessagingException e){
            log.error("Error sending email", e);
        }
    }
}
