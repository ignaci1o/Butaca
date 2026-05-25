package com.demo.butaca.repository;

import com.demo.butaca.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByUsuarioIdUsuarioOrderByFechaCompraDesc(Long usuarioId);

    @Query("select c from Compra c where c.usuario.idUsuario = :usuarioId order by c.fechaCompra desc")
    List<Compra> findByUsuarioIdOrderByFechaCompraDesc(@Param("usuarioId") Long usuarioId);
}
