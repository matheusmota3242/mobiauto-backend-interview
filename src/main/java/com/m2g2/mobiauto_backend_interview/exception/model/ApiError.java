package com.m2g2.mobiauto_backend_interview.exception.model;

public class ApiError {

    private String descricao;

    public ApiError(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
