package com.demo.butaca.repository;

import com.demo.butaca.model.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UbicacionRepository extends JpaRepository<Ubicacion, Long> {
    List<Ubicacion> findByActivoTrueOrderByNombreLugarAsc();

    Optional<Ubicacion> findByNombreLugarIgnoreCaseAndDireccionIgnoreCase(String nombreLugar, String direccion);
}
