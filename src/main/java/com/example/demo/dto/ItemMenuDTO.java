package com.example.demo.dto;

//import com.example.demo.serializers.ItemMenuDTODeserializer;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "tipoItem"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ComidaDTO.class, name = "Comida"),
        @JsonSubTypes.Type(value = BebidaDTO.class, name = "Bebida")
})
public abstract class ItemMenuDTO {
    private int id;
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Size(max = 200, message = "La descripción no puede tener más de 200 caracteres")
    private String descripcion;

    @Min(value = 0, message = "El precio no puede ser negativo")
    private double precio;

    @NotNull(message = "La categoría no puede ser nula")
    private CategoriaDTO categoria;

    @Min(value = 0, message = "El peso no puede ser negativo")
    private double peso;

    // Métodos abstractos para diferenciar entre comida y bebida
    public abstract boolean esComida();
    public abstract boolean esBebida();
    public abstract boolean isAptoVegano();
}
