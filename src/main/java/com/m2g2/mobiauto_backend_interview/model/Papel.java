package com.m2g2.mobiauto_backend_interview.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Papel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String descricao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public String toString() {
        return "Papel{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                '}';
    }
}
