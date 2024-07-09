package ru.shchegol.dossier.service;

import ru.shchegol.dto.EmailMessageDto;

public interface EmailService {

    void send(EmailMessageDto message, String text);

}
