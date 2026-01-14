package es.tubalcain.exception;

/**
 * Exception thrown when a user tries to access or modify an entity they don't own.
 */
public class OwnershipException extends RuntimeException {
    
    public OwnershipException(String message) {
        super(message);
    }

    public OwnershipException(String message, Throwable cause) {
        super(message, cause);
    }
}
