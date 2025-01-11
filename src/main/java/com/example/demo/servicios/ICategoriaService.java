package com.example.demo.servicios;

import com.example.demo.model.Categoria;

import java.util.List;

public interface ICategoriaService {
    Categoria crearCategoria(Categoria categoria);
    Categoria buscarCategoriaPorId(int id);
    Categoria modificarCategoria(Categoria categoriaExistente);
    void eliminarCategoria(int id);
    boolean existeCategoria(int id);
    List<Categoria> listarCategorias();
}
