package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Pedido{
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Setter
    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
    @Setter
    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Vendedor restaurante;
    @Setter
    private double precioTotal;
    @ManyToMany
    @JoinTable(
            name = "pedido_detalle",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "detalle_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"pedido_id", "detalle_id"}) // Garantiza unicidad
    )
    private List<DetallePedido> detallesPedido;
    @Enumerated(EnumType.STRING)
    private Estado estado;

    public Pedido() {
        detallesPedido = new ArrayList<>();
    }

    public Pedido(int id, Cliente cliente, Vendedor restaurante, double precioTotal, ArrayList<DetallePedido> detallesPedido, Estado estado) {
        this.id = id;
        this.cliente = cliente;
        this.restaurante = restaurante;
        this.precioTotal = precioTotal;
        this.detallesPedido = detallesPedido;
        this.estado = estado;
    }

    public void agregarDetalle(DetallePedido detalle){
        detallesPedido.add(detalle);
    }

    public void quitarDetalle(DetallePedido detalle){
        detallesPedido.remove(detalle);
    }

    public void setDetallesPedido(ArrayList<DetallePedido> detallesPedido) {
        this.detallesPedido = detallesPedido;
    }

    public double calcularPrecioTotal() {
        double total = 0;
        try {
            for (DetallePedido unDetalle : detallesPedido) {
                if (unDetalle == null) {
                    throw new NullPointerException("DetallePedido es nulo");
                }
                double totalPorDetalle = unDetalle.getCantidad() * unDetalle.getPrecio();
                total = total + totalPorDetalle;
            }
        } catch (NullPointerException e) {
            System.err.println("Se encontró un valor nulo: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.err.println("Error aritmético: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado: " + e.getMessage());
        }
        return total;
    }
}