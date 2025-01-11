package com.example.demo.servicios;

import com.example.demo.exception.ResourceAlreadyExistsException;
import com.example.demo.model.Vendedor;
import com.example.demo.repositorio.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VendedorService implements IVendedorService {

    private final VendedorRepository vendedorRepository;

    @Autowired
    public VendedorService(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    @Override
    public List<Vendedor> obtenerVendedoresPorNombre(String nombre) {
        return vendedorRepository.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public Vendedor crearVendedor(Vendedor vendedor) {
        if (vendedorRepository.existsById(vendedor.getId())) {
            throw new ResourceAlreadyExistsException("No se puede crear. El vendedor con ID " + vendedor.getId() + " ya existe.");
        }
        return vendedorRepository.save(vendedor);
    }

    @Override
    public Vendedor modificarVendedor(Vendedor vendedor) {
        return vendedorRepository.save(vendedor);
    }

    @Override
    public void eliminarVendedor(int id) {
        vendedorRepository.deleteById(id);
    }

    @Override
    public Vendedor obtenerVendedorPorId(int id) {
        return vendedorRepository.findById(id).orElse(null);
    }

    @Override
    public boolean existeVendedor(int id) {
        return vendedorRepository.existsById(id);
    }

    @Override
    public List<Vendedor> obtenerTodosLosVendedores() {
        return vendedorRepository.findAll();
    }
}