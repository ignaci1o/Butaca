package com.demo.butaca.service;

import com.demo.butaca.dto.CompraRequestDTO;
import com.demo.butaca.dto.CompraResponseDTO;
import com.demo.butaca.dto.EntradaResponseDTO;
import com.demo.butaca.dto.NotificacionDTO;
import com.demo.butaca.model.Compra;
import com.demo.butaca.model.Entrada;
import com.demo.butaca.model.EstadoCompra;
import com.demo.butaca.model.EstadoEntrada;
import com.demo.butaca.model.EstadoEvento;
import com.demo.butaca.model.Evento;
import com.demo.butaca.model.HistorialEstadoEntrada;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.CompraRepository;
import com.demo.butaca.repository.EntradaRepository;
import com.demo.butaca.repository.EventoRepository;
import com.demo.butaca.repository.HistorialEstadoEntradaRepository;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

@Service
public class CompraService {

    private static final EnumSet<EstadoEntrada> ESTADOS_QUE_OCUPAN_CUPO =
            EnumSet.of(EstadoEntrada.CONFIRMADA, EstadoEntrada.USADA);

    private final CompraRepository compraRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EntradaRepository entradaRepository;
    private final HistorialEstadoEntradaRepository historialRepository;
    private final EntradaService entradaService;
    private final NotificacionService notificacionService;

    public CompraService(CompraRepository compraRepository, EventoRepository eventoRepository,
                         UsuarioRepository usuarioRepository, EntradaRepository entradaRepository,
                         HistorialEstadoEntradaRepository historialRepository, EntradaService entradaService,
                         NotificacionService notificacionService) {
        this.compraRepository = compraRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.entradaRepository = entradaRepository;
        this.historialRepository = historialRepository;
        this.entradaService = entradaService;
        this.notificacionService = notificacionService;
    }

    @Transactional
    public CompraResponseDTO comprarEntrada(CompraRequestDTO request) {
        int cantidad = request.getCantidad() == null ? 1 : request.getCantidad();
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }

        Evento evento = eventoRepository.findByIdParaCompra(request.getEventoId())
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));
        if (!EstadoEvento.PUBLICADO.equals(evento.getEstadoPublicacionEvento())) {
            throw new IllegalStateException("El evento no esta publicado");
        }

        long ocupadas = entradaRepository.countByEventoIdEventoAndEstadoEntradaIn(
                evento.getIdEvento(), ESTADOS_QUE_OCUPAN_CUPO);
        if (evento.getCuposTotalesEvento() - ocupadas < cantidad) {
            throw new IllegalStateException("Cupos insuficientes");
        }

        Usuario cliente = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        LocalDateTime fechaCompra = LocalDateTime.now();

        Compra compra = Compra.builder()
                .fechaCompra(fechaCompra)
                .estadoCompra(EstadoCompra.CONFIRMADA)
                .cliente(cliente)
                .build();
        compra = compraRepository.save(compra);

        List<Entrada> entradas = new ArrayList<>();
        for (int i = 0; i < cantidad; i++) {
            entradas.add(Entrada.builder()
                    .codigoUnicoEntrada(UUID.randomUUID())
                    .estadoEntrada(EstadoEntrada.CONFIRMADA)
                    .precioPagado(evento.getPrecioEntradaEvento())
                    .fechaEmision(fechaCompra)
                    .evento(evento)
                    .cliente(cliente)
                    .compra(compra)
                    .build());
        }
        entradas = entradaRepository.saveAll(entradas);

        List<HistorialEstadoEntrada> historial = entradas.stream()
                .map(entrada -> HistorialEstadoEntrada.builder()
                        .entrada(entrada)
                        .estadoNuevoEntrada(EstadoEntrada.CONFIRMADA)
                        .usuarioResponsable(cliente)
                        .motivoCambioEntrada("Entrada emitida por compra")
                        .fechaCambioEntrada(fechaCompra)
                        .build())
                .toList();
        historialRepository.saveAll(historial);

        compra.setEntradas(entradas);
        registrarNotificacionCompra(compra, evento);
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
        return compraRepository.findByOrganizadorIdOrderByFechaCompraDesc(organizadorId)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    private void registrarNotificacionCompra(Compra compra, Evento evento) {
        notificacionService.crearNotificacion(NotificacionDTO.builder()
                .usuarioId(compra.getCliente().getIdUsuario())
                .eventoId(evento.getIdEvento())
                .mensaje("Compra confirmada para " + evento.getTituloEvento())
                .tipo("COMPRA")
                .fechaEnvio(LocalDateTime.now())
                .build());
    }

    public CompraResponseDTO toDTO(Compra compra) {
        List<Entrada> entidades = compra.getEntradas() == null ? List.of() : compra.getEntradas();
        List<EntradaResponseDTO> entradas = entidades.stream().map(entradaService::toDTO).toList();
        BigDecimal total = entidades.stream()
                .map(Entrada::getPrecioPagado)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Evento evento = entidades.stream().map(Entrada::getEvento).findFirst().orElse(null);

        return CompraResponseDTO.builder()
                .id(compra.getId())
                .fechaCompra(compra.getFechaCompra())
                .cantidad(entradas.size())
                .total(total)
                .clienteId(compra.getCliente() == null ? null : compra.getCliente().getIdUsuario())
                .clienteEmail(compra.getCliente() == null ? null : compra.getCliente().getEmailUsuario())
                .eventoId(evento == null ? null : evento.getIdEvento())
                .eventoTitulo(evento == null ? null : evento.getTituloEvento())
                .estadoCompra(compra.getEstadoCompra().name())
                .entradas(entradas)
                .build();
    }
}
