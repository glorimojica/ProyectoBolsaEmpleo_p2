package cr.ac.una.proyectobolsaempleo.repository;

import cr.ac.una.proyectobolsaempleo.model.OferenteCaracteristica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OferenteCaracteristicaRepository extends JpaRepository<OferenteCaracteristica, Long> {

    List<OferenteCaracteristica> findByOferenteId(Long oferenteId);

    Optional<OferenteCaracteristica> findByIdAndOferenteId(Long id, Long oferenteId);

    boolean existsByOferenteIdAndCaracteristicaId(Long oferenteId, Long caracteristicaId);

    Optional<OferenteCaracteristica> findByOferenteIdAndCaracteristicaId(Long oferenteId, Long caracteristicaId);
}