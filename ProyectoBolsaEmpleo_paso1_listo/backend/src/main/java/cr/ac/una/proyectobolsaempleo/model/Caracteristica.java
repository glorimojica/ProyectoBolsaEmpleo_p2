package cr.ac.una.proyectobolsaempleo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "caracteristica")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Caracteristica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "padre_id")
    private Caracteristica padre;

    @OneToMany(mappedBy = "padre", cascade = CascadeType.ALL)
    @OrderBy("nombre ASC")
    @Builder.Default
    private List<Caracteristica> hijas = new ArrayList<>();

    @Transient
    public String getNombreCompleto() {
        if (padre == null) {
            return nombre;
        }
        return padre.getNombreCompleto() + " > " + nombre;
    }

    @Transient
    public boolean esHoja() {
        return hijas == null || hijas.isEmpty();
    }
}