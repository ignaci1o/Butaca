package com.demo.butaca.controller;

import com.demo.butaca.dto.NotificacionDTO;
import com.demo.butaca.service.NotificacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/notificaciones")
public class NotificacionController {
    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificacionDTO crear(@Valid @RequestBody NotificacionDTO dto) {
        return notificacionService.crearNotificacion(dto);
    }

    @GetMapping("/usuario/{id}")
    public List<NotificacionDTO> porUsuario(@PathVariable Long id) {
        return notificacionService.getByUsuario(id);
    }

    @GetMapping("/evento/{eventoId}")
    public List<NotificacionDTO> porEvento(@PathVariable Long eventoId) {
        return notificacionService.getByEvento(eventoId);
    }

    @PutMapping("/{id}/leida")
    public NotificacionDTO marcarLeida(@PathVariable Long id) {
        return notificacionService.marcarLeida(id);
    }
}
