package com.example.demo.servicios;

import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.exception.ResourceTypeMismatchException;
import com.example.demo.model.*;
import com.example.demo.repositorio.CategoriaRepository;
import com.example.demo.repositorio.ItemMenuRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemMenuService implements IitemMenuService {

    private final ItemMenuRepository itemMenuRepository;
    private final CategoriaRepository categoriaRepository;
    private final IDetallePedidoService detallePedidoService;

    @Autowired
    public ItemMenuService(ItemMenuRepository itemMenuRepository, CategoriaRepository categoriaRepository, IDetallePedidoService detallePedidoService) {
        this.itemMenuRepository = itemMenuRepository;
        this.categoriaRepository = categoriaRepository;
        this.detallePedidoService = detallePedidoService;
    }

    public List<ItemMenu> obtenerItemsMenu() {
        return itemMenuRepository.findAll();
    }

    public ItemMenu crearItemMenu(ItemMenu itemMenu) {
        //si no es nulo y no existe
        if (itemMenuRepository.existsById(itemMenu.getId())) {
            throw new ResourceAlreadyExistsException("No se puede crear. El ítem con ID " + itemMenu.getId() + " ya existe.");
        }
        return itemMenuRepository.save(itemMenu);
    }

    public boolean existeItemMenu(int id) {
        return itemMenuRepository.existsById(id);
    }
    private void verificarTipoItem(ItemMenu itemMenuExistente, ItemMenu itemMenu) {
        if (itemMenuExistente.getClass() != itemMenu.getClass()) {
            throw new ResourceTypeMismatchException(
                    "El ítem con ID " + itemMenu.getId() + " no es del mismo tipo que el ítem a modificar."
            );
        }
    }
    private String getTipoItemFromItemMenu(ItemMenu itemMenu) {
        if (itemMenu instanceof Bebida) {
            return "Bebida";
        } else if (itemMenu instanceof Comida) {
            return "Comida";
        }
        throw new IllegalArgumentException("Tipo de ItemMenu no soportado.");
    }

    @Transactional
    public ItemMenu modificarItemMenu(ItemMenu itemMenu) {
        ItemMenu itemMenuExistente = itemMenuRepository.findById(itemMenu.getId())
                .orElseThrow(() -> new ResourceNotFoundException("El ítem con ID " + itemMenu.getId() + " no existe."));

        verificarTipoItem(itemMenuExistente, itemMenu);

        Categoria categoria = itemMenu.getCategoria();
        if (categoria == null || categoria.getId() == 0) {
            throw new IllegalArgumentException("El ItemMenu debe tener una categoría con un ID válido.");
        }

        Categoria categoriaExistente = categoriaRepository.findById(categoria.getId())
                .orElseGet(() -> {
                    if (categoria.getDescripcion() == null || categoria.getTipoItem() == null) {
                        throw new IllegalArgumentException("Para crear una nueva categoría, se requieren 'descripcion' y 'tipoItem'.");
                    }
                    // Asegurar que el tipoItem de la nueva categoría coincide con el tipoItem del ItemMenu
                    String tipoItemEsperado = getTipoItemFromItemMenu(itemMenuExistente);
                    if (!categoria.getTipoItem().equalsIgnoreCase(tipoItemEsperado)) {
                        throw new IllegalArgumentException(
                                "El tipoItem de la nueva categoría no coincide con el tipoItem del ItemMenu. " +
                                        "Esperado: " + tipoItemEsperado + ", Proporcionado: " + categoria.getTipoItem()
                        );
                    }
                    Categoria nuevaCategoria = new Categoria();
                    nuevaCategoria.setDescripcion(categoria.getDescripcion());
                    nuevaCategoria.setTipoItem(categoria.getTipoItem());
                    return categoriaRepository.save(nuevaCategoria);
                });


        if (!categoriaExistente.getTipoItem().equalsIgnoreCase(getTipoItemFromItemMenu(itemMenuExistente))) {
            throw new IllegalArgumentException(
                    "El tipoItem de la categoría existente no coincide con el tipoItem del ItemMenu. " +
                            "Esperado: " + getTipoItemFromItemMenu(itemMenuExistente) + ", Actual: " + categoriaExistente.getTipoItem()
            );
        }

        itemMenuExistente.setNombre(itemMenu.getNombre());
        itemMenuExistente.setDescripcion(itemMenu.getDescripcion());
        itemMenuExistente.setPrecio(itemMenu.getPrecio());
        itemMenuExistente.setPeso(itemMenu.getPeso());
        itemMenuExistente.setCategoria(categoriaExistente);

        return itemMenuRepository.save(itemMenuExistente);
    }
    @Transactional
    public void eliminarItemMenu(int id) {
        //verificar si el item existe usando obtenerItemMenu
        ItemMenu itemMenu = obtenerItemMenu(id);
        List<DetallePedido> detallesPedido = detallePedidoService.buscarPorItemMenu(id);
        if (!detallesPedido.isEmpty()) {
            for (DetallePedido detalle : detallesPedido) {
                detallePedidoService.eliminarDetallePedido(detalle.getId());
            }
        }
        itemMenuRepository.delete(itemMenu);

    }

    public ItemMenu obtenerItemMenu(int id) {
        return itemMenuRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("El ítem con ID " + id + " no existe."));
    }

    public List<ItemMenu> obtenerItemsPorCategoria(int idCategoria) {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResourceNotFoundException("La categoría con ID " + idCategoria + " no existe."));
        return itemMenuRepository.findItemMenuByCategoria(categoria);
    }
}
