package com.m2g2.mobiauto_backend_interview.model;

import com.m2g2.mobiauto_backend_interview.enums.StatusOportunidade;
import jakarta.persistence.*;

@Entity
public class Oportunidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private StatusOportunidade statusOportunidade;

    private Veiculo veiculo;
}
