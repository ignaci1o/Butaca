package com.demo.butaca.service;

import com.demo.butaca.dto.EventoRequestDTO;
import com.demo.butaca.dto.EventoResponseDTO;
import com.demo.butaca.model.Categoria;
import com.demo.butaca.model.EstadoEvento;
import com.demo.butaca.model.Evento;
import com.demo.butaca.model.Ubicacion;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.CategoriaRepository;
import com.demo.butaca.repository.EventoRepository;
import com.demo.butaca.repository.UbicacionRepository;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final UbicacionRepository ubicacionRepository;

    public EventoService(EventoRepository eventoRepository, UsuarioRepository usuarioRepository,
                         CategoriaRepository categoriaRepository, UbicacionRepository ubicacionRepository) {
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.ubicacionRepository = ubicacionRepository;
    }

    @Transactional
    public EventoResponseDTO crearEvento(EventoRequestDTO request) {
        Usuario organizador = usuarioRepository.findById(request.getOrganizadorIdEvento())
                .orElseThrow(() -> new IllegalArgumentException("Organizador no encontrado"));

        Evento evento = new Evento();
        aplicarDatos(evento, request);
        evento.setCuposDisponibleEvento(request.getCuposTotalesEvento());
        evento.setEstadoPublicacionEvento(parseEstado(request.getEstadoPublicacionEvento(), EstadoEvento.BORRADOR));
        evento.setOrganizador(organizador);

        return toDTO(eventoRepository.save(evento));
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarEventosPublicados(Long categoriaId) {
        List<Evento> eventos = eventoRepository
                .findByEstadoPublicacionEventoAndCuposDisponibleEventoGreaterThanOrderByFechaEventoAsc(EstadoEvento.PUBLICADO, 0);

        if (categoriaId != null) {
            eventos = eventos.stream()
                    .filter(evento -> evento.getCategoria() != null
                            && categoriaId.equals(evento.getCategoria().getIdCategoria()))
                    .toList();
        }

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
            evento.setEstadoPublicacionEvento(parseEstado(request.getEstadoPublicacionEvento(), evento.getEstadoPublicacionEvento()));
        }
        return toDTO(eventoRepository.save(evento));
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarEventosPorOrganizador(Long idOrganizador) {
        return eventoRepository.findByOrganizadorIdUsuario(idOrganizador).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<EventoResponseDTO> listarEventosPorCategoria(Long categoriaId) {
        return eventoRepository.findByCategoriaIdCategoria(categoriaId).stream()
                .filter(evento -> EstadoEvento.PUBLICADO.equals(evento.getEstadoPublicacionEvento()))
                .filter(evento -> evento.getCuposDisponibleEvento() > 0)
                .map(this::toDTO)
                .toList();
    }

    @Transactional
    public void publicarEvento(Long id) {
        Evento evento = buscarEvento(id);
        if (evento.getCuposTotalesEvento() <= 0 || evento.getCuposDisponibleEvento() < 0) {
            throw new IllegalStateException("El evento no tiene cupos validos para publicarse");
        }
        evento.setEstadoPublicacionEvento(EstadoEvento.PUBLICADO);
        eventoRepository.save(evento);
    }

    @Transactional
    public void cancelarEvento(Long id) {
        Evento evento = buscarEvento(id);
        evento.setEstadoPublicacionEvento(EstadoEvento.CANCELADO);
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
        if (!EstadoEvento.PUBLICADO.equals(evento.getEstadoPublicacionEvento())) {
            throw new IllegalStateException("El evento no esta publicado");
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
        Ubicacion ubicacion = resolverUbicacion(request);
        if (request.getCuposTotalesEvento() > ubicacion.getCapacidadMaxima()) {
            throw new IllegalArgumentException("Los cupos totales no pueden superar la capacidad de la ubicacion");
        }

        evento.setTituloEvento(request.getTituloEvento().trim());
        evento.setDescripcionEvento(request.getDescripcionEvento());
        evento.setFechaEvento(request.getFechaEvento());
        evento.setHoraEvento(request.getHoraEvento());
        evento.setPrecioEntradaEvento(request.getPrecioEntradaEvento());
        evento.setCuposTotalesEvento(request.getCuposTotalesEvento());
        evento.setImagenUrl(request.getImagenUrl());
        evento.setCategoria(buscarCategoria(request.getCategoriaId()));
        evento.setUbicacion(ubicacion);
    }

    private Categoria buscarCategoria(Long categoriaId) {
        if (categoriaId == null) {
            return null;
        }
        return categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
    }

    private Ubicacion resolverUbicacion(EventoRequestDTO request) {
        if (request.getUbicacionId() != null) {
            Ubicacion ubicacion = ubicacionRepository.findById(request.getUbicacionId())
                    .orElseThrow(() -> new IllegalArgumentException("Ubicacion no encontrada"));
            if (!Boolean.TRUE.equals(ubicacion.getActivo())) {
                throw new IllegalStateException("La ubicacion seleccionada no esta activa");
            }
            return ubicacion;
        }

        String nombreLugar = primerTexto(request.getNombreLugar(), request.getUbicacionEvento());
        String direccion = primerTexto(request.getDireccion(), request.getDetallesUbicacionEvento(), request.getUbicacionEvento());
        Integer capacidad = request.getCapacidadMaxima() == null ? request.getCuposTotalesEvento() : request.getCapacidadMaxima();

        if (nombreLugar == null || direccion == null || capacidad == null || capacidad <= 0) {
            throw new IllegalArgumentException("Debe indicar ubicacion_id o los datos completos de la ubicacion");
        }

        String nombreNormalizado = nombreLugar.trim();
        String direccionNormalizada = direccion.trim();
        return ubicacionRepository
                .findByNombreLugarIgnoreCaseAndDireccionIgnoreCase(nombreNormalizado, direccionNormalizada)
                .orElseGet(() -> ubicacionRepository.save(Ubicacion.builder()
                        .nombreLugar(nombreNormalizado)
                        .direccion(direccionNormalizada)
                        .capacidadMaxima(capacidad)
                        .activo(true)
                        .build()));
    }

    private String primerTexto(String... valores) {
        for (String valor : valores) {
            if (valor != null && !valor.isBlank()) {
                return valor;
            }
        }
        return null;
    }

    private EstadoEvento parseEstado(String raw, EstadoEvento defaultValue) {
        if (raw == null || raw.isBlank()) {
            return defaultValue;
        }
        return EstadoEvento.valueOf(raw.trim().toUpperCase());
    }

    public EventoResponseDTO toDTO(Evento evento) {
        Ubicacion ubicacion = evento.getUbicacion();
        Integer cupos = evento.getCuposDisponibleEvento();
        return EventoResponseDTO.builder()
                .idEvento(evento.getIdEvento())
                .tituloEvento(evento.getTituloEvento())
                .descripcionEvento(evento.getDescripcionEvento())
                .ubicacionEvento(ubicacion == null ? null : ubicacion.getNombreLugar())
                .detallesUbicacionEvento(ubicacion == null ? null : ubicacion.getDireccion())
                .ubicacionId(ubicacion == null ? null : ubicacion.getIdUbicacion())
                .ubicacionNombre(ubicacion == null ? null : ubicacion.getNombreLugar())
                .ubicacionDireccion(ubicacion == null ? null : ubicacion.getDireccion())
                .capacidadMaxima(ubicacion == null ? null : ubicacion.getCapacidadMaxima())
                .fechaEvento(evento.getFechaEvento())
                .horaEvento(evento.getHoraEvento())
                .precioEntradaEvento(evento.getPrecioEntradaEvento())
                .cuposTotalesEvento(evento.getCuposTotalesEvento())
                .cuposDisponibles(cupos)
                .cuposDisponibleEvento(cupos)
                .estadoPublicacionEvento(evento.getEstadoPublicacionEvento().name())
                .imagenUrl(evento.getImagenUrl())
                .categoriaId(evento.getCategoria() == null ? null : evento.getCategoria().getIdCategoria())
                .categoriaNombre(evento.getCategoria() == null ? null : evento.getCategoria().getNombre())
                .organizadorIdEvento(evento.getOrganizador() == null ? null : evento.getOrganizador().getIdUsuario())
                .version(evento.getVersion())
                .createdAt(evento.getCreatedAt())
                .updatedAt(evento.getUpdatedAt())
                .build();
    }
}
