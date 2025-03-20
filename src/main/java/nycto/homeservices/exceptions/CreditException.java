package nycto.homeservices.exceptions;

public class CreditException extends RuntimeException {
    public CreditException(String message) {
        super(message);
    }
}