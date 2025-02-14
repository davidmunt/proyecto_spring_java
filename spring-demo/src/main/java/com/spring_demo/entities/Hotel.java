package com.spring_demo.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entidad del hotel
 */

@Entity
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Schema(description = "ID del hotel", example = "1")
    private Long id;

    @NotNull(message = "El nombre no puede estar vacio")
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    @Schema(description = "Nombre del hotel", example = "Hotel 1")
    private String name;

    @NotNull(message = "La direccion no puede estar vacía")
    @Size(min = 6, message = "La direccion debe tener al menos 6 caracteres")
    @Schema(description = "Dirección del hotel", example = "Calle Principal 123")
    private String direction;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
