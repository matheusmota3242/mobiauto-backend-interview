package com.m2g2.mobiauto_backend_interview.controller;

import com.m2g2.mobiauto_backend_interview.dto.request.AutenticacaoRequest;
import com.m2g2.mobiauto_backend_interview.dto.response.AutenticacaoResponse;
import com.m2g2.mobiauto_backend_interview.enums.LogEnum;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.service.AutenticacaoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/autenticacao")
@RestController
public class AutenticacaoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AutenticacaoController.class);
    public static final String METODO_REGISTRAR = "registrar";
    public static final String METODO_AUTENTICAR = "autenticar";

    private final AutenticacaoService service;

    public AutenticacaoController(AutenticacaoService service) {
        this.service = service;
    }

    @PostMapping("/registrar")
    public ResponseEntity<AutenticacaoResponse> registrar(@RequestBody @Valid Usuario usuario) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_REGISTRAR, usuario);
        service.registrar(usuario);
        LOGGER.info(LogEnum.SAIDA.getMensagem(), METODO_REGISTRAR, "{}");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AutenticacaoResponse> autenticar(@RequestBody AutenticacaoRequest request) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_AUTENTICAR, request);
        AutenticacaoResponse response = service.autenticar(request);
        LOGGER.info(LogEnum.SAIDA.getMensagem(), METODO_AUTENTICAR, response);
        return ResponseEntity.ok(response);
    }
}
