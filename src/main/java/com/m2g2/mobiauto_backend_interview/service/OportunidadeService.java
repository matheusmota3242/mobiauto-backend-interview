package com.m2g2.mobiauto_backend_interview.service;

import com.m2g2.mobiauto_backend_interview.dto.request.AtualizacaoStatusOportunidade;
import com.m2g2.mobiauto_backend_interview.enums.DescricaoPapel;
import com.m2g2.mobiauto_backend_interview.enums.StatusOportunidade;
import com.m2g2.mobiauto_backend_interview.exception.RevendaInconsistenteException;
import com.m2g2.mobiauto_backend_interview.model.Oportunidade;
import com.m2g2.mobiauto_backend_interview.model.Revenda;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.repository.OportunidadeRepository;
import com.m2g2.mobiauto_backend_interview.repository.RevendaRepository;
import com.m2g2.mobiauto_backend_interview.utils.AutorizacaoUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OportunidadeService {

    private final OportunidadeRepository repository;
    private final UserDetailsServiceImpl userDetailsService;

    private final AutorizacaoUtils autorizacaoUtils;

    public OportunidadeService(OportunidadeRepository repository, UserDetailsServiceImpl userDetailsService, AutorizacaoUtils autorizacaoUtils) {
        this.repository = repository;
        this.userDetailsService = userDetailsService;
        this.autorizacaoUtils = autorizacaoUtils;
    }

    public void gerar(Oportunidade oportunidade) {
        Usuario usuarioAutorizado = autorizacaoUtils.autorizarUsuario(oportunidade.getRevenda());
        oportunidade.setStatusOportunidade(StatusOportunidade.NOVA);
        oportunidade.setDataInicio(LocalDate.now());
        Optional<Usuario> usuarioSelecionado = Optional.ofNullable(selecionarUsuario(oportunidade.getRevenda().getId()));
        usuarioSelecionado.ifPresentOrElse(
                oportunidade::setUsuario,
                () -> oportunidade.setUsuario(usuarioAutorizado)
        );
        repository.save(oportunidade);
    }

    public void transferir(String emailAssistente, Long oportunidadeId) {
        Oportunidade oportunidade = repository.findById(oportunidadeId).orElseThrow(() -> new RevendaInconsistenteException("Oportunidade inexistente."));
        autorizacaoUtils.autorizarUsuario(oportunidade.getRevenda());
        Usuario usuarioResponsavel = userDetailsService.consultarUsuario(emailAssistente);
        oportunidade.setUsuario(usuarioResponsavel);
        oportunidade.setDataUltimaTransferencia(LocalDate.now());
        repository.save(oportunidade);
    }

    public void atualizarStatus(AtualizacaoStatusOportunidade atualizacaoStatusOportunidade) {
        Oportunidade oportunidade = repository.findById(atualizacaoStatusOportunidade.oportunidadeId()).orElseThrow(() -> new RevendaInconsistenteException("Oportunidade inexistente."));
        autorizacaoUtils.autorizarUsuario(oportunidade.getRevenda());
        oportunidade.setStatusOportunidade(atualizacaoStatusOportunidade.statusOportunidade());
        if (atualizacaoStatusOportunidade.statusOportunidade().equals(StatusOportunidade.CONCLUIDA)) {
            oportunidade.setDataFim(LocalDate.now());
            oportunidade.setMotivoConclusao(atualizacaoStatusOportunidade.motivoConclusao());
        }
        oportunidade.setStatusOportunidade(atualizacaoStatusOportunidade.statusOportunidade());
        repository.save(oportunidade);
    }

    public List<Oportunidade> listarPorRevenda(Long revendaId) {
        Revenda revenda = new Revenda();
        revenda.setId(revendaId);
        return repository.findByRevenda(revenda);
    }

    private Usuario selecionarUsuario(Long revendaId) {
        List<Oportunidade> oportunidadesPorRevenda = repository.findByUsuarioPapelAndRevenda(DescricaoPapel.ASSISTENTE.name(), revendaId);
        Map<Usuario, List<Oportunidade>> oportunidadesEmAtendimento = oportunidadesPorRevenda.stream()
                .filter(oport -> StatusOportunidade.EM_ATENDIMENTO.equals(oport.getStatusOportunidade()))
                .collect(Collectors.groupingBy(Oportunidade::getUsuario));
        Optional<Integer> menorQuantidadeOportunidades = oportunidadesEmAtendimento.values()
                .stream()
                .map(List::size)
                .min(Integer::compareTo);

        oportunidadesEmAtendimento = oportunidadesEmAtendimento.entrySet()
                .stream()
                .filter(entry -> entry.getValue().size() == menorQuantidadeOportunidades.get())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return oportunidadesEmAtendimento.keySet().stream().findFirst().orElse(null);
    }


}
