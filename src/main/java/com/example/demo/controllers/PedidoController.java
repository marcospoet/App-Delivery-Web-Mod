package com.example.demo.controllers;

import com.example.demo.dto.PedidoDTO;
import com.example.demo.dto.DetallePedidoDTO;
import com.example.demo.mappers.PedidoMapper;
import com.example.demo.servicios.IPedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final IPedidoService pedidoService;
    private final PedidoMapper pedidoMapper;

    @Autowired
    public PedidoController(IPedidoService pedidoService, PedidoMapper pedidoMapper) {
        this.pedidoService = pedidoService;
        this.pedidoMapper = pedidoMapper;
    }

    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarPedidos() {
        var pedidos = pedidoService.listarPedidos()
                .stream()
                .map(pedidoMapper::convertirADTO)
                .toList();

        if (pedidos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(pedidos);
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> crearNuevoPedido(@Valid @RequestBody PedidoDTO pedidoDTO) {
            var pedido = pedidoMapper.convertirAEntidad(pedidoDTO);
            var pedidoGuardado = pedidoService.crearPedido(pedido);
            var pedidoDTORetorno = pedidoMapper.convertirADTO(pedidoGuardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedidoDTORetorno);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> buscarPedidoPorId(@PathVariable int id) {
        var pedido = pedidoService.buscarPedidoPorId(id);
        var pedidoDTO = pedidoMapper.convertirADTO(pedido);
        return ResponseEntity.ok(pedidoDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> actualizarPedido(@PathVariable int id, @Valid @RequestBody PedidoDTO pedidoDTO) {
        var pedido = pedidoMapper.convertirAEntidad(pedidoDTO);
        pedido.setId(id);
        var pedidoActualizado = pedidoService.actualizarPedido(pedido);
        return ResponseEntity.ok(pedidoMapper.convertirADTO(pedidoActualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable int id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstadoPedido(@PathVariable int id, @RequestParam String nuevoEstado) {
        pedidoService.cambiarEstadoPedido(id, nuevoEstado);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/detalles")
    public ResponseEntity<List<DetallePedidoDTO>> mostrarDetallesPedido(@PathVariable int id) {
        var pedido = pedidoService.buscarPedidoPorId(id);
        var pedidoDTO = pedidoMapper.convertirADTO(pedido);
        var detalles = pedidoDTO.getDetallesPedido();

        if (detalles == null || detalles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(detalles);
    }

    @GetMapping("listarDetalles/{id}")
    public ResponseEntity<List<DetallePedidoDTO>> listarDetallesPedido(@PathVariable int id) {
        var pedido = pedidoService.buscarPedidoPorId(id);
        var pedidoDTO = pedidoMapper.convertirADTO(pedido);
        var detalles = pedidoDTO.getDetallesPedido();
        if (detalles == null || detalles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(detalles);
    }

}