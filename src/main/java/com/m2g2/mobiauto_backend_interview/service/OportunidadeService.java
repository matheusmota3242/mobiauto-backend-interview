package com.m2g2.mobiauto_backend_interview.service;

import com.m2g2.mobiauto_backend_interview.dto.request.AtualizacaoStatusOportunidade;
import com.m2g2.mobiauto_backend_interview.enums.DescricaoPapel;
import com.m2g2.mobiauto_backend_interview.enums.StatusOportunidade;
import com.m2g2.mobiauto_backend_interview.exception.RevendaException;
import com.m2g2.mobiauto_backend_interview.model.Oportunidade;
import com.m2g2.mobiauto_backend_interview.model.Revenda;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.repository.OportunidadeRepository;
import com.m2g2.mobiauto_backend_interview.utils.AutenticacaoUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OportunidadeService {

    private final OportunidadeRepository repository;

    private final UsuarioDetailsServiceImpl userDetailsService;

    public OportunidadeService(OportunidadeRepository repository, UsuarioDetailsServiceImpl userDetailsService) {
        this.repository = repository;
        this.userDetailsService = userDetailsService;
    }

    public void gerar(Oportunidade oportunidade) {
        Usuario usuarioAutorizado = autorizarUsuario(oportunidade.getRevenda());
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
        Oportunidade oportunidade = repository.findById(oportunidadeId).orElseThrow(() -> new RevendaException("Oportunidade inexistente."));
        autorizarUsuario(oportunidade.getRevenda());
        Usuario usuarioResponsavel = userDetailsService.consultarUsuario(emailAssistente);
        oportunidade.setUsuario(usuarioResponsavel);
        repository.save(oportunidade);
    }

    public void atualizarStatus(AtualizacaoStatusOportunidade atualizacaoStatusOportunidade) {
        Oportunidade oportunidade = repository.findById(atualizacaoStatusOportunidade.oportunidadeId()).orElseThrow(() -> new RevendaException("Oportunidade inexistente."));
        autorizarUsuario(oportunidade.getRevenda());
        oportunidade.setStatusOportunidade(atualizacaoStatusOportunidade.statusOportunidade());
        if (atualizacaoStatusOportunidade.statusOportunidade().equals(StatusOportunidade.CONCLUIDA)) {
            oportunidade.setDataFim(LocalDate.now());
        }
        oportunidade.setStatusOportunidade(atualizacaoStatusOportunidade.statusOportunidade());
        repository.save(oportunidade);
    }

    private Usuario autorizarUsuario(Revenda revenda) {
        DescricaoPapel descricaoPapel = AutenticacaoUtils.receberPapelPorPrioridade();
        String emailAutenticado = AutenticacaoUtils.receberEmail();
        Usuario usuarioAutenticado = userDetailsService.consultarUsuario(emailAutenticado);
        if (descricaoPapel.equals(DescricaoPapel.PROPRIETARIO) || descricaoPapel.equals(DescricaoPapel.GERENTE)) {
            if (!revenda.equals(usuarioAutenticado.getRevenda())) {
                throw new AccessDeniedException("Permissão negada para esta loja.");
            }
        }
        return usuarioAutenticado;
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
