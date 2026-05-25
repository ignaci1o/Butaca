package com.demo.butaca.repository;

import com.demo.butaca.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    List<Evento> findByEstadoPublicacionEvento(String estadoPublicacionEvento);
    List<Evento> findByOrganizadorIdUsuario(Long idUsuario);
    List<Evento> findByCategoriaIdCategoria(Long categoriaId);
    long countByEstadoPublicacionEvento(String estadoPublicacionEvento);
}
