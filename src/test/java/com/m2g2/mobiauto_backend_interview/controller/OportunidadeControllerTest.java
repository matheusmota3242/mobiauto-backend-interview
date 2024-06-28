package com.m2g2.mobiauto_backend_interview.controller;

import com.m2g2.mobiauto_backend_interview.dto.request.AtualizacaoStatusOportunidade;
import com.m2g2.mobiauto_backend_interview.enums.StatusOportunidade;
import com.m2g2.mobiauto_backend_interview.model.Oportunidade;
import com.m2g2.mobiauto_backend_interview.service.OportunidadeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OportunidadeControllerTest {

    @Mock
    private OportunidadeService service;

    @InjectMocks
    private OportunidadeController controller;

    @Test
    void deveRertornarStatusCreated_quandoOportunidadeValida() {
        Oportunidade oportunidade = new Oportunidade();
        doNothing().when(service).gerar(any());

        ResponseEntity<?> response = controller.gerar(oportunidade);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(service, times(1)).gerar(oportunidade);
    }


    @Test
    void deveRetornarOk_quandoOportunidadeTransferidaComEmailEIdValido() {
        Long id = 1L;
        String email = "test@example.com";
        doNothing().when(service).transferir(anyString(), anyLong());

        ResponseEntity<?> response = controller.transferir(id, email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(service, times(1)).transferir(email, id);
    }

    @Test
    void deveRetornarOk_quandoStatusOportunidadeAtualizado_comSucesso() {
        doNothing().when(service).atualizarStatus(any());
        ResponseEntity<?> response = controller.atualizarStatus(new AtualizacaoStatusOportunidade(1L, StatusOportunidade.EM_ATENDIMENTO, "Motivo"));
        Assertions.assertEquals(200, response.getStatusCode().value());
    }

    @Test
    void deveRetornarListaDeOportunidadesPorRevenda_comSucesso() {
        when(service.listarPorRevenda(1L)).thenReturn(List.of(new Oportunidade(), new Oportunidade()));

        ResponseEntity<List<Oportunidade>> response = controller.listarPorRevenda(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, Objects.requireNonNull(response.getBody()).size());

    }

}