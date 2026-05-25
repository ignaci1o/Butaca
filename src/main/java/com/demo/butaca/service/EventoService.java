package com.demo.butaca.service;

import com.demo.butaca.dto.EventoRequestDTO;
import com.demo.butaca.dto.EventoResponseDTO;
import com.demo.butaca.model.Categoria;
import com.demo.butaca.model.Evento;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.CategoriaRepository;
import com.demo.butaca.repository.EventoRepository;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;

    public EventoService(EventoRepository eventoRepository, UsuarioRepository usuarioRepository, CategoriaRepository categoriaRepository) {
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public EventoResponseDTO crearEvento(EventoRequestDTO request) {
        Usuario organizador = usuarioRepository.findById(request.getOrganizadorIdEvento())
                .orElseThrow(() -> new IllegalArgumentException("Organizador no encontrado"));

        Evento evento = new Evento();
        aplicarDatos(evento, request);
        evento.setCuposDisponibleEvento(request.getCuposTotalesEvento());
        evento.setEstadoPublicacionEvento(request.getEstadoPublicacionEvento() == null ? "BORRADOR" : request.getEstadoPublicacionEvento());
        evento.setOrganizador(organizador);

        return toDTO(eventoRepository.save(evento));
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarEventosPublicados(Long categoriaId) {
        List<Evento> eventos = categoriaId == null
                ? eventoRepository.findByEstadoPublicacionEvento("PUBLICADO")
                : eventoRepository.findByCategoriaIdCategoria(categoriaId).stream()
                        .filter(evento -> "PUBLICADO".equals(evento.getEstadoPublicacionEvento()))
                        .toList();
        return eventos.stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarEventosPublicados() {
        return listarEventosPublicados(null);
    }

    @Transactional(readOnly = true)
    public EventoResponseDTO obtenerEvento(Long id) {
        return toDTO(buscarEvento(id));
    }

    @Transactional
    public EventoResponseDTO editarEvento(Long id, EventoRequestDTO request) {
        Evento evento = buscarEvento(id);
        int vendidos = evento.getCuposTotalesEvento() - evento.getCuposDisponibleEvento();
        if (request.getCuposTotalesEvento() < vendidos) {
            throw new IllegalStateException("Los cupos totales no pueden ser menores a los ya vendidos");
        }
        aplicarDatos(evento, request);
        evento.setCuposDisponibleEvento(request.getCuposTotalesEvento() - vendidos);
        if (request.getEstadoPublicacionEvento() != null) {
            evento.setEstadoPublicacionEvento(request.getEstadoPublicacionEvento());
        }
        return toDTO(eventoRepository.save(evento));
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarEventosPorOrganizador(Long idOrganizador) {
        return eventoRepository.findByOrganizadorIdUsuario(idOrganizador).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarEventosPorCategoria(Long categoriaId) {
        return eventoRepository.findByCategoriaIdCategoria(categoriaId).stream().map(this::toDTO).toList();
    }

    @Transactional
    public void publicarEvento(Long id) {
        Evento evento = buscarEvento(id);
        evento.setEstadoPublicacionEvento("PUBLICADO");
        eventoRepository.save(evento);
    }

    @Transactional
    public EventoResponseDTO actualizarCuposDisponibles(Long id, int cuposDisponibles) {
        Evento evento = buscarEvento(id);
        if (cuposDisponibles < 0 || cuposDisponibles > evento.getCuposTotalesEvento()) {
            throw new IllegalArgumentException("Cupos disponibles fuera de rango");
        }
        evento.setCuposDisponibleEvento(cuposDisponibles);
        return toDTO(eventoRepository.save(evento));
    }

    @Transactional
    public void descontarCupos(Evento evento, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if (evento.getCuposDisponibleEvento() < cantidad) {
            throw new IllegalStateException("Cupos insuficientes");
        }
        evento.setCuposDisponibleEvento(evento.getCuposDisponibleEvento() - cantidad);
        eventoRepository.save(evento);
    }

    private Evento buscarEvento(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
    }

    private void aplicarDatos(Evento evento, EventoRequestDTO request) {
        evento.setTituloEvento(request.getTituloEvento());
        evento.setDescripcionEvento(request.getDescripcionEvento());
        evento.setUbicacionEvento(request.getUbicacionEvento());
        evento.setDetallesUbicacionEvento(request.getDetallesUbicacionEvento());
        evento.setFechaEvento(request.getFechaEvento());
        evento.setHoraEvento(request.getHoraEvento());
        evento.setPrecioEntradaEvento(request.getPrecioEntradaEvento());
        evento.setCuposTotalesEvento(request.getCuposTotalesEvento());
        evento.setImagenUrl(request.getImagenUrl());
        evento.setCategoria(buscarCategoria(request.getCategoriaId()));
    }

    private Categoria buscarCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
    }

    public EventoResponseDTO toDTO(Evento evento) {
        Integer cupos = evento.getCuposDisponibleEvento();
        return EventoResponseDTO.builder()
                .idEvento(evento.getIdEvento())
                .tituloEvento(evento.getTituloEvento())
                .descripcionEvento(evento.getDescripcionEvento())
                .ubicacionEvento(evento.getUbicacionEvento())
                .detallesUbicacionEvento(evento.getDetallesUbicacionEvento())
                .fechaEvento(evento.getFechaEvento())
                .horaEvento(evento.getHoraEvento())
                .precioEntradaEvento(evento.getPrecioEntradaEvento())
                .cuposTotalesEvento(evento.getCuposTotalesEvento())
                .cuposDisponibles(cupos)
                .cuposDisponibleEvento(cupos)
                .estadoPublicacionEvento(evento.getEstadoPublicacionEvento())
                .imagenUrl(evento.getImagenUrl())
                .categoriaId(evento.getCategoria() == null ? null : evento.getCategoria().getIdCategoria())
                .categoriaNombre(evento.getCategoria() == null ? null : evento.getCategoria().getNombre())
                .organizadorIdEvento(evento.getOrganizador() == null ? null : evento.getOrganizador().getIdUsuario())
                .build();
    }
}
