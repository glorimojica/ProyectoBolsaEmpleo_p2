package cr.ac.una.proyectobolsaempleo.dto.empresa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PuestoEmpresaResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private Double salario;
    private Boolean publico;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    private List<RequisitoPuestoResponse> requisitos;
}