package cr.ac.una.proyectobolsaempleo.repository;

import cr.ac.una.proyectobolsaempleo.model.Caracteristica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaracteristicaRepository extends JpaRepository<Caracteristica, Long> {

    List<Caracteristica> findAllByOrderByNombreAsc();

    List<Caracteristica> findByHijasIsEmptyOrderByNombreAsc();

    List<Caracteristica> findByPadreIsNullOrderByNombreAsc();

    List<Caracteristica> findByPadreIdOrderByNombreAsc(Long padreId);

    boolean existsByPadre_Id(Long padreId);

    boolean existsByNombreIgnoreCaseAndPadreIsNull(String nombre);

    boolean existsByNombreIgnoreCaseAndPadre_Id(String nombre, Long padreId);
}