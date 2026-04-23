package com.demo.butaca.controller;

import com.demo.butaca.dto.CompraRequestDTO;
import com.demo.butaca.dto.EntradaResponseDTO;
import com.demo.butaca.service.CompraService;
import com.demo.butaca.service.EntradaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/compras")
public class CompraController {

    private final CompraService compraService;
    private final EntradaService entradaService;

    public CompraController(CompraService compraService, EntradaService entradaService) {
        this.compraService = compraService;
        this.entradaService = entradaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EntradaResponseDTO comprarEntrada(@Valid @RequestBody CompraRequestDTO request) {
        return compraService.comprarEntrada(request);
    }

    @GetMapping("/usuario/{id}")
    public List<EntradaResponseDTO> listarComprasUsuario(@PathVariable Long id) {
        return entradaService.listarEntradasPorUsuario(id);
    }
}
