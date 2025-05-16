package app.classeMorta.ClasseMorta.logic;

import org.springframework.http.ResponseEntity;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public record ConditionalResponseEntity<T>(boolean successMessage, T message) {

    public static <T> ConditionalResponseEntity<T> successMessage(T message) {
        return new ConditionalResponseEntity<>(true, message);
    }

    public static <T> ConditionalResponseEntity<T> failedMessage(T message) {
        return new ConditionalResponseEntity<>(false, message);
    }

    public static <T> ResponseEntity<ConditionalResponseEntity<T>> success(T message) {
        return ResponseEntity.ok(successMessage(message));
    }

    public static <T> ResponseEntity<ConditionalResponseEntity<T>> failed(T message) {
        return ResponseEntity.ok(failedMessage(message));
    }

    public static <T> ResponseEntity<ConditionalResponseEntity<T>> unauthorized(T message) {
        return ResponseEntity.status(UNAUTHORIZED).body(failedMessage(message));
    }

}
