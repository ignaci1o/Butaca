package com.demo.butaca.service;

import com.demo.butaca.dto.UbicacionDTO;
import com.demo.butaca.model.Ubicacion;
import com.demo.butaca.repository.UbicacionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UbicacionService {
    private final UbicacionRepository ubicacionRepository;

    public UbicacionService(UbicacionRepository ubicacionRepository) {
        this.ubicacionRepository = ubicacionRepository;
    }

    @Transactional(readOnly = true)
    public List<UbicacionDTO> listarActivas() {
        return ubicacionRepository.findByActivoTrueOrderByNombreLugarAsc().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public UbicacionDTO obtener(Long id) {
        return toDTO(buscarUbicacion(id));
    }

    @Transactional
    public UbicacionDTO crear(UbicacionDTO dto) {
        Ubicacion ubicacion = Ubicacion.builder()
                .nombreLugar(dto.getNombreLugar().trim())
                .direccion(dto.getDireccion().trim())
                .capacidadMaxima(dto.getCapacidadMaxima())
                .activo(dto.getActivo() == null || dto.getActivo())
                .build();
        return toDTO(ubicacionRepository.save(ubicacion));
    }

    @Transactional
    public UbicacionDTO actualizar(Long id, UbicacionDTO dto) {
        Ubicacion ubicacion = buscarUbicacion(id);
        ubicacion.setNombreLugar(dto.getNombreLugar().trim());
        ubicacion.setDireccion(dto.getDireccion().trim());
        ubicacion.setCapacidadMaxima(dto.getCapacidadMaxima());
        if (dto.getActivo() != null) {
            ubicacion.setActivo(dto.getActivo());
        }
        return toDTO(ubicacionRepository.save(ubicacion));
    }

    @Transactional
    public void desactivar(Long id) {
        Ubicacion ubicacion = buscarUbicacion(id);
        ubicacion.setActivo(false);
        ubicacionRepository.save(ubicacion);
    }

    private Ubicacion buscarUbicacion(Long id) {
        return ubicacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ubicacion no encontrada"));
    }

    public UbicacionDTO toDTO(Ubicacion ubicacion) {
        return UbicacionDTO.builder()
                .idUbicacion(ubicacion.getIdUbicacion())
                .nombreLugar(ubicacion.getNombreLugar())
                .direccion(ubicacion.getDireccion())
                .capacidadMaxima(ubicacion.getCapacidadMaxima())
                .activo(ubicacion.getActivo())
                .createdAt(ubicacion.getCreatedAt())
                .updatedAt(ubicacion.getUpdatedAt())
                .build();
    }
}
