package com.demo.butaca.repository;

import com.demo.butaca.model.HistorialEstadoEntrada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistorialEstadoEntradaRepository extends JpaRepository<HistorialEstadoEntrada, Long> {
    List<HistorialEstadoEntrada> findByEntradaIdEntradaOrderByFechaCambioEntradaAsc(Long idEntrada);
}
