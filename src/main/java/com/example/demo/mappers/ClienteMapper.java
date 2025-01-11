package com.example.demo.mappers;

import com.example.demo.model.Cliente;
import com.example.demo.dto.ClienteDTO;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    /**
     * Convierte una entidad Cliente a un DTO ClienteDTO.
     *
     * @param cliente la entidad Cliente a convertir.
     * @return el ClienteDTO convertido.
     */
    public ClienteDTO convertirADTO(Cliente cliente) {
        if (cliente == null) {
            return null;
        }

        return new ClienteDTO(
                cliente.getId(),
                cliente.getCuit(),
                cliente.getEmail(),
                cliente.getDireccion(),
                cliente.getNombre()
        );
    }

    /**
     * Convierte un DTO ClienteDTO a una entidad Cliente.
     *
     * @param clienteDTO el DTO ClienteDTO a convertir.
     * @return la entidad Cliente convertida.
     */
    public Cliente convertirAEntidad(ClienteDTO clienteDTO) {
        if (clienteDTO == null) {
            return null;
        }

        Cliente cliente = new Cliente();
        cliente.setId(clienteDTO.getId());
        cliente.setCuit(clienteDTO.getCuit());
        cliente.setEmail(clienteDTO.getEmail());
        cliente.setDireccion(clienteDTO.getDireccion());
        cliente.setNombre(clienteDTO.getNombre());
        return cliente;
    }
}