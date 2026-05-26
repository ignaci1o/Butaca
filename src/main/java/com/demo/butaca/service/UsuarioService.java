package com.demo.butaca.service;

import com.demo.butaca.dto.UsuarioDTO;
import com.demo.butaca.model.EstadoUsuario;
import com.demo.butaca.model.Rol;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.repository.RolRepository;
import com.demo.butaca.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    public UsuarioService(UsuarioRepository usuarioRepository, RolRepository rolRepository) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> listar() {
        return usuarioRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findById(Long id) {
        return toDTO(buscarUsuario(id));
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findByEmail(String email) {
        return toDTO(usuarioRepository.findByEmailUsuarioIgnoreCase(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado")));
    }

    @Transactional
    public UsuarioDTO actualizar(Long id, UsuarioDTO dto) {
        Usuario usuario = buscarUsuario(id);
        usuario.setNombreUsuario(dto.getNombreUsuario());
        usuario.setApellidoUsuario(dto.getApellidoUsuario());
        usuario.setEmailUsuario(dto.getEmailUsuario().trim().toLowerCase());
        usuario.setTelefono(dto.getTelefono());
        if (dto.getEstadoUsuario() != null) {
            usuario.setEstadoUsuario(dto.getEstadoUsuario().name());
        }
        if (dto.getRolId() != null) {
            usuario.setRol(buscarRol(dto.getRolId()));
        } else if (dto.getRolUsuario() != null && !dto.getRolUsuario().isBlank()) {
            usuario.setRol(buscarRol(dto.getRolUsuario()));
        }
        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioDTO changeEstado(Long id, EstadoUsuario estadoUsuario) {
        Usuario usuario = buscarUsuario(id);
        usuario.setEstadoUsuario(estadoUsuario.name());
        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioDTO assignRole(Long id, Long rolId) {
        Usuario usuario = buscarUsuario(id);
        usuario.setRol(buscarRol(rolId));
        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public UsuarioDTO assignRole(Long id, String rolNombre) {
        Usuario usuario = buscarUsuario(id);
        usuario.setRol(buscarRol(rolNombre));
        return toDTO(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    private Usuario buscarUsuario(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    private Rol buscarRol(Long id) {
        return rolRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
    }

    private Rol buscarRol(String nombre) {
        String normalizado = nombre.trim().toUpperCase();
        if ("CLIENT".equals(normalizado)) {
            normalizado = "CLIENTE";
        }
        return rolRepository.findByNombre(normalizado)
                .orElseThrow(() -> new IllegalArgumentException("Rol no encontrado"));
    }

    public UsuarioDTO toDTO(Usuario usuario) {
        return UsuarioDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombreUsuario(usuario.getNombreUsuario())
                .apellidoUsuario(usuario.getApellidoUsuario())
                .emailUsuario(usuario.getEmailUsuario())
                .telefono(usuario.getTelefono())
                .estadoUsuario(usuario.getEstadoUsuario())
                .fechaRegistro(usuario.getCreatedAt())
                .createdAt(usuario.getCreatedAt())
                .updatedAt(usuario.getUpdatedAt())
                .rolId(usuario.getRol() == null ? null : usuario.getRol().getId())
                .rolUsuario(usuario.getRol() == null ? null : usuario.getRol().getNombre())
                .build();
    }
}
