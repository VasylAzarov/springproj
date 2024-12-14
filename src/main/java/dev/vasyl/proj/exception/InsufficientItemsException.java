package dev.vasyl.proj.exception;

public class InsufficientItemsException extends RuntimeException {
    public InsufficientItemsException(String message) {
        super(message);
    }
}
