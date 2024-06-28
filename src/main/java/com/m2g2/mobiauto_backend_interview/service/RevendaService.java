package com.m2g2.mobiauto_backend_interview.service;

import com.m2g2.mobiauto_backend_interview.exception.RevendaInconsistenteException;
import com.m2g2.mobiauto_backend_interview.model.Revenda;
import com.m2g2.mobiauto_backend_interview.repository.RevendaRepository;
import com.m2g2.mobiauto_backend_interview.utils.AutorizacaoUtils;
import org.springframework.stereotype.Service;

@Service
public class RevendaService {

    private final RevendaRepository repository;

    private final AutorizacaoUtils autorizacaoUtils;


    public RevendaService(RevendaRepository repository, AutorizacaoUtils autorizacaoUtils) {
        this.repository = repository;
        this.autorizacaoUtils = autorizacaoUtils;

    }

    public void salvar(Revenda revenda) {
        repository.save(revenda);
    }

    public Revenda consultar(String cnpj) {
        Revenda revenda = repository.findByCnpj(cnpj).orElseThrow(() -> new RevendaInconsistenteException("Revenda não encontrada."));
        autorizacaoUtils.autorizarUsuario(revenda);
        return revenda;
    }

}
