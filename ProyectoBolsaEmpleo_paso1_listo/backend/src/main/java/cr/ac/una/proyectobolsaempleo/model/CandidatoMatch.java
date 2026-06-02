package cr.ac.una.proyectobolsaempleo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class CandidatoMatch {
    private Oferente oferente;
    private int porcentajeMatch;
    private int requisitosCumplidos;
    private int totalRequisitos;
}
