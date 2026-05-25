package com.demo.butaca.service;

import com.demo.butaca.dto.CategoriaDTO;
import com.demo.butaca.model.Categoria;
import com.demo.butaca.repository.CategoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaDTO> listar() {
        return categoriaRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public CategoriaDTO obtener(Long id) {
        return toDTO(buscarCategoria(id));
    }

    @Transactional
    public CategoriaDTO crear(CategoriaDTO dto) {
        categoriaRepository.findByNombreIgnoreCase(dto.getNombre()).ifPresent(categoria -> {
            throw new IllegalArgumentException("Ya existe una categoria con ese nombre");
        });
        Categoria categoria = Categoria.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
        return toDTO(categoriaRepository.save(categoria));
    }

    @Transactional
    public CategoriaDTO actualizar(Long id, CategoriaDTO dto) {
        Categoria categoria = buscarCategoria(id);
        categoria.setNombre(dto.getNombre());
        categoria.setDescripcion(dto.getDescripcion());
        return toDTO(categoriaRepository.save(categoria));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new IllegalArgumentException("Categoria no encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    private Categoria buscarCategoria(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada"));
    }

    public CategoriaDTO toDTO(Categoria categoria) {
        return CategoriaDTO.builder()
                .idCategoria(categoria.getIdCategoria())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .build();
    }
}
