package com.demo.butaca.service;

import com.demo.butaca.dto.UsuarioDTO;
import com.demo.butaca.model.EstadoUsuario;
import com.demo.butaca.model.EstadoVerificacion;
import com.demo.butaca.model.Rol;
import com.demo.butaca.model.Usuario;
import com.demo.butaca.model.VerificacionIdentidad;
import com.demo.butaca.repository.RolRepository;
import com.demo.butaca.repository.UsuarioRepository;
import com.demo.butaca.repository.VerificacionIdentidadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final VerificacionIdentidadRepository verificacionRepository;
    private final AuditoriaSeguridadService auditoriaService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          RolRepository rolRepository,
                          VerificacionIdentidadRepository verificacionRepository,
                          AuditoriaSeguridadService auditoriaService) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.verificacionRepository = verificacionRepository;
        this.auditoriaService = auditoriaService;
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
        if (dto.getDniUsuario() != null && !dto.getDniUsuario().isBlank()) {
            usuario.setDniUsuario(dto.getDniUsuario().trim());
        }
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
        Rol rol = buscarRol(rolId);
        usuario.setRol(rol);
        Usuario guardado = usuarioRepository.save(usuario);
        auditoriaService.registrarEvento(
                guardado,
                "CAMBIO_ROL_USUARIO",
                "USUARIO",
                guardado.getIdUsuario(),
                "EXITOSO",
                "Rol asignado: " + rol.getNombre() + " para " + guardado.getEmailUsuario()
        );
        return toDTO(guardado);
    }

    @Transactional
    public UsuarioDTO assignRole(Long id, String rolNombre) {
        Usuario usuario = buscarUsuario(id);
        Rol rol = buscarRol(rolNombre);
        usuario.setRol(rol);
        Usuario guardado = usuarioRepository.save(usuario);
        auditoriaService.registrarEvento(
                guardado,
                "CAMBIO_ROL_USUARIO",
                "USUARIO",
                guardado.getIdUsuario(),
                "EXITOSO",
                "Rol asignado: " + rol.getNombre() + " para " + guardado.getEmailUsuario()
        );
        return toDTO(guardado);
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
        VerificacionIdentidad verificacion = verificacionRepository.findByUsuarioIdUsuario(usuario.getIdUsuario())
                .orElse(null);
        boolean verificado = verificacion != null && EstadoVerificacion.VERIFICADA.equals(verificacion.getEstadoVerificacion());
        String estadoVerif = verificacion != null ? verificacion.getEstadoVerificacion().name() : EstadoVerificacion.PENDIENTE.name();

        return UsuarioDTO.builder()
                .idUsuario(usuario.getIdUsuario())
                .nombreUsuario(usuario.getNombreUsuario())
                .apellidoUsuario(usuario.getApellidoUsuario())
                .dniUsuario(usuario.getDniUsuario())
                .emailUsuario(usuario.getEmailUsuario())
                .telefono(usuario.getTelefono())
                .estadoUsuario(usuario.getEstadoUsuario())
                .identidadVerificada(verificado)
                .estadoVerificacion(estadoVerif)
                .fechaRegistro(usuario.getCreatedAt())
                .createdAt(usuario.getCreatedAt())
                .updatedAt(usuario.getUpdatedAt())
                .rolId(usuario.getRol() == null ? null : usuario.getRol().getId())
                .rolUsuario(usuario.getRol() == null ? null : usuario.getRol().getNombre())
                .build();
    }
}
