package cr.ac.una.proyectobolsaempleo.config;

import cr.ac.una.proyectobolsaempleo.model.Administrador;
import cr.ac.una.proyectobolsaempleo.model.Caracteristica;
import cr.ac.una.proyectobolsaempleo.model.Empresa;
import cr.ac.una.proyectobolsaempleo.model.Oferente;
import cr.ac.una.proyectobolsaempleo.model.Usuario;
import cr.ac.una.proyectobolsaempleo.repository.AdministradorRepository;
import cr.ac.una.proyectobolsaempleo.repository.CaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.EmpresaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteRepository;
import cr.ac.una.proyectobolsaempleo.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(
            UsuarioRepository usuarioRepository,
            AdministradorRepository administradorRepository,
            EmpresaRepository empresaRepository,
            OferenteRepository oferenteRepository,
            PasswordEncoder passwordEncoder,
            CaracteristicaRepository caracteristicaRepository) {

        return args -> {

            Usuario usuarioAdmin = usuarioRepository.findByCorreo("admin@admin.com")
                    .orElseGet(() -> {
                        Usuario nuevoAdmin = Usuario.builder()
                                .correo("admin@admin.com")
                                .password(passwordEncoder.encode("1234"))
                                .rol("ADMIN")
                                .activo(true)
                                .estado("APROBADO")
                                .comentarioRevision(null)
                                .build();

                        return usuarioRepository.save(nuevoAdmin);
                    });

            administradorRepository.findByUsuarioCorreo("admin@admin.com")
                    .ifPresentOrElse(admin -> {
                        if (admin.getIdentificacion() == null || admin.getIdentificacion().isBlank()) {
                            admin.setIdentificacion("admin");
                            administradorRepository.save(admin);
                        }
                    }, () -> {
                        Administrador admin = Administrador.builder()
                                .identificacion("admin")
                                .nombre("Administrador Principal")
                                .usuario(usuarioAdmin)
                                .build();

                        administradorRepository.save(admin);
                    });

            if (usuarioRepository.findByCorreo("empresa@empresa.com").isEmpty()) {
                Usuario usuarioEmpresa = Usuario.builder()
                        .correo("empresa@empresa.com")
                        .password(passwordEncoder.encode("1234"))
                        .rol("EMPRESA")
                        .activo(true)
                        .estado("APROBADO")
                        .comentarioRevision(null)
                        .build();

                usuarioRepository.save(usuarioEmpresa);

                Empresa empresa = Empresa.builder()
                        .nombre("Empresa Demo")
                        .telefono("2222-2222")
                        .localizacion("Heredia")
                        .descripcion("Empresa de prueba")
                        .usuario(usuarioEmpresa)
                        .build();

                empresaRepository.save(empresa);
            }

            if (usuarioRepository.findByCorreo("oferente@oferente.com").isEmpty()) {
                Usuario usuarioOferente = Usuario.builder()
                        .correo("oferente@oferente.com")
                        .password(passwordEncoder.encode("1234"))
                        .rol("OFERENTE")
                        .activo(true)
                        .estado("APROBADO")
                        .comentarioRevision(null)
                        .build();

                usuarioRepository.save(usuarioOferente);

                Oferente oferente = Oferente.builder()
                        .identificacion("123456789")
                        .nombre("Juan")
                        .apellido("Pérez")
                        .telefono("8888-8888")
                        .residencia("Alajuela")
                        .nacionalidad("Costarricense")
                        .usuario(usuarioOferente)
                        .build();

                oferenteRepository.save(oferente);
            }

            if (caracteristicaRepository.count() == 0) {
                caracteristicaRepository.save(Caracteristica.builder().nombre("Java").build());
                caracteristicaRepository.save(Caracteristica.builder().nombre("C#").build());
                caracteristicaRepository.save(Caracteristica.builder().nombre("MySQL").build());
                caracteristicaRepository.save(Caracteristica.builder().nombre("Oracle").build());
                caracteristicaRepository.save(Caracteristica.builder().nombre("HTML").build());
                caracteristicaRepository.save(Caracteristica.builder().nombre("CSS").build());
            }
        };
    }
}