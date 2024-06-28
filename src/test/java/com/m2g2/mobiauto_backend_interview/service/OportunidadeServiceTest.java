package com.m2g2.mobiauto_backend_interview.service;

import com.m2g2.mobiauto_backend_interview.dto.request.AtualizacaoStatusOportunidade;
import com.m2g2.mobiauto_backend_interview.enums.StatusOportunidade;
import com.m2g2.mobiauto_backend_interview.exception.RevendaInconsistenteException;
import com.m2g2.mobiauto_backend_interview.model.Oportunidade;
import com.m2g2.mobiauto_backend_interview.model.Revenda;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.repository.OportunidadeRepository;
import com.m2g2.mobiauto_backend_interview.utils.AutorizacaoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OportunidadeServiceTest {

    @Mock
    private OportunidadeRepository oportunidadeRepository;

    @Mock
    private UserDetailsServiceImpl usuarioDetailsService;

    @Mock
    private Authentication authentication;

    @Mock
    private AutorizacaoUtils autorizacaoUtils;

    @InjectMocks
    private OportunidadeService oportunidadeService;

    private Revenda revenda;
    private Usuario usuario;
    private Oportunidade oportunidade;

    @BeforeEach
    void setUp() {
        revenda = new Revenda();
        revenda.setId(1L);

        usuario = new Usuario();
        usuario.setEmail("user@example.com");
        usuario.setRevenda(revenda);

        oportunidade = new Oportunidade();
        oportunidade.setId(1L);
        oportunidade.setRevenda(revenda);
    }

    @Test
    void gerar() {
        Mockito.when(autorizacaoUtils.autorizarUsuario(any())).thenReturn(usuario);
        when(oportunidadeRepository.findByUsuarioPapelAndRevenda(anyString(), anyLong())).thenReturn(Collections.emptyList());

        oportunidadeService.gerar(oportunidade);

        assertEquals(StatusOportunidade.NOVA, oportunidade.getStatusOportunidade());
        assertEquals(LocalDate.now(), oportunidade.getDataInicio());
        assertEquals(usuario, oportunidade.getUsuario());
        verify(oportunidadeRepository, times(1)).save(oportunidade);
    }

    @Test
    void gerar_throwsAccessDeniedException() {
        when(autorizacaoUtils.autorizarUsuario(any())).thenThrow(AccessDeniedException.class);
        assertThrows(AccessDeniedException.class, () -> oportunidadeService.gerar(oportunidade));
    }

    @Test
    void transferir_comSucesso() {
        Mockito.when(autorizacaoUtils.autorizarUsuario(any())).thenReturn(usuario);
        Usuario usuarioResponsavel = new Usuario();
        usuarioResponsavel.setEmail("assistente1@example.com");
        when(oportunidadeRepository.findById(anyLong())).thenReturn(Optional.of(oportunidade));
        when(usuarioDetailsService.consultarUsuario(anyString())).thenReturn(usuario);

        oportunidadeService.transferir("assistente2@example.com", 1L);

        verify(oportunidadeRepository, times(1)).save(oportunidade);
    }

    @Test
    void transferir_revendaNaoEncontradaException() {
        when(oportunidadeRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RevendaInconsistenteException.class, () -> oportunidadeService.transferir("assistente@example.com", 1L));
    }

    @Test
    void atualizarStatus_comSucesso() {
        when(oportunidadeRepository.findById(anyLong())).thenReturn(Optional.of(oportunidade));

        AtualizacaoStatusOportunidade atualizacaoStatus = new AtualizacaoStatusOportunidade(1L, StatusOportunidade.CONCLUIDA, "Motivo");

        oportunidadeService.atualizarStatus(atualizacaoStatus);

        assertEquals(StatusOportunidade.CONCLUIDA, oportunidade.getStatusOportunidade());
        assertEquals(LocalDate.now(), oportunidade.getDataFim());
        verify(oportunidadeRepository, times(1)).save(oportunidade);
    }

    @Test
    void atualizarStatus_revendaNaoEncontrada_exception() {
        when(oportunidadeRepository.findById(anyLong())).thenReturn(Optional.empty());

        AtualizacaoStatusOportunidade atualizacaoStatus = new AtualizacaoStatusOportunidade(1L, StatusOportunidade.CONCLUIDA, "Motivo");

        assertThrows(RevendaInconsistenteException.class, () -> oportunidadeService.atualizarStatus(atualizacaoStatus));
    }

    @Test
    void listarPorRevenda_comSucesso() {
        when(oportunidadeRepository.findByRevenda(any())).thenReturn(Collections.singletonList(oportunidade));

        assertEquals(Collections.singletonList(oportunidade), oportunidadeService.listarPorRevenda(1L));
    }

}
