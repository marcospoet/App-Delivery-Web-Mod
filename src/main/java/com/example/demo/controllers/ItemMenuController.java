package com.example.demo.controllers;

import com.example.demo.dto.ItemMenuDTO;
import com.example.demo.mappers.ItemMenuMapper;
import com.example.demo.model.ItemMenu;
import com.example.demo.servicios.IitemMenuService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itemMenu")
public class ItemMenuController {

    private final IitemMenuService itemMenuService;
    private final ItemMenuMapper itemMenuMapper;

    @Autowired
    public ItemMenuController(IitemMenuService itemMenuService, ItemMenuMapper itemMenuMapper) {
        this.itemMenuService = itemMenuService;
        this.itemMenuMapper = itemMenuMapper;
    }

    @GetMapping("/items")
    public ResponseEntity<List<ItemMenuDTO>> mostrarListaItems() {
        List<ItemMenu> items = itemMenuService.obtenerItemsMenu();

        if (items.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<ItemMenuDTO> itemsDTO = items.stream()
                .map(itemMenuMapper::convertirADTO)
                .toList();

        return ResponseEntity.ok(itemsDTO);
    }

    @PostMapping("/crearItem")
    public ResponseEntity<ItemMenuDTO> crearItemMenu(@Valid @RequestBody ItemMenuDTO itemMenuDTO) {
        ItemMenu itemMenu = itemMenuMapper.convertirAEntidad(itemMenuDTO);
        ItemMenu itemMenuGuardado = itemMenuService.crearItemMenu(itemMenu);
        ItemMenuDTO itemMenuDTOGuardado = itemMenuMapper.convertirADTO(itemMenuGuardado);
        return ResponseEntity.status(HttpStatus.CREATED).body(itemMenuDTOGuardado);
    }

    @PutMapping("/modificarItem/{id}")
    public ResponseEntity<ItemMenuDTO> modificarItemMenu(@PathVariable int id, @Valid @RequestBody ItemMenuDTO itemMenuDTO) {
        itemMenuDTO.setId(id);
        ItemMenu itemMenu = itemMenuMapper.convertirAEntidad(itemMenuDTO);
        ItemMenu itemMenuModificado = itemMenuService.modificarItemMenu(itemMenu);
        ItemMenuDTO itemMenuDTOModificado = itemMenuMapper.convertirADTO(itemMenuModificado);
        return ResponseEntity.ok(itemMenuDTOModificado);
    }

    @DeleteMapping("/eliminarItem/{id}")
    public ResponseEntity<Void> eliminarItemMenu(@PathVariable int id) {
        itemMenuService.eliminarItemMenu(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buscarItem/{id}")
    public ResponseEntity<ItemMenuDTO> buscarItemMenu(@PathVariable int id) {
        ItemMenu itemMenu = itemMenuService.obtenerItemMenu(id);
        ItemMenuDTO itemMenuDTO = itemMenuMapper.convertirADTO(itemMenu);
        return ResponseEntity.ok(itemMenuDTO);
    }

    @GetMapping("/listarItemsPorCategoria/{idCategoria}")
    public ResponseEntity<List<ItemMenuDTO>> listarItemsPorCategoria(@PathVariable int idCategoria) {
        List<ItemMenu> items = itemMenuService.obtenerItemsPorCategoria(idCategoria);
        List<ItemMenuDTO> itemsDTO = items.stream()
                .map(itemMenuMapper::convertirADTO)
                .toList();

        if (itemsDTO.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(itemsDTO);
    }
}

