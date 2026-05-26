package com.demo.butaca.config;

import com.demo.butaca.model.Categoria;
import com.demo.butaca.model.EstadoEvento;
import com.demo.butaca.model.EstadoUsuario;
import com.demo.butaca.model.Evento;
import com.demo.butaca.model.Rol;
import com.demo.butaca.model.Ubicacion;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.CategoriaRepository;
import com.demo.butaca.repository.EventoRepository;
import com.demo.butaca.repository.RolRepository;
import com.demo.butaca.repository.UbicacionRepository;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, EventoRepository eventoRepository,
                                   RolRepository rolRepository, CategoriaRepository categoriaRepository,
                                   UbicacionRepository ubicacionRepository) {
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
                    asegurarUbicacion(ubicacionRepository, "Parque Centenario", "Anfiteatro Eva Peron, Buenos Aires", 1000)
            );

            asegurarUsuario(usuarioRepository, encoder, adminRol, "Admin", "Sistema", "admin@butaca.com", "admin123", "3810000001");
            Usuario org = asegurarUsuario(usuarioRepository, encoder, organizadorRol, "Productora", "Eventos S.A.",
                    "org@butaca.com", "org123", "3810000002");
            asegurarUsuario(usuarioRepository, encoder, clienteRol, "Juan", "Perez", "cliente@butaca.com", "cliente123", "3810000003");

            if (eventoRepository.count() == 0) {
                crearEventoDemo(eventoRepository, org, categorias.get(0), ubicaciones.get(0), "Concierto de Rock Alternativo",
                        "Un concierto increible de bandas indie y rock alternativo.",
                        LocalDate.now().plusDays(15), LocalTime.of(21, 0), new BigDecimal("15000.00"), 500);

                crearEventoDemo(eventoRepository, org, categorias.get(3), ubicaciones.get(1), "Festival de Jazz al Aire Libre",
                        "Jazz toda la tarde en un parque hermoso.",
                        LocalDate.now().plusDays(30), LocalTime.of(16, 0), new BigDecimal("8000.00"), 1000);
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

    private void crearEventoDemo(EventoRepository eventoRepository, Usuario organizador, Categoria categoria, Ubicacion ubicacion,
                                 String titulo, String descripcion, LocalDate fecha, LocalTime hora,
                                 BigDecimal precio, int cupos) {
        Evento evento = new Evento();
        evento.setTituloEvento(titulo);
        evento.setDescripcionEvento(descripcion);
        evento.setFechaEvento(fecha);
        evento.setHoraEvento(hora);
        evento.setPrecioEntradaEvento(precio);
        evento.setCuposTotalesEvento(cupos);
        evento.setCuposDisponibleEvento(cupos);
        evento.setEstadoPublicacionEvento(EstadoEvento.PUBLICADO);
        evento.setOrganizador(organizador);
        evento.setCategoria(categoria);
        evento.setUbicacion(ubicacion);
        eventoRepository.save(evento);
    }
}
