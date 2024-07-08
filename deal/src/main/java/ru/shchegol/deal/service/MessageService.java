package ru.shchegol.deal.service;

public interface MessageService {
    void finishRegistration(String statementId);
    void createDocuments(String statementId);
    void sendDocuments(String statementId);
    void sendSes(String statementId);
    void creditIssued(String statementId);
    void statementDenied(String statementId);
}
