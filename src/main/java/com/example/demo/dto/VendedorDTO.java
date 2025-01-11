package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VendedorDTO {
    private int id;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 20, message = "El nombre no puede tener más de 20 caracteres")
    private String nombre;
    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 30, message = "La dirección no puede tener más de 30 caracteres")
    private String direccion;
}
