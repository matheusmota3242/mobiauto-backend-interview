package com.m2g2.mobiauto_backend_interview.exception.handler;

import com.m2g2.mobiauto_backend_interview.exception.RevendaInconsistenteException;
import com.m2g2.mobiauto_backend_interview.exception.model.ApiError;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestControllerAdviceTest {

    @InjectMocks
    private RestControllerAdvice restControllerAdvice;

    @Mock
    private WebRequest webRequest;


    @Test
    void handleMethodArgumentNotValid() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        FieldError fieldError = new FieldError("objectName", "field", "default message");
        when(ex.getBindingResult()).thenReturn(mock(BindingResult.class));
        when(ex.getBindingResult().getAllErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<Object> response = restControllerAdvice.handleMethodArgumentNotValid(ex, new HttpHeaders(), HttpStatus.BAD_REQUEST, webRequest);

        assert response != null;
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("default message ", ((ApiError) Objects.requireNonNull(response.getBody())).getDescricao());
    }

    @Test
    void handleDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("data integrity violation");

        ResponseEntity<Object> response = restControllerAdvice.handleDataIntegrityViolationException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Operação não permitida por conter um ou mais erros.", ((ApiError) response.getBody()).getDescricao());
    }

    @Test
    void handleRevendaException() {
        RevendaInconsistenteException ex = new RevendaInconsistenteException("revenda exception");

        ResponseEntity<Object> response = restControllerAdvice.handleRevendaException(ex, webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("revenda exception", ((ApiError) response.getBody()).getDescricao());
    }

    @Test
    void handleAccessDenied() {
        AccessDeniedException ex = new AccessDeniedException("access denied");

        ResponseEntity<Object> response = restControllerAdvice.handleAccessDenied(ex, webRequest);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("access denied", ((ApiError) response.getBody()).getDescricao());
    }

    @Test
    void handleUsernameNotFound() {
        UsernameNotFoundException ex = new UsernameNotFoundException("username not found");

        ResponseEntity<Object> response = restControllerAdvice.handleUsernameNotFound(ex, webRequest);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("username not found", ((ApiError) response.getBody()).getDescricao());
    }

    @Test
    void handleBadCredentialsException() {
        BadCredentialsException ex = new BadCredentialsException("bad credentials");

        ResponseEntity<Object> response = restControllerAdvice.handleBadCredentials(ex, webRequest);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("bad credentials", ((ApiError) response.getBody()).getDescricao());
    }
}
