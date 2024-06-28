package com.m2g2.mobiauto_backend_interview.controller;

import com.m2g2.mobiauto_backend_interview.dto.request.AutenticacaoRequest;
import com.m2g2.mobiauto_backend_interview.dto.response.AutenticacaoResponse;
import com.m2g2.mobiauto_backend_interview.enums.LogEnum;
import com.m2g2.mobiauto_backend_interview.model.Papel;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.service.UsuarioService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

@RequestMapping("api/v1/usuario")
@RestController
public class UsuarioController {

    public static final String METODO_REMOVER = "remover";
    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);
    public static final String METODO_REGISTRAR = "registrar";
    public static final String METODO_AUTENTICAR = "autenticar";
    public static final String METODO_ATUALIZAR_PAPEIS = "atualizarPapeis";

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Novo usuário registrado",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inconsistentes",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "Permissão de acesso negada.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                    content = @Content)})
    @Secured({"ROLE_ADMIN", "ROLE_GERENTE", "ROLE_PROPRIETARIO"})
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody @Valid Usuario usuario) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_REGISTRAR, usuario);
        service.registrar(usuario);
        LOGGER.info(LogEnum.SAIDA.getMensagem(), METODO_REGISTRAR, "{}");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AutenticacaoResponse.class)) }),
            @ApiResponse(responseCode = "401", description = "E-mal e/ou senha inválido(s).",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                    content = @Content)})
    @PostMapping("/login")
    public ResponseEntity<AutenticacaoResponse> autenticar(@RequestBody @Valid AutenticacaoRequest request) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_AUTENTICAR, request);
        AutenticacaoResponse response = service.autenticar(request);
        LOGGER.info(LogEnum.SAIDA.getMensagem(), METODO_AUTENTICAR, response);
        return ResponseEntity.ok(response);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atualização dos papeis do usuário realizada com sucesso.",
                    content = { @Content(mediaType = "application/json") }),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inconsistentes",
                    content = @Content),
            @ApiResponse(responseCode = "403", description = "Permissão de acesso negada.",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor.",
                    content = @Content)})
    @Secured({"ROLE_ADMIN", "ROLE_PROPRIETARIO"})
    @PutMapping("/papeis/{email}")
    public ResponseEntity<?> atualizarPapeis(@RequestBody List<Papel> papeis, @PathVariable("email") String email) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_ATUALIZAR_PAPEIS, Arrays.asList(email, papeis));
        service.atualizarPapeis(papeis, email);
        LOGGER.info(LogEnum.SAIDA.getMensagem(), METODO_ATUALIZAR_PAPEIS, "{}");
        return ResponseEntity.ok().build();
    }

}
