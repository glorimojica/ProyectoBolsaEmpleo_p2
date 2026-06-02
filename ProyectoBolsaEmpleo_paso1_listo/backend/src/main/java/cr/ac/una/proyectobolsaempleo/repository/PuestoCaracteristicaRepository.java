package cr.ac.una.proyectobolsaempleo.repository;

import cr.ac.una.proyectobolsaempleo.model.PuestoCaracteristica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PuestoCaracteristicaRepository extends JpaRepository<PuestoCaracteristica, Long> {
    List<PuestoCaracteristica> findByPuestoId(Long puestoId);
    Optional<PuestoCaracteristica> findByIdAndPuestoId(Long id, Long puestoId);
    boolean existsByPuestoIdAndCaracteristicaId(Long puestoId, Long caracteristicaId);
}
