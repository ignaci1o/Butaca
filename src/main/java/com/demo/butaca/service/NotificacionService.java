package com.demo.butaca.service;

import com.demo.butaca.dto.NotificacionDTO;
import com.demo.butaca.model.Evento;
import com.demo.butaca.model.Notificacion;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.EventoRepository;
import com.demo.butaca.repository.NotificacionRepository;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacionService {
    private final NotificacionRepository notificacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final EventoRepository eventoRepository;

    public NotificacionService(NotificacionRepository notificacionRepository, UsuarioRepository usuarioRepository,
                               EventoRepository eventoRepository) {
        this.notificacionRepository = notificacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.eventoRepository = eventoRepository;
    }

    @Transactional
    public NotificacionDTO crearNotificacion(NotificacionDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        Evento evento = dto.getEventoId() == null ? null : eventoRepository.findById(dto.getEventoId())
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        Notificacion notificacion = Notificacion.builder()
                .usuario(usuario)
                .evento(evento)
                .mensaje(dto.getMensaje())
                .tipo(dto.getTipo() == null ? "INFO" : dto.getTipo())
                .leida(false)
                .fechaCreacion(LocalDateTime.now())
                .fechaEnvio(dto.getFechaEnvio() == null ? LocalDateTime.now() : dto.getFechaEnvio())
                .build();
        return toDTO(notificacionRepository.save(notificacion));
    }

    @Transactional(readOnly = true)
    public List<NotificacionDTO> getByUsuario(Long usuarioId) {
        return notificacionRepository.findByUsuarioIdOrderByFechaEnvioDesc(usuarioId).stream().map(this::toDTO).toList();
    }

    @Transactional
    public NotificacionDTO marcarLeida(Long id) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notificacion no encontrada"));
        notificacion.setLeida(true);
        return toDTO(notificacionRepository.save(notificacion));
    }

    @Transactional(readOnly = true)
    public List<NotificacionDTO> getByEvento(Long eventoId) {
        return notificacionRepository.findByEventoId(eventoId).stream().map(this::toDTO).toList();
    }

    public NotificacionDTO toDTO(Notificacion notificacion) {
        return NotificacionDTO.builder()
                .idNotificacion(notificacion.getIdNotificacion())
                .usuarioId(notificacion.getUsuario() == null ? null : notificacion.getUsuario().getIdUsuario())
                .eventoId(notificacion.getEvento() == null ? null : notificacion.getEvento().getIdEvento())
                .mensaje(notificacion.getMensaje())
                .tipo(notificacion.getTipo())
                .leida(notificacion.getLeida())
                .fechaCreacion(notificacion.getFechaCreacion())
                .fechaEnvio(notificacion.getFechaEnvio())
                .build();
    }
}
