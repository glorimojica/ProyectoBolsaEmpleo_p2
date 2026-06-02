package cr.ac.una.proyectobolsaempleo.controller;

import cr.ac.una.proyectobolsaempleo.model.Empresa;
import cr.ac.una.proyectobolsaempleo.model.Oferente;
import cr.ac.una.proyectobolsaempleo.model.Usuario;
import cr.ac.una.proyectobolsaempleo.repository.EmpresaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteRepository;
import cr.ac.una.proyectobolsaempleo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/registro")
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/empresa")
    public String mostrarRegistroEmpresa() {
        return "public/registro-empresa";
    }

    @PostMapping("/empresa")
    public String registrarEmpresa(
            @RequestParam("nombre") String nombre,
            @RequestParam("localizacion") String localizacion,
            @RequestParam("correo") String correo,
            @RequestParam("telefono") String telefono,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("password") String password,
            Model model) {

        if (usuarioRepository.existsByCorreo(correo)) {
            model.addAttribute("error", "El correo ya está registrado.");
            return "public/registro-empresa";
        }

        Usuario usuario = Usuario.builder()
                .correo(correo)
                .password(passwordEncoder.encode(password))
                .rol("EMPRESA")
                .activo(false)
                .estado("PENDIENTE")
                .comentarioRevision(null)
                .build();

        usuarioRepository.save(usuario);

        Empresa empresa = Empresa.builder()
                .nombre(nombre)
                .localizacion(localizacion)
                .telefono(telefono)
                .descripcion(descripcion)
                .usuario(usuario)
                .build();

        empresaRepository.save(empresa);

        model.addAttribute("mensaje", "Registro de empresa enviado. Pendiente de aprobación por administración.");
        return "auth/login";
    }

    @GetMapping("/oferente")
    public String mostrarRegistroOferente() {
        return "public/registro-oferente";
    }

    @PostMapping("/oferente")
    public String registrarOferente(
            @RequestParam("identificacion") String identificacion,
            @RequestParam("nombre") String nombre,
            @RequestParam("apellido") String apellido,
            @RequestParam("nacionalidad") String nacionalidad,
            @RequestParam("telefono") String telefono,
            @RequestParam("correo") String correo,
            @RequestParam("residencia") String residencia,
            @RequestParam("password") String password,
            Model model) {

        if (usuarioRepository.existsByCorreo(correo)) {
            model.addAttribute("error", "El correo ya está registrado.");
            return "public/registro-oferente";
        }

        Usuario usuario = Usuario.builder()
                .correo(correo)
                .password(passwordEncoder.encode(password))
                .rol("OFERENTE")
                .activo(false)
                .estado("PENDIENTE")
                .comentarioRevision(null)
                .build();

        usuarioRepository.save(usuario);

        Oferente oferente = Oferente.builder()
                .identificacion(identificacion)
                .nombre(nombre)
                .apellido(apellido)
                .nacionalidad(nacionalidad)
                .telefono(telefono)
                .residencia(residencia)
                .usuario(usuario)
                .build();

        oferenteRepository.save(oferente);

        model.addAttribute("mensaje", "Registro de oferente enviado. Pendiente de aprobación por administración.");
        return "auth/login";
    }
}