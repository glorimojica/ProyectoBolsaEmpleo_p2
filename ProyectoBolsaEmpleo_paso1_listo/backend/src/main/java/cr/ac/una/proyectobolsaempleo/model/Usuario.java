package cr.ac.una.proyectobolsaempleo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String rol; // ADMIN, EMPRESA, OFERENTE

    @Column(nullable = false)
    private boolean activo;

    @Column(nullable = false)
    private String estado; // PENDIENTE, APROBADO, RECHAZADO

    @Column(length = 500)
    private String comentarioRevision;
}