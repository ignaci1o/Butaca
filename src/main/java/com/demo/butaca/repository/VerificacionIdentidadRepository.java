package com.demo.butaca.repository;

import com.demo.butaca.model.VerificacionIdentidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificacionIdentidadRepository extends JpaRepository<VerificacionIdentidad, Long> {
    Optional<VerificacionIdentidad> findByUsuarioIdUsuario(Long idUsuario);
}
