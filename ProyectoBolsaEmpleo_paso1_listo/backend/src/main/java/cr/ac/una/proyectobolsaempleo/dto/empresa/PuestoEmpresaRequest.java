package cr.ac.una.proyectobolsaempleo.dto.empresa;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PuestoEmpresaRequest {

    @NotBlank(message = "El título del puesto es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripción del puesto es obligatoria")
    private String descripcion;

    @NotNull(message = "El salario es obligatorio")
    @Min(value = 0, message = "El salario no puede ser negativo")
    private Double salario;

    @NotNull(message = "Debe indicar si el puesto es público o privado")
    private Boolean publico;
}
