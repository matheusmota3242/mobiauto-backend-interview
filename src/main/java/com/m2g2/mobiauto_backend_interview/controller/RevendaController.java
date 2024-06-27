package com.m2g2.mobiauto_backend_interview.controller;

import com.m2g2.mobiauto_backend_interview.enums.LogEnum;
import com.m2g2.mobiauto_backend_interview.model.Oportunidade;
import com.m2g2.mobiauto_backend_interview.model.Revenda;
import com.m2g2.mobiauto_backend_interview.service.RevendaService;
import jakarta.validation.Valid;
import org.hibernate.validator.constraints.br.CNPJ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/revendas")
@RestController
public class RevendaController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevendaController.class);
    public static final String METODO_SALVAR = "salvar";
    public static final String METODO_CONSULTAR = "consultar";

    private final RevendaService service;

    public RevendaController(RevendaService service) {
        this.service = service;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid Revenda revenda) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_SALVAR, revenda);
        service.salvar(revenda);
        LOGGER.info(LogEnum.SAIDA.getMensagem(), METODO_SALVAR, "{}");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Secured({"ROLE_ADMIN", "ROLE_PROPRIETARIO", "ROLE_GERENTE"})
    @GetMapping("{cnpj}")
    public ResponseEntity<Revenda> consultar(@PathVariable("cnpj") @CNPJ String cnpj) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_CONSULTAR, cnpj);
        Revenda revenda = service.consultar(cnpj);
        LOGGER.info(LogEnum.SAIDA.getMensagem(), METODO_CONSULTAR, revenda);
        return ResponseEntity.ok(revenda);
    }

}