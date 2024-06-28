package com.m2g2.mobiauto_backend_interview.service;

import com.m2g2.mobiauto_backend_interview.config.JwtService;
import com.m2g2.mobiauto_backend_interview.dto.request.AutenticacaoRequest;
import com.m2g2.mobiauto_backend_interview.dto.response.AutenticacaoResponse;
import com.m2g2.mobiauto_backend_interview.enums.DescricaoPapel;
import com.m2g2.mobiauto_backend_interview.model.Papel;
import com.m2g2.mobiauto_backend_interview.model.Revenda;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.repository.UsuarioRepository;
import com.m2g2.mobiauto_backend_interview.utils.AutorizacaoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AutorizacaoUtils autorizacaoUtils;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    private Revenda revenda;

    @BeforeEach
    void setUp() {
        revenda = new Revenda();
        revenda.setId(1L);
        usuario = new Usuario();
        usuario.setEmail("user@example.com");
        usuario.setSenha("password");
        usuario.setRevenda(revenda);
    }

    @WithMockUser(username = "admin", roles = { "ADMIN" })
    @Test
    void registrar_comUsuarioAdmin_successo() {
        Mockito.when(autorizacaoUtils.autorizarUsuario(any())).thenReturn(usuario);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        usuarioService.registrar(usuario);

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void registrar_GerenteUserWithRevenda_Success() {
        Mockito.when(autorizacaoUtils.autorizarUsuario(any())).thenReturn(usuario);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        usuarioService.registrar(usuario);

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

    @Test
    void autenticar_comSuccesso() {
        AutenticacaoRequest request = new AutenticacaoRequest("user@example.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(any())).thenReturn("jwtToken");

        AutenticacaoResponse response = usuarioService.autenticar(request);

        assertNotNull(response);
        assertEquals("jwtToken", response.token());
    }

    @Test
    void autenticar_UserNotFound_ThrowsException() {
        AutenticacaoRequest request = new AutenticacaoRequest("user@example.com", "password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> usuarioService.autenticar(request));
    }

    @Test
    void atualizarPapeis_comSuccesso() {
        Papel papel = new Papel();
        papel.setDescricao(DescricaoPapel.GERENTE.name());
        List<Papel> papeis = Collections.singletonList(papel);
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.of(usuario));
        when(autorizacaoUtils.autorizarUsuario(any())).thenReturn(usuario);

        usuarioService.atualizarPapeis(papeis, "");

        verify(usuarioRepository, times(1)).save(any(Usuario.class));
    }

}
