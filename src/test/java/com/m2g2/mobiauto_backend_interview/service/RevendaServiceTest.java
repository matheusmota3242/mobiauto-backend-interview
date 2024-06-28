package com.m2g2.mobiauto_backend_interview.service;

import com.m2g2.mobiauto_backend_interview.exception.RevendaInconsistenteException;
import com.m2g2.mobiauto_backend_interview.model.Revenda;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.repository.RevendaRepository;
import com.m2g2.mobiauto_backend_interview.utils.AutorizacaoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RevendaServiceTest {

    @Mock
    private RevendaRepository revendaRepository;

    @Mock
    private UserDetailsServiceImplTest usuarioDetailsService;


    @InjectMocks
    private RevendaService revendaService;

    @Mock
    private AutorizacaoUtils autorizacaoUtils;

    private Revenda revenda;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        revenda = new Revenda();
        revenda.setCnpj("12345678000100");

        usuario = new Usuario();
        usuario.setEmail("user@example.com");
        usuario.setRevenda(revenda);
    }

    @Test
    void salvar_sucesso() {
        revendaService.salvar(revenda);
        verify(revendaRepository, times(1)).save(revenda);
    }

    @Test
    void consultar_comUsuarioAdmin_sucesso() {
        Mockito.when(autorizacaoUtils.autorizarUsuario(any())).thenReturn(usuario);
        when(revendaRepository.findByCnpj(anyString())).thenReturn(Optional.of(revenda));

        Revenda result = revendaService.consultar("12345678000100");

        assertEquals(revenda, result);
    }

    @Test
    void consultar_comUsuarioNaoAdmin_successo() {
        Mockito.when(autorizacaoUtils.autorizarUsuario(any())).thenReturn(usuario);
        when(revendaRepository.findByCnpj(anyString())).thenReturn(Optional.of(revenda));

        Revenda result = revendaService.consultar("12345678000100");

        assertEquals(revenda, result);
    }


    @Test
    void consultar_revendaNotFound_throwsRevendaException() {
        when(revendaRepository.findByCnpj(anyString())).thenReturn(Optional.empty());

        assertThrows(RevendaInconsistenteException.class, () -> revendaService.consultar("12345678000100"));
    }
}
