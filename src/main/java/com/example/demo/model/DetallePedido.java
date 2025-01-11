package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter

@Entity
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemMenu item;
    @ManyToMany(mappedBy = "detallesPedido")
    private List<Pedido> pedido;
    private int cantidad;
    private double precio;

    public DetallePedido() {
    pedido = new ArrayList<>();
    }

    public DetallePedido(int id, ItemMenu item, int cantidad, double precio, List<Pedido> pedido) {
        this.id = id;
        this.item = item;
        this.pedido = pedido;
        this.cantidad = cantidad;
        this.precio = precio;
    }
    public void addPedido(Pedido pedido){
        //agregar pedido a la lista de pedidos
        this.pedido.add(pedido);
    }
}
