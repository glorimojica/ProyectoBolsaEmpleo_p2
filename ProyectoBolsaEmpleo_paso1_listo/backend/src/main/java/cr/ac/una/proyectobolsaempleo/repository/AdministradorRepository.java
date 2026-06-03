package cr.ac.una.proyectobolsaempleo.repository;

import cr.ac.una.proyectobolsaempleo.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByIdentificacion(String identificacion);
    Optional<Administrador> findByUsuarioCorreo(String correo);
}
