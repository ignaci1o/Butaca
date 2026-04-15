package com.demo.butaca.repository;

import com.demo.butaca.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<Usuario> findByEmailIgnoreCase(String email);
}
