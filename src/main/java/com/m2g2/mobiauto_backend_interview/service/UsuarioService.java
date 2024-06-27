package com.m2g2.mobiauto_backend_interview.service;

import com.m2g2.mobiauto_backend_interview.config.JwtService;
import com.m2g2.mobiauto_backend_interview.dto.request.AutenticacaoRequest;
import com.m2g2.mobiauto_backend_interview.dto.response.AutenticacaoResponse;
import com.m2g2.mobiauto_backend_interview.enums.DescricaoPapel;
import com.m2g2.mobiauto_backend_interview.exception.RevendaException;
import com.m2g2.mobiauto_backend_interview.model.Papel;
import com.m2g2.mobiauto_backend_interview.model.Revenda;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.repository.RevendaRepository;
import com.m2g2.mobiauto_backend_interview.repository.UsuarioRepository;
import com.m2g2.mobiauto_backend_interview.utils.AutenticacaoUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    private final RevendaRepository revendaRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public UsuarioService(UsuarioRepository repository, RevendaRepository revendaRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.revendaRepository = revendaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public void registrar(Usuario novoUsuario) {
        DescricaoPapel papelUsarioAutenticado = AutenticacaoUtils.receberPapelPorPrioridade();
        String nomeUsuarioAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioAutenticado = repository.findByEmail(nomeUsuarioAutenticado)
                .orElseThrow(() -> new AccessDeniedException("Permissão de acesso negada."));
        if (papelUsarioAutenticado.equals(DescricaoPapel.ADMIN)) {
            novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));
            repository.save(novoUsuario);
        } else if (papelUsarioAutenticado.equals(DescricaoPapel.GERENTE) || papelUsarioAutenticado.equals(DescricaoPapel.PROPRIETARIO)) {
            Optional<Revenda> revendaOptional = Optional.ofNullable(novoUsuario.getRevenda());
            revendaOptional.ifPresentOrElse(
                    revendaNovoUsuario -> {
                        if (!revendaRepository.existsById(revendaNovoUsuario.getId())) {
                            throw new RevendaException("Revenda inexistente.");
                        }
                        if (!revendaNovoUsuario.equals(usuarioAutenticado.getRevenda())) {
                            throw new AccessDeniedException("Não há permissão para adicionar um usuário nessa loja.");
                        }
                        novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));
                        repository.save(novoUsuario);
                    },
                    () -> {
                        throw new AccessDeniedException("Novo usuário precisa estar vinculado a uma loja.");
                    });
        } else {
            throw new AccessDeniedException("Permissão de acesso negada.");
        }
    }

    public AutenticacaoResponse autenticar(AutenticacaoRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.email(),
                request.senha()
        ));
        Usuario usuario = repository.findByEmail(request.email()).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        String jwt = jwtService.generateToken(usuario);
        return new AutenticacaoResponse(jwt);
    }

    @Secured({"ROLE_ADMIN", "ROLE_PROPRIETARIO"})
    public void adicionarPapeis(List<Papel> papeis, String emailUsuario) {
        DescricaoPapel papelUsarioAutenticado = AutenticacaoUtils.receberPapelPorPrioridade();
        String emailAutenticado = AutenticacaoUtils.receberEmail();
        Usuario autenticado = repository.findByEmail(emailAutenticado).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        Usuario persistido = repository.findByEmail(emailUsuario).orElseThrow(() -> new RuntimeException(""));
        if (papelUsarioAutenticado.equals(DescricaoPapel.PROPRIETARIO)) {
            if (autenticado.getRevenda().equals(persistido.getRevenda())) {
                persistido.setPapeis(papeis);
            } else {
                throw new AccessDeniedException("Autorização negada.");
            }
        } else {
            persistido.setPapeis(papeis);
        }
        repository.save(persistido);
    }
}
