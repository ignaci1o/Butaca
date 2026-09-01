package com.demo.butaca;

import com.demo.butaca.dto.CompraRequestDTO;
import com.demo.butaca.dto.CompraResponseDTO;
import com.demo.butaca.dto.EventoResponseDTO;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.HistorialEstadoEntradaRepository;
import com.demo.butaca.repository.UsuarioRepository;
import com.demo.butaca.service.CompraService;
import com.demo.butaca.service.EventoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("demo")
class ButacaApplicationTests {

    @Autowired
    private EventoService eventoService;

    @Autowired
    private CompraService compraService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private HistorialEstadoEntradaRepository historialRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void catalogoPublicoSoloMuestraEventosPublicadosConCupo() {
        List<EventoResponseDTO> eventos = eventoService.listarEventosPublicados();

        assertFalse(eventos.isEmpty());
        assertTrue(eventos.stream().allMatch(evento -> "PUBLICADO".equals(evento.getEstadoPublicacionEvento())));
        assertTrue(eventos.stream().allMatch(evento -> evento.getCuposDisponibles() > 0));
    }

    @Test
    void comprarCalculaStockYEmiteTicketsConPrecioHistorico() {
        EventoResponseDTO publicado = eventoService.listarEventosPublicados().get(0);
        Usuario cliente = usuarioRepository.findByEmailUsuarioIgnoreCase("cliente@butaca.com").orElseThrow();
        int stockAntes = publicado.getCuposDisponibles();
        BigDecimal precioAlComprar = publicado.getPrecioEntradaEvento();

        CompraRequestDTO request = new CompraRequestDTO();
        request.setEventoId(publicado.getIdEvento());
        request.setUsuarioId(cliente.getIdUsuario());
        request.setCantidad(2);

        CompraResponseDTO compra = compraService.comprarEntrada(request);
        EventoResponseDTO eventoDespues = eventoService.obtenerEvento(publicado.getIdEvento());

        assertEquals(stockAntes - 2, eventoDespues.getCuposDisponibles());
        assertTrue(eventoDespues.getVersion() > publicado.getVersion());
        assertEquals(2, compra.getCantidad());
        assertEquals(precioAlComprar.multiply(BigDecimal.valueOf(2)), compra.getTotal());
        assertEquals(2, compra.getEntradas().size());
        assertTrue(compra.getEntradas().stream().allMatch(entrada -> entrada.getCodigoUnicoEntrada() != null));
        assertTrue(compra.getEntradas().stream()
                .allMatch(entrada -> precioAlComprar.compareTo(entrada.getPrecioPagado()) == 0));
        assertTrue(compra.getEntradas().stream().allMatch(entrada ->
                historialRepository.findByEntradaIdEntradaOrderByFechaCambioEntradaAsc(entrada.getIdEntrada()).size() == 1));
        assertNotNull(compra.getId());
    }

}
