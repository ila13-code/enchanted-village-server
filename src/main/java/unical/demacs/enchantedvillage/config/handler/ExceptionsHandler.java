package unical.demacs.enchantedvillage.config.handler;


import net.minidev.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import unical.demacs.enchantedvillage.config.handler.exception.*;

import java.util.Map;
import java.util.ResourceBundle;

@ControllerAdvice
public class ExceptionsHandler {

    //private final ResourceBundle resourceBundle = ResourceBundle.getBundle("LAN_it");

    @ExceptionHandler(TooManyRequestsException.class)
    private ResponseEntity<?> handleTooManyRequests() {
        return new ResponseEntity<>(new JSONObject(
                Map.of("message", "TooManyRequestEx")).toString(), HttpStatus.TOO_MANY_REQUESTS);
    }


    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<?> handleRuntimeException() {
        return new ResponseEntity<>(new JSONObject(
                Map.of("message", "Internal server error")).toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NullPointerException.class)
    private ResponseEntity<?> handleNullPointerException(){
        return new ResponseEntity<>(new JSONObject(
                Map.of("message", "Null pointer exception")).toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    private ResponseEntity<?> handleHttpMessageNotReadableException(){
        return new ResponseEntity<>(new JSONObject(
                Map.of("message", "Value out of range")).toString(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    private ResponseEntity<?> handleAccessDeniedException(){
        return new ResponseEntity<>(new JSONObject(
                Map.of("message", "Access denied")).toString(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UserException.class)
    private ResponseEntity<?> handleUserException(UserException ex) {
        return new ResponseEntity<>(new JSONObject(
                Map.of("message", ex.getMessage())).toString(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoGameInformationFound.class)
    private ResponseEntity<?> handleNoGameInformationFound() {
        return new ResponseEntity<>(new JSONObject(
                Map.of("message", "No game information found")).toString(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidBuildingDataException.class)
    private ResponseEntity<?> handleInvalidBuildingDataException() {
        return new ResponseEntity<>(new JSONObject(
                Map.of("message", "Invalid building data")).toString(), HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EnemyNotAvaibleException.class)
    private ResponseEntity<?> handleEnemyNotAvaibleException() {
        return new ResponseEntity<>(new JSONObject(
                Map.of("message", "Enemy not Avaible")).toString(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}