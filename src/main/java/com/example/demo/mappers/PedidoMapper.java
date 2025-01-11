package com.example.demo.mappers;

import com.example.demo.dto.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;
import com.example.demo.servicios.ClienteService;
import com.example.demo.servicios.IitemMenuService;
import com.example.demo.servicios.VendedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PedidoMapper {

    private final ClienteService clienteService;
    private final VendedorService vendedorService;
    private final IitemMenuService itemMenuService;

    @Autowired
    public PedidoMapper(ClienteService clienteService, VendedorService vendedorService, IitemMenuService itemMenuService) {
        this.clienteService = clienteService;
        this.vendedorService = vendedorService;
        this.itemMenuService = itemMenuService;
    }
    /**
     * Convierte una entidad Pedido a su correspondiente DTO PedidoDTO.
     *
     * @param pedido la entidad Pedido a convertir.
     * @return el DTO correspondiente.
     */
    public PedidoDTO convertirADTO(Pedido pedido) {
        if (pedido == null) {
            return null;
        }

        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setClienteId(pedido.getCliente().getId());
        dto.setVendedorId(pedido.getRestaurante().getId());
        dto.setPrecioTotal(pedido.getPrecioTotal());
        dto.setDetallesPedido(convertirListaDetallesADTO(pedido.getDetallesPedido()));
        dto.setEstado(pedido.getEstado());
        return dto;
    }

    /**
     * Convierte un DTO PedidoDTO a su correspondiente entidad Pedido.
     *
     * @param dto el DTO PedidoDTO a convertir.
     * @return la entidad correspondiente.
     */
    public Pedido convertirAEntidad(PedidoDTO dto) {
        if (dto == null) {
            return null;
        }

        Pedido pedido = new Pedido();
        pedido.setId(dto.getId());
        if(clienteService.obtenerClientePorId(dto.getClienteId()) == null){
            throw new ResourceNotFoundException("El cliente no existe");
        }
        if(vendedorService.obtenerVendedorPorId(dto.getVendedorId()) == null){
            throw new ResourceNotFoundException("El vendedor no existe");
        }
        pedido.setCliente(clienteService.obtenerClientePorId(dto.getClienteId()));
        pedido.setRestaurante(vendedorService.obtenerVendedorPorId(dto.getVendedorId()));
        pedido.setPrecioTotal(dto.getPrecioTotal());
        pedido.setEstado(dto.getEstado() != null ? dto.getEstado() : Estado.PENDIENTE);

        // Convertir y agregar DetallePedido
        List<DetallePedido> detalles = convertirListaDetallesAEntidad(dto.getDetallesPedido(), pedido);
        for (DetallePedido detalle : detalles) {
            pedido.agregarDetalle(detalle);
        }

        return pedido;
    }

    /**
     * Convierte una lista de entidades DetallePedido a una lista de DetallePedidoDTO.
     *
     * @param detalles la lista de entidades DetallePedido.
     * @return la lista correspondiente de DetallePedidoDTO.
     */
    private List<DetallePedidoDTO> convertirListaDetallesADTO(List<DetallePedido> detalles) {
        if (detalles == null) return new ArrayList<>();
        List<DetallePedidoDTO> listaDTO = new ArrayList<>();
        for (DetallePedido d : detalles) {
            DetallePedidoDTO dto = new DetallePedidoDTO();
            dto.setId(d.getId());
            dto.setCantidad(d.getCantidad());
            dto.setPrecio(d.getPrecio());
            // Asignar solo el itemMenuId
            if (d.getItem() != null) {
                dto.setItemMenuId(d.getItem().getId());
            }
            listaDTO.add(dto);
        }
        return listaDTO;
    }

    /**
     * Convierte una lista de DetallePedidoDTO a una lista de entidades DetallePedido.
     *
     * @param detallesDTO la lista de DetallePedidoDTO.
     * @param pedido      la entidad Pedido a la que pertenecen los detalles.
     * @return la lista correspondiente de entidades DetallePedido.
     */
    private ArrayList<DetallePedido> convertirListaDetallesAEntidad(List<DetallePedidoDTO> detallesDTO, Pedido pedido) {
        if (detallesDTO == null || detallesDTO.isEmpty()) {
            throw new IllegalArgumentException("La lista de detalles no puede ser nula o vacía");
        }

        ArrayList<DetallePedido> lista = new ArrayList<>();
        for (DetallePedidoDTO dto : detallesDTO) {
            // Validar que el DTO tiene la información necesaria
            if (dto.getItemMenuId() == 0) {
                throw new IllegalArgumentException("El ID del ItemMenu no puede ser 0");
            }
            DetallePedido detalle = new DetallePedido();
            detalle.setCantidad(dto.getCantidad());
            detalle.setId(dto.getId());
            ItemMenu itemMenu = itemMenuService.obtenerItemMenu(dto.getItemMenuId());
            detalle.setPrecio(itemMenu.getPrecio()*dto.getCantidad());
            detalle.setItem(itemMenu);
            detalle.addPedido(pedido);
            lista.add(detalle);
        }

        return lista;
    }
}

