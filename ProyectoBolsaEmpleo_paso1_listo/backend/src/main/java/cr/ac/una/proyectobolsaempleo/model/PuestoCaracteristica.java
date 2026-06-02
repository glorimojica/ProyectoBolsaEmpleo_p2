package cr.ac.una.proyectobolsaempleo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "puesto_caracteristica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PuestoCaracteristica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "puesto_id", nullable = false)
    private Puesto puesto;
    @ManyToOne
    @JoinColumn(name = "caracteristica_id", nullable = false)
    private Caracteristica caracteristica;
    @Column(nullable = false)
    private Integer nivel;
}