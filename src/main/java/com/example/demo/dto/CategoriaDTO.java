package com.example.demo.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@NoArgsConstructor
@Getter
@Setter
public class CategoriaDTO {
    private int id;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 100, message = "La descripción no puede tener más de 100 caracteres")
    private String descripcion;

    @NotBlank(message = "El tipo de ítem no puede estar vacío")
    @Size(max = 6, message = "El tipo de ítem no puede tener más de 6 caracteres")
    private String tipoItem; // Bebida o Comida

    public CategoriaDTO(int id, String descripcion, String tipoItem) {
        this.id = id;
        this.descripcion = descripcion;
        this.tipoItem = tipoItem;
    }
}
