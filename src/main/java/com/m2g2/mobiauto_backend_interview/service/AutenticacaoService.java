package com.m2g2.mobiauto_backend_interview.service;

import com.m2g2.mobiauto_backend_interview.config.JwtService;
import com.m2g2.mobiauto_backend_interview.dto.request.AutenticacaoRequest;
import com.m2g2.mobiauto_backend_interview.dto.response.AutenticacaoResponse;
import com.m2g2.mobiauto_backend_interview.enums.DescricaoPapel;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.repository.UsuarioRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService {

    private final UsuarioRepository repository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private HttpServletRequest request;

    public AutenticacaoService(UsuarioRepository repository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public void registrar(Usuario usuario) {

        String nomeUsuarioAutenticado = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioAutenticado = repository.findByEmail(nomeUsuarioAutenticado).orElseThrow(() -> new IllegalAccessError("Erro grave de autenticação."));

        if (usuarioAutenticado.getPapeis().stream().anyMatch(papel ->
                papel.getDescricao().equals(DescricaoPapel.GERENTE.name()) ||
                        papel.getDescricao().equals(DescricaoPapel.PROPRIETARIO.name()))) {

            if (usuarioAutenticado.getRevenda() != null && usuario.getRevenda() != null && usuarioAutenticado.getRevenda().equals(usuario.getRevenda())) {
                usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
                repository.save(usuario);
                return;
            }
        }

        if (repository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalAccessError("Usuário já cadastrado.");
        }

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        repository.save(usuario);
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
}
