package cr.ac.una.proyectobolsaempleo.dto.empresa;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PuestoEmpresaRequest {

    @NotBlank(message = "El título del puesto es obligatorio")
    @Size(max = 150, message = "El título no puede superar los 150 caracteres")
    private String titulo;

    @NotBlank(message = "La descripción del puesto es obligatoria")
    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String descripcion;

    @NotNull(message = "El salario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = true, message = "El salario no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "El salario debe tener máximo 10 dígitos enteros y 2 decimales")
    private Double salario;

    @NotNull(message = "Debe indicar si el puesto es público o privado")
    private Boolean publico;
}