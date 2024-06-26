package com.m2g2.mobiauto_backend_interview.repository;

import com.m2g2.mobiauto_backend_interview.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u JOIN FETCH u.papeis WHERE u.email = :email")
    Optional<Usuario> findByEmail(@Param("email") String email);
}
