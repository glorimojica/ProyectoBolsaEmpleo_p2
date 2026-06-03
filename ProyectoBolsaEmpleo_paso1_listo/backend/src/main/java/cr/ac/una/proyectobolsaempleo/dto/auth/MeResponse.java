package cr.ac.una.proyectobolsaempleo.dto.auth;

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
public class MeResponse {

    private Long usuarioId;

    private String nombre;

    private String correo;

    private String rol;

    private String estado;
}
