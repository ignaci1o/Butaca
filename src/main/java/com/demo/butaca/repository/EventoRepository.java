package com.demo.butaca.repository;

import com.demo.butaca.model.Evento;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EventoRepository {
    private final Map<Long, Evento> eventos = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public Evento save(Evento evento) {
        if (evento.getId() == null) {
            evento.setId(sequence.getAndIncrement());
        }
        eventos.put(evento.getId(), evento);
        return evento;
    }

    public List<Evento> findAll() {
        return new ArrayList<>(eventos.values());
    }

    public Optional<Evento> findById(Long id) {
        return Optional.ofNullable(eventos.get(id));
    }
}
