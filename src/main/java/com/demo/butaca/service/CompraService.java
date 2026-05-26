package com.demo.butaca.service;

import com.demo.butaca.dto.CompraRequestDTO;
import com.demo.butaca.dto.CompraResponseDTO;
import com.demo.butaca.dto.EntradaResponseDTO;
import com.demo.butaca.dto.NotificacionDTO;
import com.demo.butaca.model.Compra;
import com.demo.butaca.model.Entrada;
import com.demo.butaca.model.EstadoEntrada;
import com.demo.butaca.model.Evento;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.CompraRepository;
import com.demo.butaca.repository.EntradaRepository;
import com.demo.butaca.repository.EventoRepository;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EntradaRepository entradaRepository;
    private final EntradaService entradaService;
    private final EventoService eventoService;
    private final NotificacionService notificacionService;

    public CompraService(CompraRepository compraRepository, EventoRepository eventoRepository, UsuarioRepository usuarioRepository,
                         EntradaRepository entradaRepository, EntradaService entradaService, EventoService eventoService,
                         NotificacionService notificacionService) {
        this.compraRepository = compraRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.entradaRepository = entradaRepository;
        this.entradaService = entradaService;
        this.eventoService = eventoService;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public CompraResponseDTO comprarEntrada(CompraRequestDTO request) {
        Evento evento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        Usuario cliente = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        int cantidad = request.getCantidad() == null ? 1 : request.getCantidad();

        eventoService.descontarCupos(evento, cantidad);
        LocalDateTime fechaCompra = LocalDateTime.now();
        BigDecimal total = evento.getPrecioEntradaEvento().multiply(BigDecimal.valueOf(cantidad));

        Compra compra = Compra.builder()
                .fechaCompra(fechaCompra)
                .cantidad(cantidad)
                .total(total)
                .cliente(cliente)
                .evento(evento)
                .build();
        compra = compraRepository.save(compra);

        List<Entrada> entradas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            Entrada entrada = Entrada.builder()
                    .codigoUnicoEntrada(UUID.randomUUID())
                    .estadoEntrada(EstadoEntrada.CONFIRMADA)
                    .precioPagado(evento.getPrecioEntradaEvento())
                    .fechaEmision(fechaCompra)
                    .evento(evento)
                    .cliente(cliente)
                    .compra(compra)
                    .build();
            entradas.add(entradaRepository.save(entrada));
        }
        compra.setEntradas(entradas);
        registrarNotificacionCompra(compra);
        return toDTO(compra);
    }

    @Transactional(readOnly = true)
    public CompraResponseDTO obtenerCompra(Long id) {
        return toDTO(compraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Compra no encontrada")));
    }

    @Transactional(readOnly = true)
    public List<CompraResponseDTO> listarComprasUsuario(Long usuarioId) {
        return compraRepository.findByUsuarioIdOrderByFechaCompraDesc(usuarioId).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<CompraResponseDTO> listarComprasOrganizador(Long organizadorId) {
        return compraRepository.findByEventoOrganizadorIdUsuarioOrderByFechaCompraDesc(organizadorId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private void registrarNotificacionCompra(Compra compra) {
        NotificacionDTO dto = NotificacionDTO.builder()
                .usuarioId(compra.getCliente().getIdUsuario())
                .eventoId(compra.getEvento().getIdEvento())
                .mensaje("Compra confirmada para " + compra.getEvento().getTituloEvento())
                .tipo("COMPRA")
                .fechaEnvio(LocalDateTime.now())
                .build();
        notificacionService.crearNotificacion(dto);
    }

    public CompraResponseDTO toDTO(Compra compra) {
        List<EntradaResponseDTO> entradas = compra.getEntradas() == null
                ? List.of()
                : compra.getEntradas().stream().map(entradaService::toDTO).toList();
        Long clienteId = compra.getCliente() == null ? null : compra.getCliente().getIdUsuario();
        String clienteEmail = compra.getCliente() == null ? null : compra.getCliente().getEmailUsuario();
        return CompraResponseDTO.builder()
                .id(compra.getId())
                .fechaCompra(compra.getFechaCompra())
                .cantidad(compra.getCantidad())
                .total(compra.getTotal())
                .usuarioId(clienteId)
                .usuarioEmail(clienteEmail)
                .clienteId(clienteId)
                .clienteEmail(clienteEmail)
                .eventoId(compra.getEvento() == null ? null : compra.getEvento().getIdEvento())
                .eventoTitulo(compra.getEvento() == null ? null : compra.getEvento().getTituloEvento())
                .entradas(entradas)
                .build();
    }
}
