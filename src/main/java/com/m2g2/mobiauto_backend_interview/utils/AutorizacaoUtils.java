package com.m2g2.mobiauto_backend_interview.utils;

import com.m2g2.mobiauto_backend_interview.enums.DescricaoPapel;
import com.m2g2.mobiauto_backend_interview.model.Revenda;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.service.UserDetailsServiceImpl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Component
public class AutorizacaoUtils {

    private final UserDetailsServiceImpl userDetailsService;

    public AutorizacaoUtils(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    public Usuario autorizarUsuario(Revenda revenda) {
        DescricaoPapel descricaoPapel = receberPapelPorPrioridade();
        String emailAutenticado = receberEmail();
        Usuario usuarioAutenticado = userDetailsService.consultarUsuario(emailAutenticado);
        if (descricaoPapel.equals(DescricaoPapel.PROPRIETARIO) || descricaoPapel.equals(DescricaoPapel.GERENTE)) {
            if (!revenda.equals(usuarioAutenticado.getRevenda())) {
                throw new AccessDeniedException("Permissão negada para esta loja.");
            }
        }
        return usuarioAutenticado;
    }
    private DescricaoPapel receberPapelPorPrioridade() {
        Collection<? extends GrantedAuthority> autoridades = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<DescricaoPapel> papeis = Arrays.stream(DescricaoPapel.values())
                .sorted(Comparator.comparingInt(DescricaoPapel::getPrioridade))
                .toList();
        return papeis.stream()
                .filter(papel -> autoridades.contains(new SimpleGrantedAuthority("ROLE_".concat(papel.name()))))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("Permissão de acesso negada."));

    }

    public String receberEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
