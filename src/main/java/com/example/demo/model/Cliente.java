package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter

@Entity
@Table
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String cuit;
    private String email;
    private String direccion;
    private String nombre;

    public Cliente(int id, String cuit, String email, String direccion, String nombre) {
        this.id = id;
        this.cuit = cuit;
        this.email = email;
        this.direccion = direccion;
        this.nombre = nombre;
    }
}
