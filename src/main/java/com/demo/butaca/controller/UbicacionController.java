package com.demo.butaca.controller;

import com.demo.butaca.dto.UbicacionDTO;
import com.demo.butaca.service.UbicacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/ubicaciones")
public class UbicacionController {
    private final UbicacionService ubicacionService;

    public UbicacionController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @GetMapping
    public List<UbicacionDTO> listar() {
        return ubicacionService.listarActivas();
    }

    @GetMapping("/{id}")
    public UbicacionDTO obtener(@PathVariable Long id) {
        return ubicacionService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UbicacionDTO crear(@Valid @RequestBody UbicacionDTO dto) {
        return ubicacionService.crear(dto);
    }

    @PutMapping("/{id}")
    public UbicacionDTO actualizar(@PathVariable Long id, @Valid @RequestBody UbicacionDTO dto) {
        return ubicacionService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desactivar(@PathVariable Long id) {
        ubicacionService.desactivar(id);
    }
}
