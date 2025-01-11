package com.example.demo.mappers;

import com.example.demo.dto.VendedorDTO;
import com.example.demo.model.Vendedor;
import org.springframework.stereotype.Component;

@Component
public class VendedorMapper {

    /**
     * Convierte una entidad Vendedor a un DTO VendedorDTO.
     *
     * @param vendedor la entidad Vendedor a convertir.
     * @return el VendedorDTO convertido.
     */
    public VendedorDTO convertirADTO(Vendedor vendedor) {
        if (vendedor == null) {
            return null;
        }

        return new VendedorDTO(
                vendedor.getId(),
                vendedor.getNombre(),
                vendedor.getDireccion()
        );
    }

    /**
     * Convierte un DTO VendedorDTO a una entidad Vendedor.
     *
     * @param vendedorDTO el DTO VendedorDTO a convertir.
     * @return la entidad Vendedor convertida.
     */
    public Vendedor convertirAEntidad(VendedorDTO vendedorDTO) {
        if (vendedorDTO == null) {
            return null;
        }

        Vendedor vendedor = new Vendedor();
        vendedor.setId(vendedorDTO.getId());
        vendedor.setNombre(vendedorDTO.getNombre());
        vendedor.setDireccion(vendedorDTO.getDireccion());
        return vendedor;
    }
}
