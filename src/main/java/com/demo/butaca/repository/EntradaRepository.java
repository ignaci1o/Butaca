package com.demo.butaca.repository;

import com.demo.butaca.model.Entrada;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EntradaRepository {
    private final Map<Long, Entrada> entradasById = new HashMap<>();
    private final Map<String, Entrada> entradasByCodigo = new HashMap<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public Entrada save(Entrada entrada) {
        if (entrada.getId() == null) {
            entrada.setId(sequence.getAndIncrement());
        }
        entradasById.put(entrada.getId(), entrada);
        entradasByCodigo.put(entrada.getCodigoUnico(), entrada);
        return entrada;
    }

    public Optional<Entrada> findByCodigoUnico(String codigoUnico) {
        return Optional.ofNullable(entradasByCodigo.get(codigoUnico));
    }
}
