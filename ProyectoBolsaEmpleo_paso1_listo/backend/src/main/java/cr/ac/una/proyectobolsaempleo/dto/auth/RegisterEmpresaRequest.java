package cr.ac.una.proyectobolsaempleo.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterEmpresaRequest {

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombre;

    @NotBlank(message = "La localización es obligatoria")
    private String localizacion;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "La clave es obligatoria")
    @Size(min = 4, message = "La clave debe tener al menos 4 caracteres")
    private String password;
}
