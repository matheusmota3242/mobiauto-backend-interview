package com.m2g2.mobiauto_backend_interview.controller;

import com.m2g2.mobiauto_backend_interview.dto.request.AtualizacaoStatusOportunidade;
import com.m2g2.mobiauto_backend_interview.enums.LogEnum;
import com.m2g2.mobiauto_backend_interview.model.Oportunidade;
import com.m2g2.mobiauto_backend_interview.repository.RevendaRepository;
import com.m2g2.mobiauto_backend_interview.service.OportunidadeService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/revendas/oportunidades")
public class OportunidadeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OportunidadeController.class);

    public static final String METODO_SALVAR = "salvar";
    public static final String METODO_TRANSFERIR = "transferir";
    public static final String METODO_ATUALIZAR_STATUS = "atualizarStatus";
    public static final String METODO_LISTAR_POR_REVENDA = "listarPorRevenda";

    private final OportunidadeService service;

    public OportunidadeController(OportunidadeService service) {
        this.service = service;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Oportunidade registrada com sucesso.",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Oportunidade inconsistente.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Permissão de acesso negada.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                    content = @Content)})
    @Secured({"ROLE_ADMIN", "ROLE_PROPRIETARIO", "ROLE_GERENTE"})
    @PostMapping
    public ResponseEntity<?> gerar(@RequestBody @Valid Oportunidade oportunidade) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_SALVAR, oportunidade);
        service.gerar(oportunidade);
        LOGGER.info(LogEnum.SAIDA.getMensagem(), METODO_SALVAR, "{}");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Oportunidade transferida com sucesso.",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Oportunidade não encontrada.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Permissão de acesso negada.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                    content = @Content)})
    @Secured({"ROLE_ADMIN", "ROLE_PROPRIETARIO", "ROLE_GERENTE"})
    @GetMapping("{id}/transferencias/{email}")
    public ResponseEntity<?> transferir(@PathVariable("id") Long id, @PathVariable("email") String email) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_TRANSFERIR, Arrays.asList(id, email));
        service.transferir(email, id);
        LOGGER.info(LogEnum.SAIDA.getMensagem(), METODO_TRANSFERIR, "{}");
        return ResponseEntity.ok().build();
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status da oportunidade atualizada com sucesso.",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Oportunidade não encontrada.",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Permissão de acesso negada.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                    content = @Content)})
    @Secured({"ROLE_ADMIN", "ROLE_PROPRIETARIO", "ROLE_GERENTE"})
    @PutMapping
    public ResponseEntity<?> atualizarStatus(AtualizacaoStatusOportunidade atualizacaoStatusOportunidade) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_ATUALIZAR_STATUS, atualizacaoStatusOportunidade);
        service.atualizarStatus(atualizacaoStatusOportunidade);
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_ATUALIZAR_STATUS, "{}");
        return ResponseEntity.ok().build();
    }

    @Secured({"ROLE_ADMIN", "ROLE_PROPRIETARIO", "ROLE_GERENTE", "ROLE_ASSISTENTE"})
    @GetMapping("{revendaId}/listar")
    public ResponseEntity<List<Oportunidade>> listarPorRevenda(@PathVariable("revendaId") Long revendaId) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_LISTAR_POR_REVENDA, revendaId);
        List<Oportunidade> oportunidades = service.listarPorRevenda(revendaId);
        LOGGER.info(LogEnum.SAIDA.getMensagem(), METODO_LISTAR_POR_REVENDA, oportunidades);
        return ResponseEntity.ok(oportunidades);
    }
}
