package cr.ac.una.proyectobolsaempleo.dto.oferente;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabilidadRequest {

    @NotNull(message = "La característica es obligatoria")
    private Long caracteristicaId;

    @NotNull(message = "El nivel es obligatorio")
    @Min(value = 1, message = "El nivel mínimo es 1")
    @Max(value = 5, message = "El nivel máximo es 5")
    private Integer nivel;
}
