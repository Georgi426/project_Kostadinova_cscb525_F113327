package org.cscb525.exception;

public class TransportValidationException extends RuntimeException {

    public TransportValidationException(String message) {
        super(message);
    } 
    // Ако въведената цена е по-ниска, системата хвърля грешка
    public TransportValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
