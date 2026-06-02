package cr.ac.una.proyectobolsaempleo.dto.publico;

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
public class PuestoPublicoResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private Double salario;
    private Boolean publico;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    private Long empresaId;
    private String empresa;

    private List<RequisitoPuestoResponse> requisitos;
}