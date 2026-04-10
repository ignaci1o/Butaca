package com.demo.butaca.service;

import com.demo.butaca.dto.CrearEventoRequest;
import com.demo.butaca.model.Entrada;
import com.demo.butaca.model.Evento;
import com.demo.butaca.repository.EntradaRepository;
import com.demo.butaca.repository.EventoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TicketService {
    private final EventoRepository eventoRepository;
    private final EntradaRepository entradaRepository;

    public TicketService(EventoRepository eventoRepository, EntradaRepository entradaRepository) {
        this.eventoRepository = eventoRepository;
        this.entradaRepository = entradaRepository;
    }

    public Evento crearEvento(CrearEventoRequest request) {
        if (request.getCapacidad() <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a 0");
        }

        Evento evento = new Evento();
        evento.setNombre(request.getNombre());
        evento.setFecha(request.getFecha());
        evento.setCapacidad(request.getCapacidad());
        evento.setEntradasDisponibles(request.getCapacidad());

        return eventoRepository.save(evento);
    }

    public List<Evento> listarEventos() {
        return eventoRepository.findAll();
    }

    public Entrada comprarEntrada(Long eventoId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new IllegalArgumentException("Evento no encontrado"));

        if (evento.getEntradasDisponibles() <= 0) {
            throw new IllegalStateException("No hay entradas disponibles");
        }

        evento.setEntradasDisponibles(evento.getEntradasDisponibles() - 1);
        eventoRepository.save(evento);

        Entrada entrada = new Entrada();
        entrada.setEventoId(eventoId);
        entrada.setCodigoUnico(UUID.randomUUID().toString());
        entrada.setUsada(false);

        return entradaRepository.save(entrada);
    }

    public String validarEntrada(String codigoUnico) {
        Entrada entrada = entradaRepository.findByCodigoUnico(codigoUnico).orElse(null);

        if (entrada == null) {
            return "Entrada invalida: codigo no existe";
        }

        if (entrada.isUsada()) {
            return "Entrada invalida: ya fue usada";
        }

        entrada.setUsada(true);
        entradaRepository.save(entrada);
        return "Entrada valida";
    }
}
