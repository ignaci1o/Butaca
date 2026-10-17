package com.demo.butaca.repository;

import com.demo.butaca.model.AuditoriaSeguridad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditoriaSeguridadRepository extends JpaRepository<AuditoriaSeguridad, Long> {
    List<AuditoriaSeguridad> findTop50ByOrderByFechaAuditoriaDesc();
    List<AuditoriaSeguridad> findByUsuarioIdUsuarioOrderByFechaAuditoriaDesc(Long idUsuario);
}
