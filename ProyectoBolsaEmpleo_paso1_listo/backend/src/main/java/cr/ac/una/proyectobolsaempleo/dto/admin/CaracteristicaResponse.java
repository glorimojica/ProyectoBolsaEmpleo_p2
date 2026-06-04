package cr.ac.una.proyectobolsaempleo.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaracteristicaResponse {

    private Long id;
    private String nombre;
    private String nombreCompleto;
    private Long padreId;
    private Boolean hoja;

    private List<CaracteristicaResponse> hijas;
}