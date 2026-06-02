package cr.ac.una.proyectobolsaempleo.repository;

import cr.ac.una.proyectobolsaempleo.model.Oferente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OferenteRepository extends JpaRepository<Oferente, Long> {

    List<Oferente> findByUsuarioEstado(String estado);

    Optional<Oferente> findByUsuarioCorreo(String correo);
}