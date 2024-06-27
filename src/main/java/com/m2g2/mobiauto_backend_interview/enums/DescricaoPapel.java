package com.m2g2.mobiauto_backend_interview.enums;

public enum DescricaoPapel {
    USUARIO(4),
    ASSISTENTE(3),
    GERENTE(2),
    PROPRIETARIO(1),
    ADMIN(0);

    private final int prioridade;

    DescricaoPapel(int prioridade) {
        this.prioridade = prioridade;
    }

    public int getPrioridade() {
        return prioridade;
    }
}
