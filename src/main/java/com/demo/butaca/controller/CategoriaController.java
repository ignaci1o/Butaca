package com.demo.butaca.controller;

import com.demo.butaca.dto.CategoriaDTO;
import com.demo.butaca.service.CategoriaService;
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
@RequestMapping("/api/admin/categorias")
public class CategoriaController {
    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<CategoriaDTO> listar() {
        return categoriaService.listar();
    }

    @GetMapping("/{id}")
    public CategoriaDTO obtener(@PathVariable Long id) {
        return categoriaService.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaDTO crear(@Valid @RequestBody CategoriaDTO dto) {
        return categoriaService.crear(dto);
    }

    @PutMapping("/{id}")
    public CategoriaDTO actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaDTO dto) {
        return categoriaService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
    }
}
