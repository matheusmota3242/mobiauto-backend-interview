package com.m2g2.mobiauto_backend_interview.service;

import com.m2g2.mobiauto_backend_interview.config.JwtService;
import com.m2g2.mobiauto_backend_interview.dto.request.AutenticacaoRequest;
import com.m2g2.mobiauto_backend_interview.dto.response.AutenticacaoResponse;
import com.m2g2.mobiauto_backend_interview.model.Papel;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.repository.UsuarioRepository;
import com.m2g2.mobiauto_backend_interview.utils.AutorizacaoUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final AutorizacaoUtils autorizacaoUtils;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder, AutorizacaoUtils autorizacaoUtils, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.autorizacaoUtils = autorizacaoUtils;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public void registrar(Usuario novoUsuario) {
        autorizacaoUtils.autorizarUsuario(novoUsuario.getRevenda());
        novoUsuario.setSenha(passwordEncoder.encode(novoUsuario.getSenha()));
        repository.save(novoUsuario);
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


    public void atualizarPapeis(List<Papel> papeis, String emailUsuario) {
        Usuario persistido = repository.findByEmail(emailUsuario).orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        autorizacaoUtils.autorizarUsuario(persistido.getRevenda());
        persistido.setPapeis(papeis);
        repository.save(persistido);
    }

}
