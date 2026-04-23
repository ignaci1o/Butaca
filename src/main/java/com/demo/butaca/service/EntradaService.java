package com.demo.butaca.service;

import com.demo.butaca.dto.EntradaResponseDTO;
import com.demo.butaca.model.Entrada;
import com.demo.butaca.repository.EntradaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        return toDTO(entradaRepository.save(entrada));
    }

    @Transactional(readOnly = true)
    public List<EntradaResponseDTO> listarEntradasPorUsuario(Long idUsuario) {
        return entradaRepository.findByUsuarioIdUsuario(idUsuario)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EntradaResponseDTO> listarEntradasPorEvento(Long idEvento) {
        return entradaRepository.findByEventoIdEvento(idEvento)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    public EntradaResponseDTO toDTO(Entrada entrada) {
        EntradaResponseDTO dto = new EntradaResponseDTO();
        dto.setIdEntrada(entrada.getIdEntrada());
        dto.setCodigoUnicoEntrada(entrada.getCodigoUnicoEntrada());
        dto.setEstadoEntrada(entrada.getEstadoEntrada());
        dto.setEventoId(entrada.getEvento().getIdEvento());
        dto.setUsuarioId(entrada.getUsuario().getIdUsuario());
        return dto;
    }
}
