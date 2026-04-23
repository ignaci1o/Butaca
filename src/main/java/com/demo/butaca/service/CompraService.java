package com.demo.butaca.service;

import com.demo.butaca.dto.CompraRequestDTO;
import com.demo.butaca.dto.EntradaResponseDTO;
import com.demo.butaca.model.Entrada;
import com.demo.butaca.model.Evento;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.EntradaRepository;
import com.demo.butaca.repository.EventoRepository;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CompraService {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EntradaRepository entradaRepository;
    private final EntradaService entradaService;

    public CompraService(EventoRepository eventoRepository, UsuarioRepository usuarioRepository, EntradaRepository entradaRepository, EntradaService entradaService) {
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
        this.entradaRepository = entradaRepository;
        this.entradaService = entradaService;
    }

    @Transactional
    public EntradaResponseDTO comprarEntrada(CompraRequestDTO request) {
        Evento evento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));

        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!"PUBLICADO".equals(evento.getEstadoPublicacionEvento())) {
            throw new IllegalStateException("El evento no esta publicado");
        }

        if (evento.getCuposDisponibleEvento() <= 0) {
            throw new IllegalStateException("Cupo agotado");
        }

        // Descontar cupo
        evento.setCuposDisponibleEvento(evento.getCuposDisponibleEvento() - 1);
        eventoRepository.save(evento);

        // Crear entrada
        Entrada entrada = new Entrada();
        entrada.setCodigoUnicoEntrada(UUID.randomUUID());
        entrada.setEstadoEntrada("CONFIRMADA");
        entrada.setEvento(evento);
        entrada.setUsuario(usuario);

        Entrada guardada = entradaRepository.save(entrada);
        return entradaService.toDTO(guardada);
    }
}
