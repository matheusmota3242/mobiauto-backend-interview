package com.m2g2.mobiauto_backend_interview.service;

import com.m2g2.mobiauto_backend_interview.enums.DescricaoPapel;
import com.m2g2.mobiauto_backend_interview.enums.StatusOportunidade;
import com.m2g2.mobiauto_backend_interview.exception.RevendaException;
import com.m2g2.mobiauto_backend_interview.model.Oportunidade;
import com.m2g2.mobiauto_backend_interview.model.Revenda;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.repository.OportunidadeRepository;
import com.m2g2.mobiauto_backend_interview.repository.RevendaRepository;
import com.m2g2.mobiauto_backend_interview.utils.AutenticacaoUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RevendaService {

    private final RevendaRepository repository;

    private final OportunidadeRepository oportunidadeRepository;

    private final UsuarioDetailsServiceImpl usuarioDetailsService;

    public RevendaService(RevendaRepository repository, OportunidadeRepository oportunidadeRepository, UsuarioDetailsServiceImpl usuarioDetailsService) {
        this.repository = repository;
        this.oportunidadeRepository = oportunidadeRepository;
        this.usuarioDetailsService = usuarioDetailsService;
    }

    public void salvar(Revenda revenda) {
        repository.save(revenda);
    }

    public Revenda consultar(String cnpj) {
        DescricaoPapel descricaoPapel = AutenticacaoUtils.receberPapelPorPrioridade();
        Revenda revenda = repository.findByCnpj(cnpj).orElseThrow(() -> new RevendaException("Revenda não encontrada."));
        if (!descricaoPapel.equals(DescricaoPapel.ADMIN)) {
            String emailAutenticado = AutenticacaoUtils.receberEmail();
            Usuario autenticado = usuarioDetailsService.consultarUsuario(emailAutenticado);
            if (revenda.equals(autenticado.getRevenda())) {
                return revenda;
            }
            throw new AccessDeniedException("Permissão negada para esta loja.");
        }
        return revenda;
    }

}
