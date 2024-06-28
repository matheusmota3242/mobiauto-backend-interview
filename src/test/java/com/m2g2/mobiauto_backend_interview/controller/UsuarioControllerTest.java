package com.m2g2.mobiauto_backend_interview.controller;

import com.m2g2.mobiauto_backend_interview.dto.request.AutenticacaoRequest;
import com.m2g2.mobiauto_backend_interview.dto.response.AutenticacaoResponse;
import com.m2g2.mobiauto_backend_interview.model.Papel;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    @Test
    void deveRegistrarUsuario() {
        Usuario usuario = new Usuario();
        doNothing().when(usuarioService).registrar(any());

        ResponseEntity<?> response = usuarioController.registrar(usuario);

        assertEquals(201, response.getStatusCode().value());
        verify(usuarioService, times(1)).registrar(usuario);
    }

    @Test
    void deveAutenticarUsuario() {
        AutenticacaoRequest request = new AutenticacaoRequest("email", "senha");
        AutenticacaoResponse expectedResponse = new AutenticacaoResponse("");
        when(usuarioService.autenticar(any())).thenReturn(expectedResponse);

        ResponseEntity<AutenticacaoResponse> response = usuarioController.autenticar(request);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResponse, response.getBody());
        verify(usuarioService, times(1)).autenticar(request);
    }

    @Test
     void deveAtualizarPapeis() {
        List<Papel> papeis = List.of(new Papel());
        String email = "test@test.com";
        doNothing().when(usuarioService).atualizarPapeis(papeis, email);

        ResponseEntity<?> response = usuarioController.atualizarPapeis(papeis, email);

        assertEquals(200, response.getStatusCode().value());
    }
}