package com.demo.butaca.service;

import com.demo.butaca.dto.EntradaResponseDTO;
import com.demo.butaca.model.Entrada;
import com.demo.butaca.model.EstadoEntrada;
import com.demo.butaca.model.HistorialEstadoEntrada;
import com.demo.butaca.repository.EntradaRepository;
import com.demo.butaca.repository.HistorialEstadoEntradaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EntradaService {

    private final EntradaRepository entradaRepository;
    private final HistorialEstadoEntradaRepository historialRepository;

    public EntradaService(EntradaRepository entradaRepository,
                           HistorialEstadoEntradaRepository historialRepository) {
        this.entradaRepository = entradaRepository;
        this.historialRepository = historialRepository;
    }

    @Transactional(readOnly = true)
    public EntradaResponseDTO validarEntrada(UUID codigoUnico) {
        Entrada entrada = entradaRepository.findByCodigoUnicoEntrada(codigoUnico)
                .orElseThrow(() -> new IllegalArgumentException("Codigo de entrada invalido"));
        return toDTO(entrada);
    }

    @Transactional
    public EntradaResponseDTO usarEntrada(Long id) {
        Entrada entrada = entradaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entrada no encontrada"));

        if (!EstadoEntrada.CONFIRMADA.equals(entrada.getEstadoEntrada())) {
            throw new IllegalStateException("La entrada no esta en estado CONFIRMADA");
        }

        LocalDateTime fechaValidacion = LocalDateTime.now();
        entrada.setEstadoEntrada(EstadoEntrada.USADA);
        entrada.setFechaValidacion(fechaValidacion);
        entrada = entradaRepository.save(entrada);

        historialRepository.save(HistorialEstadoEntrada.builder()
                .entrada(entrada)
                .estadoAnteriorEntrada(EstadoEntrada.CONFIRMADA)
                .estadoNuevoEntrada(EstadoEntrada.USADA)
                .motivoCambioEntrada("Validacion de acceso")
                .fechaCambioEntrada(fechaValidacion)
                .build());
        return toDTO(entrada);
    }

    @Transactional(readOnly = true)
    public List<EntradaResponseDTO> listarEntradasPorUsuario(Long idUsuario) {
        return entradaRepository.findByClienteIdUsuario(idUsuario).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<EntradaResponseDTO> listarEntradasPorEvento(Long idEvento) {
        return entradaRepository.findByEventoIdEvento(idEvento).stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public List<EntradaResponseDTO> listarEntradasPorCompra(Long idCompra) {
        return entradaRepository.findByCompraId(idCompra).stream().map(this::toDTO).toList();
    }

    public EntradaResponseDTO toDTO(Entrada entrada) {
        return EntradaResponseDTO.builder()
                .idEntrada(entrada.getIdEntrada())
                .codigoUnicoEntrada(entrada.getCodigoUnicoEntrada())
                .estadoEntrada(entrada.getEstadoEntrada().name())
                .precioPagado(entrada.getPrecioPagado())
                .fechaEmision(entrada.getFechaEmision())
                .fechaValidacion(entrada.getFechaValidacion())
                .eventoId(entrada.getEvento() == null ? null : entrada.getEvento().getIdEvento())
                .usuarioId(entrada.getCliente() == null ? null : entrada.getCliente().getIdUsuario())
                .compraId(entrada.getCompra() == null ? null : entrada.getCompra().getId())
                .build();
    }
}
