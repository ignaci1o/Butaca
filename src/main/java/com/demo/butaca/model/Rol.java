package com.demo.butaca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "rol", uniqueConstraints = @UniqueConstraint(name = "uk_rol_nombre", columnNames = "nombre_rol"))
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol extends AuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "nombre_rol", nullable = false, length = 20, unique = true)
    private String nombre;
}
