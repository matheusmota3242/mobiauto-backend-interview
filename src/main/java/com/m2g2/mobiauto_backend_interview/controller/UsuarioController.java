package com.m2g2.mobiauto_backend_interview.controller;

import com.m2g2.mobiauto_backend_interview.dto.request.AutenticacaoRequest;
import com.m2g2.mobiauto_backend_interview.dto.response.AutenticacaoResponse;
import com.m2g2.mobiauto_backend_interview.enums.LogEnum;
import com.m2g2.mobiauto_backend_interview.model.Papel;
import com.m2g2.mobiauto_backend_interview.model.Usuario;
import com.m2g2.mobiauto_backend_interview.service.UsuarioService;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(UsuarioController.class);
    public static final String METODO_REGISTRAR = "registrar";
    public static final String METODO_AUTENTICAR = "autenticar";

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Secured({"ROLE_ADMIN", "ROLE_GERENTE", "ROLE_PROPRIETARIO"})
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody @Valid Usuario usuario) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_REGISTRAR, usuario);
        service.registrar(usuario);
        LOGGER.info(LogEnum.SAIDA.getMensagem(), METODO_REGISTRAR, "{}");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AutenticacaoResponse> autenticar(@RequestBody @Valid AutenticacaoRequest request) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), METODO_AUTENTICAR, request);
        AutenticacaoResponse response = service.autenticar(request);
        LOGGER.info(LogEnum.SAIDA.getMensagem(), METODO_AUTENTICAR, response);
        return ResponseEntity.ok(response);
    }

    @Secured({"ROLE_ADMIN", "ROLE_PROPRIETARIO"})
    @PutMapping("/papeis/{email}")
    public ResponseEntity<?> atualizarPapeis(@RequestBody List<Papel> papeis, @PathVariable("email") String email) {
        LOGGER.info(LogEnum.ENTRADA.getMensagem(), "atualizarPapeis", Arrays.asList(email, papeis));
        service.adicionarPapeis(papeis, email);
        LOGGER.info(LogEnum.SAIDA.getMensagem(), "atualizarPapeis", "{}");
        return ResponseEntity.ok().build();
    }
}
