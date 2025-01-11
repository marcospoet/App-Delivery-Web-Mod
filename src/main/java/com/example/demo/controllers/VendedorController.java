package com.example.demo.controllers;

import com.example.demo.dto.VendedorDTO;
import com.example.demo.mappers.VendedorMapper;
import com.example.demo.model.Vendedor;
import com.example.demo.servicios.IVendedorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vendedor")
public class VendedorController {

    private final IVendedorService vendedorService;
    private final VendedorMapper vendedorMapper;

    @Autowired
    public VendedorController(IVendedorService vendedorService, VendedorMapper vendedorMapper) {
        this.vendedorService = vendedorService;
        this.vendedorMapper = vendedorMapper;
    }
    @GetMapping("/mostrarTodos")
    public ResponseEntity<List<VendedorDTO>> mostrarListaVendedores() {
        List<Vendedor> vendedores = vendedorService.obtenerTodosLosVendedores();

        if (vendedores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        List<VendedorDTO> vendedoresDTO = vendedores.stream()
                .map(vendedorMapper::convertirADTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(vendedoresDTO);
    }

    @GetMapping("/buscarPorNombre")
    public ResponseEntity<List<VendedorDTO>> buscarVendedorPorNombre(@Valid @RequestParam String nombre) {
        List<Vendedor> vendedores = vendedorService.obtenerVendedoresPorNombre(nombre);

        if (vendedores.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }

        List<VendedorDTO> vendedoresDTO = vendedores.stream()
                .map(vendedorMapper::convertirADTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(vendedoresDTO);
    }


    @PostMapping("/crearVendedor")
    public ResponseEntity<VendedorDTO> crearNuevoVendedor(@Valid @RequestBody VendedorDTO vendedorDTO) {
        try {
            Vendedor vendedor = vendedorMapper.convertirAEntidad(vendedorDTO);
            Vendedor vendedorGuardado = vendedorService.crearVendedor(vendedor);
            VendedorDTO vendedorDTOGuardado = vendedorMapper.convertirADTO(vendedorGuardado);

            return ResponseEntity.status(HttpStatus.CREATED).body(vendedorDTOGuardado);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/modificarVendedor/{id}")
    public ResponseEntity<VendedorDTO> modificarVendedor(@PathVariable int id,@Valid @RequestBody VendedorDTO vendedorDTO) {
        try {
            if (!vendedorService.existeVendedor(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Vendedor vendedor = vendedorMapper.convertirAEntidad(vendedorDTO);
            vendedor.setId(id);
            Vendedor vendedorModificado = vendedorService.modificarVendedor(vendedor);
            VendedorDTO vendedorDTOModificado = vendedorMapper.convertirADTO(vendedorModificado);

            return ResponseEntity.ok(vendedorDTOModificado);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/borrarVendedor/{id}")
    public ResponseEntity<Void> eliminarVendedor(@PathVariable int id) {
        try {
            if (!vendedorService.existeVendedor(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            vendedorService.eliminarVendedor(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendedorDTO> buscarVendedor(@PathVariable int id) {
        try {
            Vendedor vendedor = vendedorService.obtenerVendedorPorId(id);

            if (vendedor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            VendedorDTO vendedorDTO = vendedorMapper.convertirADTO(vendedor);
            return ResponseEntity.ok(vendedorDTO);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}