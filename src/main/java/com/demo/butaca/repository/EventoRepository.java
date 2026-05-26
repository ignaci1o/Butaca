package com.demo.butaca.repository;

import com.demo.butaca.model.EstadoEvento;
import com.demo.butaca.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByEstadoPublicacionEventoAndCuposDisponibleEventoGreaterThanOrderByFechaEventoAsc(EstadoEvento estadoPublicacionEvento, Integer cupos);
    List<Evento> findByEstadoPublicacionEventoOrderByFechaEventoAsc(EstadoEvento estadoPublicacionEvento);
    List<Evento> findByOrganizadorIdUsuario(Long idUsuario);
    List<Evento> findByCategoriaIdCategoria(Long categoriaId);
    long countByEstadoPublicacionEvento(EstadoEvento estadoPublicacionEvento);
}
