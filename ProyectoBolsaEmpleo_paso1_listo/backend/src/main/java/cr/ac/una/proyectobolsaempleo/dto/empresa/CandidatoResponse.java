package cr.ac.una.proyectobolsaempleo.dto.empresa;

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
public class CandidatoResponse {

    private Long oferenteId;
    private String nombre;
    private String correo;
    private String telefono;
    private String residencia;

    private Integer requisitosCumplidos;
    private Integer totalRequisitos;
    private Double porcentajeCoincidencia;
}