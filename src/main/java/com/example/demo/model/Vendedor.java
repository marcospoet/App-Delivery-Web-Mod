package com.example.demo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter

@Entity
public class Vendedor {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String nombre;
    private String direccion;
    @Setter
    @Getter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "itemMenu_id")
    private List<ItemMenu> itemsMenu = new ArrayList<>();
    @OneToMany(mappedBy = "restaurante", targetEntity = Pedido.class, cascade = CascadeType.ALL)
    private List<Pedido> listaDePedidos;


    public Vendedor(int id, String nombre, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
    }

    public ArrayList<Comida> getItemComida() {
        ArrayList<Comida> comidas = new ArrayList<>();
        for (ItemMenu item : itemsMenu) {
            if (item.esComida()) {
                comidas.add((Comida) item);
            }
        }
        return comidas;
    }

    public ArrayList<Bebida> getItemBebida() {
        ArrayList<Bebida> bebidas = new ArrayList<>();
        for (ItemMenu item : itemsMenu) {
            if (item.esBebida()) {
                bebidas.add((Bebida) item);
            }
        }
        return bebidas;
    }

    public ArrayList<Comida> getItemComidaVegana(){
        ArrayList<Comida> comidasVeganas = new ArrayList<>();
        for (ItemMenu item : itemsMenu) {
            if (item.esComida() && item.aptoVegano()) {
                comidasVeganas.add((Comida) item);
            }
        }
        return comidasVeganas;
    }

    public ArrayList<Bebida> getItemBebidaSinAlcohol(){
        ArrayList<Bebida> bebidasSinAlcohol = new ArrayList<>();
        for (ItemMenu item : itemsMenu) {
            if (item.esBebida() && ((Bebida) item).getGraduacionAlcoholica() == 0) {
                bebidasSinAlcohol.add((Bebida) item);
            }
        }
        return bebidasSinAlcohol;
    }

    public static Vendedor buscarVendedorPorNombre(ArrayList<Vendedor> vendedores, String nombre) {
        for (Vendedor vendedor : vendedores) {
            if (vendedor.getNombre().equalsIgnoreCase(nombre)) {
                return vendedor;
            }
        }
        return null;
    }

    public static Vendedor buscarVendedorPorId(ArrayList<Vendedor> vendedores, int id) {
        for (Vendedor vendedor : vendedores) {
            if (vendedor.getId() == id) {
                return vendedor;
            }
        }
        return null;
    }
    public void addItemMenu(ItemMenu unItemMenu){
        itemsMenu.add(unItemMenu);
    }

    public boolean perteneceId(Vendedor unVendedor, int unId){
        boolean pertenece = false;
        for (ItemMenu unItemMenu: unVendedor.getItemsMenu()){
            if (unItemMenu.getId() == unId) {
                pertenece = true;
                break;
            }
        }
        return pertenece;
    }

    public ItemMenu getItemById(Vendedor unVendedor, int unId){
        for (ItemMenu unItemMenu: unVendedor.getItemsMenu()){
            if(unItemMenu.getId() == unId){
                return unItemMenu;
            }
        }
        return null;
    }

    public Pedido generarPedido(int[][] itemsyCant, Vendedor vendedor, Cliente unCliente) {
        Pedido nuevoPedido = new Pedido();
        try {
            for (int[] itemsYcant : itemsyCant) {
                if (perteneceId(vendedor, itemsYcant[0])) {
                    ItemMenu nuevoItemMenu = getItemById(vendedor, itemsYcant[0]);
                    if (nuevoItemMenu == null) {
                        throw new IllegalArgumentException("ItemMenu not found for id: " + itemsYcant[0]);
                    }
                    DetallePedido nuevoDetalle = new DetallePedido(nuevoItemMenu.getId(), nuevoItemMenu, itemsYcant[1], nuevoItemMenu.getPrecio(), List.of(nuevoPedido));
                    nuevoPedido.agregarDetalle(nuevoDetalle);
                    nuevoPedido.setEstado(Estado.RECIBIDO);
                    nuevoPedido.setCliente(unCliente);
                }
            }
            nuevoPedido.setPrecioTotal(nuevoPedido.calcularPrecioTotal());
        } catch (NullPointerException e) {
            System.err.println("Se encontró un valor nulo: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Índice de matriz fuera de límites: " + e.getMessage());
        } catch (ClassCastException e) {
            System.err.println("Error de conversión de clase: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Argumento ilegal: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ocurrió un error inesperado: " + e.getMessage());
        }
        return nuevoPedido;
    }

    public List<Pedido> buscarPedidosPorEstado(Estado estado) {
        return listaDePedidos.stream()
                .filter(pedido -> pedido.getEstado().equals(estado))
                .collect(Collectors.toList());
    }

    public void actualizarEstadoPedido(int pedidoId, Estado nuevoEstado) {
        for (Pedido pedido : listaDePedidos) {
            if (pedido.getId() == pedidoId) {
                pedido.setEstado(nuevoEstado);
                break;
            }
        }
    }
}
