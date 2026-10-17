package com.demo.butaca.service;

import com.demo.butaca.dto.AuditoriaSeguridadDTO;
import com.demo.butaca.model.AuditoriaSeguridad;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.AuditoriaSeguridadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditoriaSeguridadService {

    private final AuditoriaSeguridadRepository auditoriaRepository;

    public AuditoriaSeguridadService(AuditoriaSeguridadRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @Transactional
    public AuditoriaSeguridad registrarEvento(Usuario usuario, String accion, String entidad,
                                              Long idRegistro, String resultado, String detalle) {
        return auditoriaRepository.save(AuditoriaSeguridad.builder()
                .usuario(usuario)
                .accionAuditoria(accion)
                .entidadAfectada(entidad)
                .idRegistroAfectado(idRegistro)
                .resultadoAuditoria(resultado == null ? "EXITOSO" : resultado)
                .ipOrigen("127.0.0.1")
                .detalleTracking(detalle)
                .fechaAuditoria(LocalDateTime.now())
                .build());
    }

    @Transactional(readOnly = true)
    public List<AuditoriaSeguridadDTO> listarRecientes() {
        return auditoriaRepository.findTop50ByOrderByFechaAuditoriaDesc()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AuditoriaSeguridadDTO> listarPorUsuario(Long idUsuario) {
        return auditoriaRepository.findByUsuarioIdUsuarioOrderByFechaAuditoriaDesc(idUsuario)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public AuditoriaSeguridadDTO toDTO(AuditoriaSeguridad entidad) {
        return AuditoriaSeguridadDTO.builder()
                .idAuditoria(entidad.getIdAuditoria())
                .usuarioId(entidad.getUsuario() == null ? null : entidad.getUsuario().getIdUsuario())
                .emailUsuario(entidad.getUsuario() == null ? null : entidad.getUsuario().getEmailUsuario())
                .accionAuditoria(entidad.getAccionAuditoria())
                .entidadAfectada(entidad.getEntidadAfectada())
                .idRegistroAfectado(entidad.getIdRegistroAfectado())
                .resultadoAuditoria(entidad.getResultadoAuditoria())
                .ipOrigen(entidad.getIpOrigen())
                .detalleTracking(entidad.getDetalleTracking())
                .fechaAuditoria(entidad.getFechaAuditoria())
                .build();
    }
}
