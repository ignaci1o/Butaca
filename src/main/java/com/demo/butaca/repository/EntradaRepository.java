package com.demo.butaca.repository;

import com.demo.butaca.model.Entrada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntradaRepository extends JpaRepository<Entrada, Long> {
    Optional<Entrada> findByCodigoUnicoEntrada(UUID codigoUnicoEntrada);
    List<Entrada> findByUsuarioIdUsuario(Long idUsuario);
    List<Entrada> findByEventoIdEvento(Long idEvento);
}
