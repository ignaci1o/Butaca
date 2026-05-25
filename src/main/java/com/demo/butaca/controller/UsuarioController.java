package com.demo.butaca.controller;

import com.demo.butaca.dto.UsuarioDTO;
import com.demo.butaca.model.EstadoUsuario;
import com.demo.butaca.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioDTO> listar() {
        return usuarioService.listar();
    }

    @GetMapping("/{id}")
    public UsuarioDTO obtener(@PathVariable Long id) {
        return usuarioService.findById(id);
    }

    @PutMapping("/{id}")
    public UsuarioDTO actualizar(@PathVariable Long id, @Valid @RequestBody UsuarioDTO dto) {
        return usuarioService.actualizar(id, dto);
    }

    @PutMapping("/{id}/estado")
    public UsuarioDTO cambiarEstado(@PathVariable Long id, @RequestParam EstadoUsuario estado) {
        return usuarioService.changeEstado(id, estado);
    }

    @PutMapping("/{id}/rol")
    public UsuarioDTO asignarRol(@PathVariable Long id, @RequestParam String rol) {
        return usuarioService.assignRole(id, rol);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        usuarioService.eliminar(id);
    }
}
