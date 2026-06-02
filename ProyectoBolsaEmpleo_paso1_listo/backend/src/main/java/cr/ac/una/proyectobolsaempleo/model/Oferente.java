package cr.ac.una.proyectobolsaempleo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "oferente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Oferente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String identificacion;
    private String nombre;
    private String apellido;
    private String telefono;
    private String residencia;
    private String nacionalidad;

    @Column(name = "cv_nombre_archivo")
    private String cvNombreArchivo;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;
}