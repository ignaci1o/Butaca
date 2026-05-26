package com.demo.butaca.repository;

import com.demo.butaca.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByClienteIdUsuarioOrderByFechaCompraDesc(Long usuarioId);
    List<Compra> findByEventoOrganizadorIdUsuarioOrderByFechaCompraDesc(Long organizadorId);

    @Query("select c from Compra c where c.cliente.idUsuario = :usuarioId order by c.fechaCompra desc")
    List<Compra> findByUsuarioIdOrderByFechaCompraDesc(@Param("usuarioId") Long usuarioId);
}
