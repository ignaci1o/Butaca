package com.demo.butaca.controller;

import com.demo.butaca.dto.VerificacionIdentidadDTO;
import com.demo.butaca.service.VerificacionIdentidadService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/verificaciones")
public class VerificacionIdentidadController {

    private final VerificacionIdentidadService verificacionService;

    public VerificacionIdentidadController(VerificacionIdentidadService verificacionService) {
        this.verificacionService = verificacionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VerificacionIdentidadDTO verificarIdentidad(@RequestBody VerificacionIdentidadDTO request) {
        return verificacionService.verificarIdentidad(request);
    }

    @GetMapping("/usuario/{usuarioId}")
    public VerificacionIdentidadDTO obtenerPorUsuario(@PathVariable Long usuarioId) {
        return verificacionService.obtenerPorUsuario(usuarioId);
    }
}
