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
        String emailNormalizado = request.getEmailUsuario().trim().toLowerCase();

        if (usuarioRepository.existsByEmailUsuarioIgnoreCase(emailNormalizado)) {
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getNombreUsuario().trim());
        usuario.setApellidoUsuario(request.getApellidoUsuario().trim());
        usuario.setEmailUsuario(emailNormalizado);
        usuario.setPasswordUsuario(passwordEncoder.encode(request.getPasswordUsuario()));
        usuario.setRolUsuario(request.getRolUsuario());

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

    private AuthResponse toResponse(Usuario usuario, String mensaje) {
        return new AuthResponse(
                usuario.getIdUsuario(),
                usuario.getNombreUsuario(),
                usuario.getApellidoUsuario(),
                usuario.getEmailUsuario(),
                usuario.getRolUsuario(),
                mensaje
        );
    }
}
