package com.m2g2.mobiauto_backend_interview.dto.request;

import com.m2g2.mobiauto_backend_interview.enums.StatusOportunidade;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoStatusOportunidade(@NotNull(message = "Campo 'oportunidadeId' é obrigatório.") Long oportunidadeId, @NotNull(message = "Campo 'statusOportunidade' é obrigatório.") StatusOportunidade statusOportunidade, String motivoConclusao) {}
