package com.m2g2.mobiauto_backend_interview.utils;

import com.m2g2.mobiauto_backend_interview.model.Revenda;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AutorizacaoUtilsTest {

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AutorizacaoUtils autorizacaoUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void autorizarUsuario_ProprietarioOuGerente_ComPermissao() {
        Revenda revenda = new Revenda();
        Usuario usuario = new Usuario();
        usuario.setRevenda(revenda);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        List autoridades = List.of(new SimpleGrantedAuthority("ROLE_PROPRIETARIO"));
        when(authentication.getAuthorities()).thenReturn(autoridades);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userDetailsService.consultarUsuario("test@example.com")).thenReturn(usuario);

        Usuario result = autorizacaoUtils.autorizarUsuario(revenda);

        assertEquals(usuario, result);
    }

    @Test
    void autorizarUsuario_ProprietarioOuGerente_SemPermissao() {
        Revenda revenda = new Revenda();
        Revenda outraRevenda = new Revenda();
        Usuario usuario = new Usuario();
        usuario.setRevenda(outraRevenda);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        List autoridades = List.of(new SimpleGrantedAuthority("ROLE_PROPRIETARIO"));
        when(authentication.getAuthorities()).thenReturn(autoridades);
        when(authentication.getName()).thenReturn("test@example.com");
        when(userDetailsService.consultarUsuario("test@example.com")).thenReturn(usuario);

        assertThrows(AccessDeniedException.class, () -> {
            autorizacaoUtils.autorizarUsuario(revenda);
        });
    }

    @Test
    void receberEmail() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("test@example.com");

        String email = autorizacaoUtils.receberEmail();

        assertEquals("test@example.com", email);
    }

    private Collection<? extends GrantedAuthority> createAuthorities(String role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
