package com.m2g2.mobiauto_backend_interview.exception.handler;

import com.m2g2.mobiauto_backend_interview.exception.RevendaInconsistenteException;
import com.m2g2.mobiauto_backend_interview.exception.model.ApiError;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;

@ControllerAdvice
public class RestControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        StringBuilder builder = new StringBuilder();

        for (ObjectError error : allErrors) {
            if (error instanceof FieldError fieldError) {
                builder.append(fieldError.getDefaultMessage()).append(" ");
            }
        }
        return createResponseEntity(new ApiError(builder.toString()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, EntityNotFoundException.class})
    ResponseEntity<Object> handleDataIntegrityViolationException(RuntimeException ex, WebRequest request) {
        return createResponseEntity(new ApiError("Operação não permitida por conter um ou mais erros."), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(RevendaInconsistenteException.class)
    ResponseEntity<Object> handleRevendaException(RevendaInconsistenteException ex, WebRequest request) {
        return createResponseEntity(new ApiError(ex.getMessage()), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<Object> handleAccessDenied(AccessDeniedException ex, WebRequest request) {
        return createResponseEntity(new ApiError(ex.getMessage()), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    ResponseEntity<Object> handleUsernameNotFound(UsernameNotFoundException ex, WebRequest request) {
        return createResponseEntity(new ApiError(ex.getMessage()), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<Object> handleBadCredentials(BadCredentialsException ex, WebRequest request) {
        return createResponseEntity(new ApiError(ex.getMessage()), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }
}
