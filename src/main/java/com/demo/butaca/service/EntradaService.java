package com.demo.butaca.service;

import com.demo.butaca.dto.EntradaResponseDTO;
import com.demo.butaca.model.Entrada;
import com.demo.butaca.repository.EntradaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EntradaService {

    private final EntradaRepository entradaRepository;

    public EntradaService(EntradaRepository entradaRepository) {
        this.entradaRepository = entradaRepository;
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

        if (!"CONFIRMADA".equals(entrada.getEstadoEntrada())) {
            throw new IllegalStateException("La entrada no esta en estado CONFIRMADA");
        }

        entrada.setEstadoEntrada("USADA");
        entrada.setFechaValidacion(LocalDateTime.now());
        return toDTO(entradaRepository.save(entrada));
    }

    @Transactional(readOnly = true)
    public List<EntradaResponseDTO> listarEntradasPorUsuario(Long idUsuario) {
        return entradaRepository.findByUsuarioIdUsuario(idUsuario).stream().map(this::toDTO).toList();
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
                .estadoEntrada(entrada.getEstadoEntrada())
                .fechaCompra(entrada.getFechaCompra())
                .fechaValidacion(entrada.getFechaValidacion())
                .eventoId(entrada.getEvento() == null ? null : entrada.getEvento().getIdEvento())
                .usuarioId(entrada.getUsuario() == null ? null : entrada.getUsuario().getIdUsuario())
                .compraId(entrada.getCompra() == null ? null : entrada.getCompra().getId())
                .build();
    }
}
