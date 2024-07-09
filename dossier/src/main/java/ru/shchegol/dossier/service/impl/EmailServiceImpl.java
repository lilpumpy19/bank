package ru.shchegol.dossier.service.impl;



import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.shchegol.dossier.service.EmailService;
import ru.shchegol.dto.EmailMessageDto;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;

    @Override
    public void send(EmailMessageDto emailMessage, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("shchegolevilya1@gmail.com");
        message.setTo(emailMessage.getAddress());
        message.setSubject(emailMessage.getStatementId());
        message.setText(text);
        emailSender.send(message);
    }
}
