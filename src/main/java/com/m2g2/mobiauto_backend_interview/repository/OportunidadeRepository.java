package com.m2g2.mobiauto_backend_interview.repository;

import com.m2g2.mobiauto_backend_interview.enums.StatusOportunidade;
import com.m2g2.mobiauto_backend_interview.model.Oportunidade;
import com.m2g2.mobiauto_backend_interview.model.Revenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OportunidadeRepository extends JpaRepository<Oportunidade, Long> {
    @Query("SELECT o FROM Oportunidade o " +
            "JOIN o.usuario u " +
            "JOIN u.papeis p " +
            "WHERE p.descricao = :descricaoPapel " +
            "AND o.revenda.id = :revendaId")
    List<Oportunidade> findByUsuarioPapelAndRevenda(@Param("descricaoPapel") String descricaoPapel, @Param("revendaId") Long revendaId);
}
