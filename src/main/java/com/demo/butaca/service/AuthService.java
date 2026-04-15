package com.demo.butaca.service;

import com.demo.butaca.dto.AuthResponse;
import com.demo.butaca.dto.LoginRequest;
import com.demo.butaca.dto.RegistroUsuarioRequest;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public AuthResponse registrar(RegistroUsuarioRequest request) {
        String emailNormalizado = request.getEmail().trim().toLowerCase();

        if (usuarioRepository.existsByEmailIgnoreCase(emailNormalizado)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(request.getNombre().trim());
        usuario.setApellido(request.getApellido().trim());
        usuario.setEmail(emailNormalizado);
        usuario.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        usuario.setRol(request.getRol());
        usuario.setActivo(true);

        Usuario guardado = usuarioRepository.save(usuario);
        return toResponse(guardado, "Usuario registrado correctamente");
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        String emailNormalizado = request.getEmail().trim().toLowerCase();
        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(emailNormalizado)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales invalidas"));

        if (!usuario.isActivo()) {
            throw new IllegalStateException("La cuenta se encuentra desactivada");
        }

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales invalidas");
        }

        return toResponse(usuario, "Inicio de sesion correcto");
    }

    private AuthResponse toResponse(Usuario usuario, String mensaje) {
        return new AuthResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                usuario.getRol(),
                mensaje
        );
    }
}
