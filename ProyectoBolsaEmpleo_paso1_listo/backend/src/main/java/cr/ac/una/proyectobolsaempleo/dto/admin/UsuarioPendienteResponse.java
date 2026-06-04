package cr.ac.una.proyectobolsaempleo.dto.admin;

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
public class UsuarioPendienteResponse {

    private Long id;
    private Long usuarioId;
    private String nombre;
    private String correo;
    private String telefono;
    private String estado;
    private String tipo;
}