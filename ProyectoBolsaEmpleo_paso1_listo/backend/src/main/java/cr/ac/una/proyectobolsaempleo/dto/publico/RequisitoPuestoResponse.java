package cr.ac.una.proyectobolsaempleo.dto.publico;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequisitoPuestoResponse {

    private Long caracteristicaId;
    private String caracteristica;
    private String categoria;
    private Integer nivel;
}
