package com.demo.butaca.config;

import com.demo.butaca.model.Categoria;
import com.demo.butaca.model.Compra;
import com.demo.butaca.model.Entrada;
import com.demo.butaca.model.EstadoCompra;
import com.demo.butaca.model.EstadoEntrada;
import com.demo.butaca.model.EstadoEvento;
import com.demo.butaca.model.EstadoUsuario;
import com.demo.butaca.model.Evento;
import com.demo.butaca.model.HistorialEstadoEntrada;
import com.demo.butaca.model.Notificacion;
import com.demo.butaca.model.Rol;
import com.demo.butaca.model.Ubicacion;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.CategoriaRepository;
import com.demo.butaca.repository.CompraRepository;
import com.demo.butaca.repository.EntradaRepository;
import com.demo.butaca.repository.EventoRepository;
import com.demo.butaca.repository.HistorialEstadoEntradaRepository;
import com.demo.butaca.repository.NotificacionRepository;
import com.demo.butaca.repository.RolRepository;
import com.demo.butaca.repository.UbicacionRepository;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, EventoRepository eventoRepository,
                                   RolRepository rolRepository, CategoriaRepository categoriaRepository,
                                   UbicacionRepository ubicacionRepository, CompraRepository compraRepository,
                                   EntradaRepository entradaRepository,
                                   HistorialEstadoEntradaRepository historialRepository,
                                   NotificacionRepository notificacionRepository) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            Rol adminRol = asegurarRol(rolRepository, "ADMIN");
            Rol clienteRol = asegurarRol(rolRepository, "CLIENTE");
            Rol organizadorRol = asegurarRol(rolRepository, "ORGANIZADOR");

            List<Categoria> categorias = List.of(
                    asegurarCategoria(categoriaRepository, "Conciertos", "Eventos musicales y recitales"),
                    asegurarCategoria(categoriaRepository, "Teatro", "Obras teatrales y artes escenicas"),
                    asegurarCategoria(categoriaRepository, "Deportes", "Competencias y eventos deportivos"),
                    asegurarCategoria(categoriaRepository, "Festivales", "Festivales y experiencias culturales")
            );

            List<Ubicacion> ubicaciones = List.of(
                    asegurarUbicacion(ubicacionRepository, "Teatro Vorterix", "Av. Federico Lacroze 3455, Buenos Aires", 500),
                    asegurarUbicacion(ubicacionRepository, "Parque Centenario", "Anfiteatro Eva Peron, Buenos Aires", 1000),
                    asegurarUbicacion(ubicacionRepository, "Teatro San Martin", "Av. Corrientes 1530, Buenos Aires", 350),
                    asegurarUbicacion(ubicacionRepository, "Microestadio Malvinas", "Gutemberg 350, Buenos Aires", 900),
                    asegurarUbicacion(ubicacionRepository, "Centro Cultural Recoleta", "Junin 1930, Buenos Aires", 700)
            );

            Usuario admin = asegurarUsuario(usuarioRepository, encoder, adminRol, "Admin", "Sistema",
                    "admin@butaca.com", "admin123", "3810000001");
            Usuario org = asegurarUsuario(usuarioRepository, encoder, organizadorRol, "Productora", "Eventos S.A.",
                    "org@butaca.com", "org123", "3810000002");
            Usuario cliente = asegurarUsuario(usuarioRepository, encoder, clienteRol, "Juan", "Perez",
                    "cliente@butaca.com", "cliente123", "3810000003");
            Usuario ana = asegurarUsuario(usuarioRepository, encoder, clienteRol, "Ana", "Gomez",
                    "ana@butaca.com", "ana123", "3810000004");

            List<Evento> eventos = new ArrayList<>();
            if (eventoRepository.count() == 0) {
                eventos.add(crearEventoDemo(eventoRepository, org, categorias.get(0), ubicaciones.get(0), "Concierto de Rock Alternativo",
                        "Un concierto increible de bandas indie y rock alternativo.",
                        LocalDate.now().plusDays(15), LocalTime.of(21, 0), new BigDecimal("15000.00"), 500,
                        EstadoEvento.PUBLICADO));

                eventos.add(crearEventoDemo(eventoRepository, org, categorias.get(3), ubicaciones.get(1), "Festival de Jazz al Aire Libre",
                        "Jazz toda la tarde en un parque hermoso.",
                        LocalDate.now().plusDays(30), LocalTime.of(16, 0), new BigDecimal("8000.00"), 1000,
                        EstadoEvento.PUBLICADO));

                eventos.add(crearEventoDemo(eventoRepository, org, categorias.get(1), ubicaciones.get(2), "Hamlet en Sala Negra",
                        "Version moderna de un clasico teatral con elenco local.",
                        LocalDate.now().plusDays(8), LocalTime.of(20, 30), new BigDecimal("12000.00"), 180,
                        EstadoEvento.PUBLICADO));

                eventos.add(crearEventoDemo(eventoRepository, org, categorias.get(2), ubicaciones.get(3), "Final Metropolitana de Basquet",
                        "Partido final con ubicaciones generales y acceso digital.",
                        LocalDate.now().plusDays(22), LocalTime.of(19, 0), new BigDecimal("9500.00"), 900,
                        EstadoEvento.PUBLICADO));

                eventos.add(crearEventoDemo(eventoRepository, org, categorias.get(3), ubicaciones.get(4), "Expo Indie Gaming",
                        "Muestra de videojuegos independientes, charlas y experiencias interactivas.",
                        LocalDate.now().plusDays(45), LocalTime.of(14, 0), new BigDecimal("6000.00"), 700,
                        EstadoEvento.PUBLICADO));

                eventos.add(crearEventoDemo(eventoRepository, org, categorias.get(1), ubicaciones.get(2), "Stand Up de Viernes",
                        "Evento cargado como borrador para mostrar publicacion desde el panel organizador.",
                        LocalDate.now().plusDays(12), LocalTime.of(22, 0), new BigDecimal("7000.00"), 120,
                        EstadoEvento.BORRADOR));

                eventos.add(crearEventoDemo(eventoRepository, org, categorias.get(0), ubicaciones.get(0), "Noche Electronica Cancelada",
                        "Evento cancelado para mostrar el ciclo de vida completo.",
                        LocalDate.now().plusDays(18), LocalTime.of(23, 30), new BigDecimal("18000.00"), 500,
                        EstadoEvento.CANCELADO));
            } else {
                eventos.addAll(eventoRepository.findAll());
            }

            if (compraRepository.count() == 0 && eventos.size() >= 4) {
                LocalDateTime ahora = LocalDateTime.now();
                crearCompraDemo(compraRepository, entradaRepository, historialRepository, eventos.get(0), cliente, 2,
                        ahora.minusDays(3).withHour(10).withMinute(15));
                crearCompraDemo(compraRepository, entradaRepository, historialRepository, eventos.get(1), cliente, 1,
                        ahora.minusDays(1).withHour(17).withMinute(40));
                crearCompraDemo(compraRepository, entradaRepository, historialRepository, eventos.get(2), ana, 3,
                        ahora.minusDays(2).withHour(19).withMinute(5));
                crearCompraDemo(compraRepository, entradaRepository, historialRepository, eventos.get(3), ana, 2,
                        ahora.minusHours(5));
            }

            if (notificacionRepository.count() == 0 && !eventos.isEmpty()) {
                LocalDateTime ahora = LocalDateTime.now();
                crearNotificacionDemo(notificacionRepository, cliente, eventos.get(0),
                        "Compra confirmada para Concierto de Rock Alternativo.", "COMPRA", true, ahora.minusDays(3));
                crearNotificacionDemo(notificacionRepository, cliente, eventos.get(1),
                        "Tu entrada para Festival de Jazz al Aire Libre ya esta disponible.", "ENTRADA", false, ahora.minusDays(1));
                crearNotificacionDemo(notificacionRepository, ana, eventos.get(2),
                        "Recordatorio: Hamlet en Sala Negra empieza a las 20:30.", "RECORDATORIO", false, ahora.minusHours(8));
                crearNotificacionDemo(notificacionRepository, admin, null,
                        "Demo inicializada con usuarios, eventos, compras y entradas de prueba.", "SISTEMA", false, ahora.minusMinutes(30));
            }
        };
    }

    private Rol asegurarRol(RolRepository rolRepository, String nombre) {
        return rolRepository.findByNombre(nombre).orElseGet(() -> rolRepository.save(Rol.builder().nombre(nombre).build()));
    }

    private Categoria asegurarCategoria(CategoriaRepository categoriaRepository, String nombre, String descripcion) {
        return categoriaRepository.findByNombreIgnoreCase(nombre)
                .orElseGet(() -> categoriaRepository.save(Categoria.builder().nombre(nombre).descripcion(descripcion).build()));
    }

    private Ubicacion asegurarUbicacion(UbicacionRepository ubicacionRepository, String nombre, String direccion, int capacidad) {
        return ubicacionRepository.findByNombreLugarIgnoreCaseAndDireccionIgnoreCase(nombre, direccion)
                .orElseGet(() -> ubicacionRepository.save(Ubicacion.builder()
                        .nombreLugar(nombre)
                        .direccion(direccion)
                        .capacidadMaxima(capacidad)
                        .activo(true)
                        .build()));
    }

    private Usuario asegurarUsuario(UsuarioRepository usuarioRepository, BCryptPasswordEncoder encoder, Rol rol,
                                    String nombre, String apellido, String email, String password, String telefono) {
        return usuarioRepository.findByEmailUsuarioIgnoreCase(email).orElseGet(() -> {
            Usuario usuario = new Usuario();
            usuario.setNombreUsuario(nombre);
            usuario.setApellidoUsuario(apellido);
            usuario.setEmailUsuario(email);
            usuario.setPasswordUsuario(encoder.encode(password));
            usuario.setRol(rol);
            usuario.setTelefono(telefono);
            usuario.setEstadoUsuario(EstadoUsuario.ACTIVO.name());
            return usuarioRepository.save(usuario);
        });
    }

    private Evento crearEventoDemo(EventoRepository eventoRepository, Usuario organizador, Categoria categoria, Ubicacion ubicacion,
                                   String titulo, String descripcion, LocalDate fecha, LocalTime hora,
                                   BigDecimal precio, int cupos, EstadoEvento estado) {
        Evento evento = new Evento();
        evento.setTituloEvento(titulo);
        evento.setDescripcionEvento(descripcion);
        evento.setFechaEvento(fecha);
        evento.setHoraEvento(hora);
        evento.setPrecioEntradaEvento(precio);
        evento.setCuposTotalesEvento(cupos);
        evento.setEstadoPublicacionEvento(estado);
        evento.setOrganizador(organizador);
        evento.setCategoria(categoria);
        evento.setUbicacion(ubicacion);
        return eventoRepository.save(evento);
    }

    private void crearCompraDemo(CompraRepository compraRepository, EntradaRepository entradaRepository,
                                 HistorialEstadoEntradaRepository historialRepository, Evento evento, Usuario cliente,
                                 int cantidad, LocalDateTime fechaCompra) {
        Compra compra = Compra.builder()
                .fechaCompra(fechaCompra)
                .estadoCompra(EstadoCompra.CONFIRMADA)
                .cliente(cliente)
                .build();
        compra = compraRepository.save(compra);

        for (int i = 0; i < cantidad; i++) {
            Entrada entrada = Entrada.builder()
                    .estadoEntrada(EstadoEntrada.CONFIRMADA)
                    .precioPagado(evento.getPrecioEntradaEvento())
                    .fechaEmision(fechaCompra)
                    .evento(evento)
                    .cliente(cliente)
                    .compra(compra)
                    .build();
            entrada = entradaRepository.save(entrada);
            historialRepository.save(HistorialEstadoEntrada.builder()
                    .entrada(entrada)
                    .estadoNuevoEntrada(EstadoEntrada.CONFIRMADA)
                    .usuarioResponsable(cliente)
                    .motivoCambioEntrada("Entrada de demostracion")
                    .fechaCambioEntrada(fechaCompra)
                    .build());
        }
    }

    private void crearNotificacionDemo(NotificacionRepository notificacionRepository, Usuario usuario, Evento evento,
                                       String mensaje, String tipo, boolean leida, LocalDateTime fecha) {
        Notificacion notificacion = Notificacion.builder()
                .usuario(usuario)
                .evento(evento)
                .mensaje(mensaje)
                .tipo(tipo)
                .leida(leida)
                .fechaCreacion(fecha)
                .fechaEnvio(fecha)
                .build();
        notificacionRepository.save(notificacion);
    }
}
