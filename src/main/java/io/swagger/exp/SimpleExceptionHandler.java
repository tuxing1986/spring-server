package io.swagger.exp;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.swagger.api.NotFoundException;
// TODO: Auto-generated Javadoc

/**
 * The Class SimpleExceptionHandler.
 */
@RestControllerAdvice
public class SimpleExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Handle IllegalArgumentException.
     *
     * @param exp the exception
     * @return the error response entity
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseBody
    public ResponseEntity<Object> handleIllegalArgumentException(Exception exp) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exp.getMessage());
    }
    
    /**
     * Handle general exceptions.
     *
     * @param throwable the throwable
     * @return the error response entity
     */
    @ExceptionHandler(Throwable.class)
    @ResponseBody
    public ResponseEntity<Object> handleGeneralException(Throwable throwable) {
        throwable.printStackTrace();
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }
    
    /**
     * Handle general exceptions.
     *
     * @param throwable the throwable
     * @return the error response entity
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleException(Exception throwable) {
        throwable.printStackTrace();
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }
    
    /**
     * Handle general exceptions.
     *
     * @param throwable the throwable
     * @return the error response entity
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<Object> handleException(RuntimeException throwable) {
        throwable.printStackTrace();
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }
    
    /**
     * Handle not found exception.
     *
     * @param exp the exception
     * @return the error response entity
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    public ResponseEntity<Object> handleApplicationException(NotFoundException exp) {
        System.out.println(".................................");
        exp.printStackTrace();
        return buildErrorResponse(HttpStatus.NOT_FOUND, exp.getMessage());
    }
    
    /**
     * Build error response.
     *
     * @param status the http status
     * @param message the message
     * @return the error response entity with code and message
     */
    private static ResponseEntity<Object> buildErrorResponse(HttpStatus status, String message) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", message);
        return new ResponseEntity<>((Object) responseBody, status);
    }
}
