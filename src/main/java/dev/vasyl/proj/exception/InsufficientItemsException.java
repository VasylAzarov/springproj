package dev.vasyl.proj.exception;

public class InsufficientItemsException extends RuntimeException {
    public InsufficientItemsException(String message) {
        super(message);
    }

    public InsufficientItemsException(String message, Exception e) {
        super(message, e);
    }
}
