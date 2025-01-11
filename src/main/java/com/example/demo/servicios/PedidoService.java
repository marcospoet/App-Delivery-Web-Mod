package com.example.demo.servicios;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Estado;
import com.example.demo.model.Pedido;
import com.example.demo.model.DetallePedido;
import com.example.demo.repositorio.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService implements IPedidoService {

    private final PedidoRepository pedidoRepository;
    private final IDetallePedidoService detallePedidoService;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, IDetallePedidoService detallePedidoService) {
        this.pedidoRepository = pedidoRepository;
        this.detallePedidoService = detallePedidoService;
    }


    @Override
    public List<Pedido> listarPedidos() {
        return pedidoRepository.findAll();
    }


    @Override
    @Transactional
    public Pedido crearPedido(Pedido pedido) {
        ArrayList<DetallePedido> detallesProcesados = new ArrayList<>();

        for (DetallePedido detalle : pedido.getDetallesPedido()) {
            Optional<DetallePedido> detalleExistente = detallePedidoService.buscarPorCampos(
                    detalle.getItem().getId(),
                    detalle.getCantidad(),
                    detalle.getPrecio()
            );

            if (detalleExistente.isPresent()) {
                detallesProcesados.add(detalleExistente.get());
            } else {
                DetallePedido nuevoDetalle = detallePedidoService.crearDetallePedido(detalle);
                detallesProcesados.add(nuevoDetalle);
            }
        }

        pedido.setDetallesPedido(detallesProcesados);

        return pedidoRepository.save(pedido);
    }



    @Override
    public Pedido buscarPedidoPorId(int id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El pedido con ID " + id + " no existe."));
    }

    @Override
    public boolean existePedido(int id) {
        return pedidoRepository.existsById(id);
    }

    @Override
    public Pedido actualizarPedido(Pedido pedido) {
        Optional<Pedido> pedidoOptional = pedidoRepository.findById(pedido.getId());
        if (pedidoOptional.isEmpty()) {
            throw new ResourceNotFoundException("El pedido con ID " + pedido.getId() + " no existe.");
        }else {
            Pedido pedidoActualizado = pedidoOptional.get();
            pedidoActualizado.setCliente(pedido.getCliente());
            pedidoActualizado.setRestaurante(pedido.getRestaurante());
            pedidoActualizado.setPrecioTotal(pedido.getPrecioTotal());
            for(DetallePedido detalle : pedido.getDetallesPedido()){
                if(detallePedidoService.buscarPorCampos(detalle.getItem().getId(), detalle.getCantidad(), detalle.getPrecio()).isEmpty()){
                    detallePedidoService.crearDetallePedido(detalle);
                }
            }
            pedidoActualizado.setDetallesPedido((ArrayList<DetallePedido>) pedido.getDetallesPedido());
            pedidoActualizado.setEstado(pedido.getEstado());
            return pedidoRepository.save(pedidoActualizado);
        }
    }

    @Override
    public void eliminarPedido(int id) {
        if (!existePedido(id)) {
            throw new ResourceNotFoundException("El pedido con ID " + id + " no existe.");
        }
        pedidoRepository.deleteById(id);
    }

    @Override
    public void cambiarEstadoPedido(int id, String nuevoEstado) {
        Pedido pedido = buscarPedidoPorId(id);

        // Intentar parsear el enum
        Estado estado;
        try {
            estado = Estado.valueOf(nuevoEstado.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado);
        }

        pedido.setEstado(estado);
        pedidoRepository.save(pedido);
    }
}

