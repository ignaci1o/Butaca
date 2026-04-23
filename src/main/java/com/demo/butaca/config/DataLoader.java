package com.demo.butaca.config;

import com.demo.butaca.model.Evento;
import com.demo.butaca.model.RolUsuario;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.EventoRepository;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, EventoRepository eventoRepository) {
        return args -> {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            if (usuarioRepository.count() == 0) {
                Usuario admin = new Usuario();
                admin.setNombreUsuario("Admin");
                admin.setApellidoUsuario("Sistema");
                admin.setEmailUsuario("admin@butaca.com");
                admin.setPasswordUsuario(encoder.encode("admin123"));
                admin.setRolUsuario(RolUsuario.ADMIN);
                usuarioRepository.save(admin);

                Usuario org = new Usuario();
                org.setNombreUsuario("Productora");
                org.setApellidoUsuario("Eventos S.A.");
                org.setEmailUsuario("org@butaca.com");
                org.setPasswordUsuario(encoder.encode("org123"));
                org.setRolUsuario(RolUsuario.ORGANIZADOR);
                org = usuarioRepository.save(org);

                Usuario cliente = new Usuario();
                cliente.setNombreUsuario("Juan");
                cliente.setApellidoUsuario("Perez");
                cliente.setEmailUsuario("cliente@butaca.com");
                cliente.setPasswordUsuario(encoder.encode("cliente123"));
                cliente.setRolUsuario(RolUsuario.CLIENTE);
                usuarioRepository.save(cliente);

                if (eventoRepository.count() == 0) {
                    Evento ev1 = new Evento();
                    ev1.setTituloEvento("Concierto de Rock Alternativo");
                    ev1.setDescripcionEvento("Un concierto increible de bandas indie y rock alternativo.");
                    ev1.setUbicacionEvento("Teatro Vorterix, Buenos Aires");
                    ev1.setDetallesUbicacionEvento("Av. Federico Lacroze 3455");
                    ev1.setFechaEvento(LocalDate.now().plusDays(15));
                    ev1.setHoraEvento(LocalTime.of(21, 0));
                    ev1.setPrecioEntradaEvento(new BigDecimal("15000.00"));
                    ev1.setCuposTotalesEvento(500);
                    ev1.setCuposDisponibleEvento(500);
                    ev1.setEstadoPublicacionEvento("PUBLICADO");
                    ev1.setOrganizador(org);
                    eventoRepository.save(ev1);

                    Evento ev2 = new Evento();
                    ev2.setTituloEvento("Festival de Jazz al Aire Libre");
                    ev2.setDescripcionEvento("Jazz toda la tarde en un parque hermoso.");
                    ev2.setUbicacionEvento("Parque Centenario");
                    ev2.setDetallesUbicacionEvento("Anfiteatro");
                    ev2.setFechaEvento(LocalDate.now().plusDays(30));
                    ev2.setHoraEvento(LocalTime.of(16, 0));
                    ev2.setPrecioEntradaEvento(new BigDecimal("8000.00"));
                    ev2.setCuposTotalesEvento(1000);
                    ev2.setCuposDisponibleEvento(1000);
                    ev2.setEstadoPublicacionEvento("PUBLICADO");
                    ev2.setOrganizador(org);
                    eventoRepository.save(ev2);
                }
            }
        };
    }
}
