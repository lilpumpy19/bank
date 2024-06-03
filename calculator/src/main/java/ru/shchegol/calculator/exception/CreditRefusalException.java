package ru.shchegol.calculator.exception;


public class CreditRefusalException extends RuntimeException {
    public CreditRefusalException(String message) {
        super(message);
    }
}

