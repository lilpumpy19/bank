package ru.shchegol.dossier.service;

import ru.shchegol.dto.EmailMessageDto;

import javax.mail.MessagingException;

public interface EmailService {

    void send(EmailMessageDto message, String text);

    public void sendWithAttachment(EmailMessageDto emailMessage, String text);

}
