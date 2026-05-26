package com.demo.butaca.controller;

import com.demo.butaca.model.EstadoEvento;
import com.demo.butaca.repository.CategoriaRepository;
import com.demo.butaca.repository.CompraRepository;
import com.demo.butaca.repository.EntradaRepository;
import com.demo.butaca.repository.EventoRepository;
import com.demo.butaca.repository.NotificacionRepository;
import com.demo.butaca.repository.UbicacionRepository;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UsuarioRepository usuarioRepository;
    private final EventoRepository eventoRepository;
    private final CompraRepository compraRepository;
    private final EntradaRepository entradaRepository;
    private final CategoriaRepository categoriaRepository;
    private final NotificacionRepository notificacionRepository;
    private final UbicacionRepository ubicacionRepository;

    public AdminController(UsuarioRepository usuarioRepository, EventoRepository eventoRepository, CompraRepository compraRepository,
                           EntradaRepository entradaRepository, CategoriaRepository categoriaRepository,
                           NotificacionRepository notificacionRepository, UbicacionRepository ubicacionRepository) {
        this.usuarioRepository = usuarioRepository;
        this.eventoRepository = eventoRepository;
        this.compraRepository = compraRepository;
        this.entradaRepository = entradaRepository;
        this.categoriaRepository = categoriaRepository;
        this.notificacionRepository = notificacionRepository;
        this.ubicacionRepository = ubicacionRepository;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return stats();
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        return Map.of(
                "usuarios", usuarioRepository.count(),
                "eventos", eventoRepository.count(),
                "eventos_publicados", eventoRepository.countByEstadoPublicacionEvento(EstadoEvento.PUBLICADO),
                "compras", compraRepository.count(),
                "entradas", entradaRepository.count(),
                "categorias", categoriaRepository.count(),
                "ubicaciones", ubicacionRepository.count(),
                "notificaciones", notificacionRepository.count()
        );
    }
}
