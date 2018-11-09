package io.swagger.exp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.swagger.api.NotFoundException;

/**
 * This handler is used to map exceptions to corresponding response status and message.
 */
//@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    /**
     * Handle invalid method argument type exception.
     *
     * @param ex the exception
     * @return the error response entity
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String requiredType = ex.getRequiredType().getSimpleName().toLowerCase();
        if (ex.getCause() instanceof NumberFormatException) {
            requiredType += " number";
        }
        return buildErrorResponse(HttpStatus.BAD_REQUEST, String.format("%s must be a %s", ex.getParameter().getParameterName(), requiredType));
    }

    /**
     * Handle illegal argument exception.
     *
     * @param exp the exception
     * @return the error response entity
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleIllegalArgumentException(Exception exp) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, exp.getMessage());
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
        return buildErrorResponse(HttpStatus.NOT_FOUND, exp.getMessage());
    }

    /**
     * Handle duplicate key exception.
     *
     * @param exp the exp
     * @return the response entity
     */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException exp) {
        String responseMessage = "The document already exists";
        if (exp.getCause() instanceof com.mongodb.DuplicateKeyException) {
            com.mongodb.DuplicateKeyException mongoEx = (com.mongodb.DuplicateKeyException) exp.getCause();
            if (mongoEx != null) {
                String message = mongoEx.getMessage();
                String[] splittedByIndex = StringUtils.split(message, "index: ");
                if (splittedByIndex != null) {
                    String[] splittedByDupKey = StringUtils.split(splittedByIndex[1], " duplicate key");
                    if (splittedByDupKey != null) {
                        responseMessage += " with the same " + splittedByDupKey[0] + " key";
                    }
                }
            }
        }
        return buildErrorResponse(HttpStatus.BAD_REQUEST, responseMessage);
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
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
    }

    /**
     * Handle exception internally
     *
     * @param exp the exception
     * @param body the body for the response
     * @param headers the headers for the response
     * @param status the response status
     * @param request the request
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception exp, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = exp.getMessage();
        if (exp instanceof HttpMessageNotReadableException) {
            message = "Request body is missing or invalid";
        } else if (exp instanceof BindException) {
            message = convertErrorsToMessage(((BindException) exp).getAllErrors());
        } else if (exp instanceof MethodArgumentNotValidException) {
            message = convertErrorsToMessage(((MethodArgumentNotValidException) exp).getBindingResult().getAllErrors());
        } else if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            message = "Internal server error";
        }
        return buildErrorResponse(status, message);
    }

    /**
     * Convert object errors to error message string.
     *
     * @param objectErrors the list of object errors
     * @return the comma separated error message
     */
    private String convertErrorsToMessage(List<ObjectError> objectErrors) {
        List<String> messages = new ArrayList<>();
        for (ObjectError objectError : objectErrors) {
            messages.add(messageSource.getMessage(objectError, null));
        }
        return StringUtils.collectionToDelimitedString(messages, ", ");
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
