package cr.ac.una.proyectobolsaempleo.repository;

import cr.ac.una.proyectobolsaempleo.model.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {

    List<Caracteristica> findByPadreIsNullOrderByNombreAsc();

    List<Caracteristica> findByPadreIdOrderByNombreAsc(Long padreId);

    List<Caracteristica> findAllByOrderByNombreAsc();

    List<Caracteristica> findByHijasIsEmptyOrderByNombreAsc();
}