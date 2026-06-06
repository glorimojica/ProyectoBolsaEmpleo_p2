package cr.ac.una.proyectobolsaempleo.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
public class RegisterEmpresaRequest {

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    @Size(max = 100, message = "El nombre de la empresa no puede superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "La localización es obligatoria")
    @Size(max = 150, message = "La localización no puede superar los 150 caracteres")
    private String localizacion;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 150, message = "El correo no puede superar los 150 caracteres")
    private String correo;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe contener exactamente 8 números")
    private String telefono;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
    private String descripcion;

    @NotBlank(message = "La clave es obligatoria")
    @Size(min = 4, max = 100, message = "La clave debe tener entre 4 y 100 caracteres")
    private String password;
}
