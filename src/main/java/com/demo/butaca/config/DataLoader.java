package com.demo.butaca.config;

import com.demo.butaca.model.Categoria;
import com.demo.butaca.model.EstadoUsuario;
import com.demo.butaca.model.Evento;
import com.demo.butaca.model.Rol;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.CategoriaRepository;
import com.demo.butaca.repository.EventoRepository;
import com.demo.butaca.repository.RolRepository;
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
                                   RolRepository rolRepository, CategoriaRepository categoriaRepository) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            Rol adminRol = asegurarRol(rolRepository, "ADMIN");
            Rol clientRol = asegurarRol(rolRepository, "CLIENT");
            Rol organizadorRol = asegurarRol(rolRepository, "ORGANIZADOR");

            List<Categoria> categorias = List.of(
                    asegurarCategoria(categoriaRepository, "Concierto", "Eventos musicales y recitales"),
                    asegurarCategoria(categoriaRepository, "Teatro", "Obras teatrales y artes escenicas"),
                    asegurarCategoria(categoriaRepository, "Deporte", "Competencias y eventos deportivos"),
                    asegurarCategoria(categoriaRepository, "Festival", "Festivales y experiencias culturales")
            );

            if (!usuarioRepository.existsByEmailUsuarioIgnoreCase("admin@butaca.com")) {
                Usuario admin = new Usuario();
                admin.setNombreUsuario("Admin");
                admin.setApellidoUsuario("Sistema");
                admin.setEmailUsuario("admin@butaca.com");
                admin.setPasswordUsuario(encoder.encode("admin123"));
                admin.setRol(adminRol);
                admin.setEstadoUsuario(EstadoUsuario.ACTIVO.name());
                usuarioRepository.save(admin);
            }

            Usuario org = usuarioRepository.findByEmailUsuarioIgnoreCase("org@butaca.com").orElseGet(() -> {
                Usuario usuario = new Usuario();
                usuario.setNombreUsuario("Productora");
                usuario.setApellidoUsuario("Eventos S.A.");
                usuario.setEmailUsuario("org@butaca.com");
                usuario.setPasswordUsuario(encoder.encode("org123"));
                usuario.setRol(organizadorRol);
                usuario.setEstadoUsuario(EstadoUsuario.ACTIVO.name());
                return usuarioRepository.save(usuario);
            });

            if (!usuarioRepository.existsByEmailUsuarioIgnoreCase("cliente@butaca.com")) {
                Usuario cliente = new Usuario();
                cliente.setNombreUsuario("Juan");
                cliente.setApellidoUsuario("Perez");
                cliente.setEmailUsuario("cliente@butaca.com");
                cliente.setPasswordUsuario(encoder.encode("cliente123"));
                cliente.setRol(clientRol);
                cliente.setEstadoUsuario(EstadoUsuario.ACTIVO.name());
                usuarioRepository.save(cliente);
            }

            if (eventoRepository.count() == 0) {
                crearEventoDemo(eventoRepository, org, categorias.get(0), "Concierto de Rock Alternativo",
                        "Un concierto increible de bandas indie y rock alternativo.",
                        "Teatro Vorterix, Buenos Aires", "Av. Federico Lacroze 3455",
                        LocalDate.now().plusDays(15), LocalTime.of(21, 0), new BigDecimal("15000.00"), 500);

                crearEventoDemo(eventoRepository, org, categorias.get(3), "Festival de Jazz al Aire Libre",
                        "Jazz toda la tarde en un parque hermoso.",
                        "Parque Centenario", "Anfiteatro",
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

    private void crearEventoDemo(EventoRepository eventoRepository, Usuario organizador, Categoria categoria, String titulo,
                                 String descripcion, String ubicacion, String detallesUbicacion, LocalDate fecha,
                                 LocalTime hora, BigDecimal precio, int cupos) {
        Evento evento = new Evento();
        evento.setTituloEvento(titulo);
        evento.setDescripcionEvento(descripcion);
        evento.setUbicacionEvento(ubicacion);
        evento.setDetallesUbicacionEvento(detallesUbicacion);
        evento.setFechaEvento(fecha);
        evento.setHoraEvento(hora);
        evento.setPrecioEntradaEvento(precio);
        evento.setCuposTotalesEvento(cupos);
        evento.setCuposDisponibleEvento(cupos);
        evento.setEstadoPublicacionEvento("PUBLICADO");
        evento.setOrganizador(organizador);
        evento.setCategoria(categoria);
        eventoRepository.save(evento);
    }
}
