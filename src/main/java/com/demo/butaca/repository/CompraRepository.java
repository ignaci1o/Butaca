package com.demo.butaca.repository;

import com.demo.butaca.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByClienteIdUsuarioOrderByFechaCompraDesc(Long usuarioId);

    @Query("select c from Compra c where c.cliente.idUsuario = :usuarioId order by c.fechaCompra desc")
    List<Compra> findByUsuarioIdOrderByFechaCompraDesc(@Param("usuarioId") Long usuarioId);

    @Query("""
            select distinct c
            from Compra c
            join c.entradas e
            where e.evento.organizador.idUsuario = :organizadorId
            order by c.fechaCompra desc
            """)
    List<Compra> findByOrganizadorIdOrderByFechaCompraDesc(@Param("organizadorId") Long organizadorId);
}
