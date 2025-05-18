package app.classeMorta.ClasseMorta.logic.dto;

import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.*;

/**
 * Generic response wrapper that includes a success flag and message
 *
 * @param <T> Type of the message payload
 */
public record ConditionalResponseEntity<T>(boolean successMessage, T message) {
    /**
     * Creates successful response
     *
     * @param message Success message
     * @return ConditionalResponseEntity with success=true
     */
    public static <T> ConditionalResponseEntity<T> successMessage(T message) {
        return new ConditionalResponseEntity<>(true, message);
    }

    /**
     * Creates failed response
     *
     * @param message Error message
     * @return ConditionalResponseEntity with success=false
     */
    public static <T> ConditionalResponseEntity<T> failedMessage(T message) {
        return new ConditionalResponseEntity<>(false, message);
    }



    /**
     * Creates HTTP 200 OK response with a success message
     *
     * @param message Success message
     * @return ResponseEntity with 200 status and success=true
     */
    public static <T> ResponseEntity<ConditionalResponseEntity<T>> success(T message) {
        return ResponseEntity.ok(successMessage(message));
    }

    /**
     * Creates HTTP 200 OK response with failed message
     *
     * @param message Error message
     * @return ResponseEntity with 200 status and success=false
     */
    public static <T> ResponseEntity<ConditionalResponseEntity<T>> failed(T message) {
        return ResponseEntity.status(BAD_REQUEST).body(failedMessage(message));
    }

    /**
     * Creates HTTP 401 Unauthorized response
     *
     * @param message Error message
     * @return ResponseEntity with 401 status and success=false
     */
    public static <T> ResponseEntity<ConditionalResponseEntity<T>> unauthorized(T message) {
        return ResponseEntity.status(UNAUTHORIZED).body(failedMessage(message));
    }

    /**
     * Creates HTTP 404 Not Found response
     *
     * @param message Error message
     * @return ResponseEntity with 404 status and success=false
     */
    public static <T> ResponseEntity<ConditionalResponseEntity<T>> notFound(T message) {
        return ResponseEntity.status(NOT_FOUND).body(failedMessage(message));
    }

    /**
     * Creates HTTP 400 Bad Request response
     *
     * @param message Error message
     * @return ResponseEntity with 400 status and success=false
     */
    public static <T> ResponseEntity<ConditionalResponseEntity<T>> badRequest(T message) {
        return ResponseEntity.badRequest().body(failedMessage(message));
    }


}