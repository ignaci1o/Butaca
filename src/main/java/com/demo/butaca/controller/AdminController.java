package com.demo.butaca.controller;

import com.demo.butaca.dto.AuditoriaSeguridadDTO;
import com.demo.butaca.dto.CompraResponseDTO;
import com.demo.butaca.model.EstadoEvento;
import com.demo.butaca.repository.AuditoriaSeguridadRepository;
import com.demo.butaca.repository.CategoriaRepository;
import com.demo.butaca.repository.CompraRepository;
import com.demo.butaca.repository.ComprobanteRepository;
import com.demo.butaca.repository.EntradaRepository;
import com.demo.butaca.repository.EventoRepository;
import com.demo.butaca.repository.NotificacionRepository;
import com.demo.butaca.repository.PagoRepository;
import com.demo.butaca.repository.UbicacionRepository;
import com.demo.butaca.repository.UsuarioRepository;
import com.demo.butaca.repository.VerificacionIdentidadRepository;
import com.demo.butaca.service.AuditoriaSeguridadService;
import com.demo.butaca.service.CompraService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
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
    private final VerificacionIdentidadRepository verificacionRepository;
    private final PagoRepository pagoRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final AuditoriaSeguridadRepository auditoriaRepository;
    private final AuditoriaSeguridadService auditoriaService;
    private final CompraService compraService;

    public AdminController(UsuarioRepository usuarioRepository,
                           EventoRepository eventoRepository,
                           CompraRepository compraRepository,
                           EntradaRepository entradaRepository,
                           CategoriaRepository categoriaRepository,
                           NotificacionRepository notificacionRepository,
                           UbicacionRepository ubicacionRepository,
                           VerificacionIdentidadRepository verificacionRepository,
                           PagoRepository pagoRepository,
                           ComprobanteRepository comprobanteRepository,
                           AuditoriaSeguridadRepository auditoriaRepository,
                           AuditoriaSeguridadService auditoriaService,
                           CompraService compraService) {
        this.usuarioRepository = usuarioRepository;
        this.eventoRepository = eventoRepository;
        this.compraRepository = compraRepository;
        this.entradaRepository = entradaRepository;
        this.categoriaRepository = categoriaRepository;
        this.notificacionRepository = notificacionRepository;
        this.ubicacionRepository = ubicacionRepository;
        this.verificacionRepository = verificacionRepository;
        this.pagoRepository = pagoRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.auditoriaRepository = auditoriaRepository;
        this.auditoriaService = auditoriaService;
        this.compraService = compraService;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        return stats();
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        Map<String, Object> resultado = new LinkedHashMap<>();
        resultado.put("usuarios", usuarioRepository.count());
        resultado.put("verificaciones_kyc", verificacionRepository.count());
        resultado.put("eventos", eventoRepository.count());
        resultado.put("eventos_publicados", eventoRepository.countByEstadoPublicacionEvento(EstadoEvento.PUBLICADO));
        resultado.put("compras", compraRepository.count());
        resultado.put("pagos_aprobados", pagoRepository.count());
        resultado.put("comprobantes_emitidos", comprobanteRepository.count());
        resultado.put("entradas", entradaRepository.count());
        resultado.put("categorias", categoriaRepository.count());
        resultado.put("ubicaciones", ubicacionRepository.count());
        resultado.put("notificaciones", notificacionRepository.count());
        resultado.put("eventos_auditoria", auditoriaRepository.count());
        return resultado;
    }

    @GetMapping("/auditoria")
    public List<AuditoriaSeguridadDTO> listarAuditoria() {
        return auditoriaService.listarRecientes();
    }

    @GetMapping("/compras")
    public List<CompraResponseDTO> listarCompras() {
        return compraService.listarTodas();
    }
}

