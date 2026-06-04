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
public class OferentePerfilResponse {

    private Long id;
    private String identificacion;
    private String nombre;
    private String apellido;
    private String nacionalidad;
    private String telefono;
    private String correo;
    private String residencia;
    private String cvNombreArchivo;
    private Boolean cvDisponible;
}
