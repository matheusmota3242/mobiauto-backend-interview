package com.m2g2.mobiauto_backend_interview.repository;

import com.m2g2.mobiauto_backend_interview.model.Papel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PapelRepository extends JpaRepository<Papel, Long> {
    Optional<Papel> findByDescricao(String descricao);
}
