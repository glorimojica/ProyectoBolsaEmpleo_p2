package cr.ac.una.proyectobolsaempleo.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Pattern;

@Getter
@Setter
public class RegisterOferenteRequest {

    @NotBlank(message = "La identificación es obligatoria")
    @Pattern(regexp = "^[0-9]{9,12}$", message = "La identificación debe contener entre 9 y 12 números")
    private String identificacion;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "El nombre solo puede contener letras y espacios")
    @Size(max = 80, message = "El nombre no puede superar los 80 caracteres")
    private String nombre;

    @NotBlank(message = "El primer apellido es obligatorio")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "El apellido solo puede contener letras y espacios")
    @Size(max = 80, message = "El apellido no puede superar los 80 caracteres")
    private String apellido;

    @NotBlank(message = "La nacionalidad es obligatoria")
    @Pattern(regexp = "^[\\p{L} ]+$", message = "La nacionalidad solo puede contener letras y espacios")
    @Size(max = 80, message = "La nacionalidad no puede superar los 80 caracteres")
    private String nacionalidad;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe contener exactamente 8 números")
    private String telefono;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @Size(max = 150, message = "El correo no puede superar los 150 caracteres")
    private String correo;

    @NotBlank(message = "La residencia es obligatoria")
    @Size(max = 150, message = "La residencia no puede superar los 150 caracteres")
    private String residencia;

    @NotBlank(message = "La clave es obligatoria")
    @Size(min = 4, max = 100, message = "La clave debe tener entre 4 y 100 caracteres")
    private String password;
}
