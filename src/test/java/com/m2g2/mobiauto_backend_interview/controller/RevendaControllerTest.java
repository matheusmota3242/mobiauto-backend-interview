package com.m2g2.mobiauto_backend_interview.controller;

import com.m2g2.mobiauto_backend_interview.controller.RevendaController;
import com.m2g2.mobiauto_backend_interview.model.Revenda;
import com.m2g2.mobiauto_backend_interview.service.RevendaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RevendaControllerTest {

    @InjectMocks
    private RevendaController revendaController;

    @Mock
    private RevendaService revendaService;

    @Test
    public void deveRertornarStatusCreatedQuandoRevendaValida() {
        Revenda revenda = new Revenda();
        doNothing().when(revendaService).salvar(any());
        ResponseEntity<?> response = revendaController.salvar(revenda);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(revendaService, times(1)).salvar(revenda);
    }

    @Test
    public void deveRetornarRevendaQuandoCnpjValido() {
        String cnpj = "validCnpj";
        Revenda revenda = new Revenda();
        when(revendaService.consultar(anyString())).thenReturn(revenda);

        ResponseEntity<Revenda> response = revendaController.consultar(cnpj);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(revenda, response.getBody());
        verify(revendaService, times(1)).consultar(cnpj);
    }

}