package com.demo.butaca.service;

import com.demo.butaca.dto.AuthResponse;
import com.demo.butaca.dto.LoginRequest;
import com.demo.butaca.dto.RegistroUsuarioRequest;
import com.demo.butaca.model.EstadoUsuario;
import com.demo.butaca.model.Rol;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.RolRepository;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Transactional
    public AuthResponse registrar(RegistroUsuarioRequest request) {
        String emailNormalizado = request.getEmailUsuario().trim().toLowerCase();

        if (usuarioRepository.existsByEmailUsuarioIgnoreCase(emailNormalizado)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        String rolNombre = normalizarRol(request.getRolUsuario());
        Rol rol = rolRepository.findByNombre(rolNombre)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado: " + rolNombre));

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getNombreUsuario().trim());
        usuario.setApellidoUsuario(request.getApellidoUsuario().trim());
        usuario.setEmailUsuario(emailNormalizado);
        usuario.setPasswordUsuario(passwordEncoder.encode(request.getPasswordUsuario()));
        usuario.setRol(rol);
        usuario.setTelefono(request.getTelefono());
        usuario.setEstadoUsuario((request.getEstadoUsuario() == null ? EstadoUsuario.ACTIVO : request.getEstadoUsuario()).name());

        Usuario guardado = usuarioRepository.save(usuario);
        return toResponse(guardado, "Usuario registrado correctamente");
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String emailNormalizado = request.getEmailUsuario().trim().toLowerCase();
        Usuario usuario = usuarioRepository.findByEmailUsuarioIgnoreCase(emailNormalizado)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales invalidas"));

        if (!passwordEncoder.matches(request.getPasswordUsuario(), usuario.getPasswordUsuario())) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        return toResponse(usuario, "Inicio de sesion correcto");
    }

    private String normalizarRol(String rolUsuario) {
        if (rolUsuario == null || rolUsuario.isBlank()) {
            return "CLIENTE";
        }
        String normalizado = rolUsuario.trim().toUpperCase();
        return "CLIENT".equals(normalizado) ? "CLIENTE" : normalizado;
    }

    private AuthResponse toResponse(Usuario usuario, String mensaje) {
        return AuthResponse.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombreUsuario(usuario.getNombreUsuario())
                .apellidoUsuario(usuario.getApellidoUsuario())
                .emailUsuario(usuario.getEmailUsuario())
                .rolUsuario(usuario.getRol() == null ? null : usuario.getRol().getNombre())
                .telefono(usuario.getTelefono())
                .estadoUsuario(usuario.getEstadoUsuario())
                .mensaje(mensaje)
                .build();
    }
}
