package es.tubalcain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Catches exceptions and returns nice JSON error messages to the client.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * When user tries to access something they don't own.
     */
    @ExceptionHandler(OwnershipException.class)
    public ResponseEntity<Map<String, String>> handleOwnershipException(OwnershipException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", ex.getMessage()));
    }

    /**
     * When user is not logged in or other authentication issues.
     */
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalStateException(IllegalStateException ex) {
        if (ex.getMessage() != null && ex.getMessage().contains("logged in")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", ex.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }
}
