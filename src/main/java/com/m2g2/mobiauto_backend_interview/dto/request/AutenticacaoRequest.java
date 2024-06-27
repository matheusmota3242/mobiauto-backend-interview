package com.m2g2.mobiauto_backend_interview.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AutenticacaoRequest(@NotBlank(message = "O campo 'email' precisa estar preenchido.") String email,
                                  @NotBlank(message = "O campo 'senha' precisa estar preenchido.") String senha) {}
