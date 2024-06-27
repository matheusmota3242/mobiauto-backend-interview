package com.m2g2.mobiauto_backend_interview.utils;

import com.m2g2.mobiauto_backend_interview.enums.DescricaoPapel;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class AutenticacaoUtils {
    public static DescricaoPapel receberPapelPorPrioridade() {
        Collection<? extends GrantedAuthority> autoridades = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        List<DescricaoPapel> papeis = Arrays.stream(DescricaoPapel.values())
                .sorted(Comparator.comparingInt(DescricaoPapel::getPrioridade))
                .toList();
        return papeis.stream()
                .filter(papel -> autoridades.contains(new SimpleGrantedAuthority("ROLE_".concat(papel.name()))))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("Permissão de acesso negada."));

    }

    public static String receberEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
