package cr.ac.una.proyectobolsaempleo.dto.admin;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaracteristicaRequest {

    @NotBlank(message = "El nombre de la característica es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    private Long padreId;
}