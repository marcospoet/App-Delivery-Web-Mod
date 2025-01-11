package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ClienteDTO {
    private int id;

    @NotBlank(message = "El CUIT no puede estar vacío")
    @Size(max = 11, message = "El CUIT no puede tener más de 11 caracteres")
    private String cuit;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato del email es inválido")
    private String email;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(max = 30, message = "La dirección no puede tener más de 30 caracteres")
    private String direccion;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 20, message = "El nombre no puede tener más de 20 caracteres")
    private String nombre;
}
