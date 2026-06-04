package cr.ac.una.proyectobolsaempleo.dto.oferente;

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
public class HabilidadResponse {

    private Long id;
    private Long caracteristicaId;
    private String caracteristica;
    private String nombreCompleto;
    private Integer nivel;
}