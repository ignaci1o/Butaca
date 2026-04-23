package com.demo.butaca.service;

import com.demo.butaca.dto.EventoRequestDTO;
import com.demo.butaca.dto.EventoResponseDTO;
import com.demo.butaca.model.Evento;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.EventoRepository;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    public EventoService(EventoRepository eventoRepository, UsuarioRepository usuarioRepository) {
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public EventoResponseDTO crearEvento(EventoRequestDTO request) {
        Usuario organizador = usuarioRepository.findById(request.getOrganizadorIdEvento())
                .orElseThrow(() -> new IllegalArgumentException("Organizador no encontrado"));

        if (!"ORGANIZADOR".equals(organizador.getRolUsuario().name())) {
            throw new IllegalStateException("El usuario no tiene rol de ORGANIZADOR");
        }

        Evento evento = new Evento();
        evento.setTituloEvento(request.getTituloEvento());
        evento.setDescripcionEvento(request.getDescripcionEvento());
        evento.setUbicacionEvento(request.getUbicacionEvento());
        evento.setDetallesUbicacionEvento(request.getDetallesUbicacionEvento());
        evento.setFechaEvento(request.getFechaEvento());
        evento.setHoraEvento(request.getHoraEvento());
        evento.setPrecioEntradaEvento(request.getPrecioEntradaEvento());
        evento.setCuposTotalesEvento(request.getCuposTotalesEvento());
        evento.setCuposDisponibleEvento(request.getCuposTotalesEvento());
        evento.setEstadoPublicacionEvento("BORRADOR");
        evento.setOrganizador(organizador);

        Evento guardado = eventoRepository.save(evento);
        return toDTO(guardado);
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarEventosPublicados() {
        return eventoRepository.findByEstadoPublicacionEvento("PUBLICADO")
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EventoResponseDTO obtenerEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        return toDTO(evento);
    }

    @Transactional
    public EventoResponseDTO editarEvento(Long id, EventoRequestDTO request) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));

        evento.setTituloEvento(request.getTituloEvento());
        evento.setDescripcionEvento(request.getDescripcionEvento());
        evento.setUbicacionEvento(request.getUbicacionEvento());
        evento.setDetallesUbicacionEvento(request.getDetallesUbicacionEvento());
        evento.setFechaEvento(request.getFechaEvento());
        evento.setHoraEvento(request.getHoraEvento());
        evento.setPrecioEntradaEvento(request.getPrecioEntradaEvento());
        // Ajuste basico de cupos
        int diferenciaCupos = request.getCuposTotalesEvento() - evento.getCuposTotalesEvento();
        evento.setCuposTotalesEvento(request.getCuposTotalesEvento());
        evento.setCuposDisponibleEvento(evento.getCuposDisponibleEvento() + diferenciaCupos);

        return toDTO(eventoRepository.save(evento));
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarEventosPorOrganizador(Long idOrganizador) {
        return eventoRepository.findByOrganizadorIdUsuario(idOrganizador)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional
    public void publicarEvento(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        evento.setEstadoPublicacionEvento("PUBLICADO");
        eventoRepository.save(evento);
    }

    public EventoResponseDTO toDTO(Evento evento) {
        EventoResponseDTO dto = new EventoResponseDTO();
        dto.setIdEvento(evento.getIdEvento());
        dto.setTituloEvento(evento.getTituloEvento());
        dto.setDescripcionEvento(evento.getDescripcionEvento());
        dto.setUbicacionEvento(evento.getUbicacionEvento());
        dto.setDetallesUbicacionEvento(evento.getDetallesUbicacionEvento());
        dto.setFechaEvento(evento.getFechaEvento());
        dto.setHoraEvento(evento.getHoraEvento());
        dto.setPrecioEntradaEvento(evento.getPrecioEntradaEvento());
        dto.setCuposTotalesEvento(evento.getCuposTotalesEvento());
        dto.setCuposDisponibleEvento(evento.getCuposDisponibleEvento());
        dto.setEstadoPublicacionEvento(evento.getEstadoPublicacionEvento());
        dto.setOrganizadorIdEvento(evento.getOrganizador().getIdUsuario());
        return dto;
    }
}
