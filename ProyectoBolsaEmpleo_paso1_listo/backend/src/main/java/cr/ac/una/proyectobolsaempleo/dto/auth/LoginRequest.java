package cr.ac.una.proyectobolsaempleo.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank(message = "El usuario es obligatorio")
    private String usuario;

    @NotBlank(message = "La clave es obligatoria")
    private String password;
}
