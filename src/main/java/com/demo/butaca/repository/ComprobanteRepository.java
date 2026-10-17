package com.demo.butaca.repository;

import com.demo.butaca.model.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {
    Optional<Comprobante> findByPagoIdPago(Long idPago);
    Optional<Comprobante> findByPagoCompraId(Long idCompra);
    Optional<Comprobante> findByNumeroComprobante(String numeroComprobante);
}
