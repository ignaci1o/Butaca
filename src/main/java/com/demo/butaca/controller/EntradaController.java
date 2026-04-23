package com.demo.butaca.controller;

import com.demo.butaca.dto.EntradaResponseDTO;
import com.demo.butaca.service.EntradaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/entradas")
public class EntradaController {

    private final EntradaService entradaService;

    public EntradaController(EntradaService entradaService) {
        this.entradaService = entradaService;
    }

    @GetMapping("/validar/{codigo}")
    public EntradaResponseDTO validarEntrada(@PathVariable UUID codigo) {
        return entradaService.validarEntrada(codigo);
    }

    @PutMapping("/{id}/usar")
    public EntradaResponseDTO usarEntrada(@PathVariable Long id) {
        return entradaService.usarEntrada(id);
    }

    @GetMapping("/usuario/{id}")
    public List<EntradaResponseDTO> listarEntradasUsuario(@PathVariable Long id) {
        return entradaService.listarEntradasPorUsuario(id);
    }
}
