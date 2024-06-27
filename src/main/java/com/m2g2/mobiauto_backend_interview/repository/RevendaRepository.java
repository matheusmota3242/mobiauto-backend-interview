package com.m2g2.mobiauto_backend_interview.repository;

import com.m2g2.mobiauto_backend_interview.model.Revenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RevendaRepository extends JpaRepository<Revenda, Long> {

    Optional<Revenda> findByCnpj(String cnpj);

}
