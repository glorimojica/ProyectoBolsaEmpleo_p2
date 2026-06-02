package cr.ac.una.proyectobolsaempleo.controller;

import cr.ac.una.proyectobolsaempleo.model.Caracteristica;
import cr.ac.una.proyectobolsaempleo.model.Oferente;
import cr.ac.una.proyectobolsaempleo.model.OferenteCaracteristica;
import cr.ac.una.proyectobolsaempleo.repository.CaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteCaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/oferente")
public class OferenteController {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private OferenteCaracteristicaRepository oferenteCaracteristicaRepository;

    @GetMapping("/dashboard")
    public String dashboardOferente() {
        return "oferente/dashboard";
    }

    @GetMapping("/habilidades")
    public String listarHabilidades(Authentication authentication, Model model) {
        String correo = authentication.getName();
        Oferente oferente = oferenteRepository.findByUsuarioCorreo(correo).orElse(null);

        if (oferente == null || !oferente.getUsuario().isActivo()) {
            return "redirect:/login";
        }

        List<OferenteCaracteristica> habilidades =
                oferenteCaracteristicaRepository.findByOferenteId(oferente.getId());

        model.addAttribute("habilidades", habilidades);
        return "oferente/habilidades";
    }

    @GetMapping("/habilidades/nueva")
    public String mostrarFormularioNuevaHabilidad(Authentication authentication, Model model) {
        String correo = authentication.getName();
        Oferente oferente = oferenteRepository.findByUsuarioCorreo(correo).orElse(null);

        if (oferente == null || !oferente.getUsuario().isActivo()) {
            return "redirect:/login";
        }

        model.addAttribute("caracteristicas", caracteristicaRepository.findByHijasIsEmptyOrderByNombreAsc());
        return "oferente/nueva-habilidad";
    }

    @PostMapping("/habilidades")
    public String guardarHabilidad(Authentication authentication,
                                   @RequestParam("caracteristicaId") Long caracteristicaId,
                                   @RequestParam("nivel") Integer nivel,
                                   Model model) {

        String correo = authentication.getName();
        Oferente oferente = oferenteRepository.findByUsuarioCorreo(correo).orElse(null);

        if (oferente == null || !oferente.getUsuario().isActivo()) {
            return "redirect:/login";
        }

        if (nivel == null || nivel < 1 || nivel > 5) {
            model.addAttribute("error", "El nivel debe estar entre 1 y 5.");
            model.addAttribute("caracteristicas", caracteristicaRepository.findByHijasIsEmptyOrderByNombreAsc());
            return "oferente/nueva-habilidad";
        }

        if (oferenteCaracteristicaRepository.existsByOferenteIdAndCaracteristicaId(oferente.getId(), caracteristicaId)) {
            model.addAttribute("error", "Esa habilidad ya fue agregada.");
            model.addAttribute("caracteristicas", caracteristicaRepository.findByHijasIsEmptyOrderByNombreAsc());
            return "oferente/nueva-habilidad";
        }

        Caracteristica caracteristica = caracteristicaRepository.findById(caracteristicaId).orElse(null);

        if (caracteristica == null) {
            model.addAttribute("error", "La característica seleccionada no existe.");
            model.addAttribute("caracteristicas", caracteristicaRepository.findByHijasIsEmptyOrderByNombreAsc());
            return "oferente/nueva-habilidad";
        }

        if (!caracteristica.esHoja()) {
            model.addAttribute("error", "Debe seleccionar una característica específica, no una categoría general.");
            model.addAttribute("caracteristicas", caracteristicaRepository.findByHijasIsEmptyOrderByNombreAsc());
            return "oferente/nueva-habilidad";
        }

        OferenteCaracteristica habilidad = OferenteCaracteristica.builder()
                .oferente(oferente)
                .caracteristica(caracteristica)
                .nivel(nivel)
                .build();

        oferenteCaracteristicaRepository.save(habilidad);

        return "redirect:/oferente/habilidades";
    }

    @PostMapping("/habilidades/{id}/eliminar")
    public String eliminarHabilidad(@PathVariable Long id, Authentication authentication) {
        String correo = authentication.getName();
        Oferente oferente = oferenteRepository.findByUsuarioCorreo(correo).orElse(null);

        if (oferente == null || !oferente.getUsuario().isActivo()) {
            return "redirect:/login";
        }

        OferenteCaracteristica habilidad = oferenteCaracteristicaRepository
                .findByIdAndOferenteId(id, oferente.getId())
                .orElse(null);

        if (habilidad != null) {
            oferenteCaracteristicaRepository.delete(habilidad);
        }

        return "redirect:/oferente/habilidades";
    }

    @GetMapping("/cv")
    public String formularioCv(Authentication authentication, Model model) {
        String correo = authentication.getName();
        Oferente oferente = oferenteRepository.findByUsuarioCorreo(correo).orElse(null);

        if (oferente == null || !oferente.getUsuario().isActivo()) {
            return "redirect:/login";
        }

        model.addAttribute("oferente", oferente);
        return "oferente/cv";
    }

    @PostMapping("/cv")
    public String subirCv(Authentication authentication,
                          @RequestParam("archivo") MultipartFile archivo,
                          Model model) throws IOException {

        String correo = authentication.getName();
        Oferente oferente = oferenteRepository.findByUsuarioCorreo(correo).orElse(null);

        if (oferente == null || !oferente.getUsuario().isActivo()) {
            return "redirect:/login";
        }

        if (archivo.isEmpty()) {
            model.addAttribute("error", "Debe seleccionar un archivo PDF.");
            model.addAttribute("oferente", oferente);
            return "oferente/cv";
        }

        String nombreOriginal = archivo.getOriginalFilename();
        boolean extensionValida = nombreOriginal != null && nombreOriginal.toLowerCase().endsWith(".pdf");
        boolean tipoValido = archivo.getContentType() != null &&
                archivo.getContentType().equalsIgnoreCase("application/pdf");

        if (!extensionValida && !tipoValido) {
            model.addAttribute("error", "Solo se permite subir archivos PDF.");
            model.addAttribute("oferente", oferente);
            return "oferente/cv";
        }

        Path carpeta = Paths.get(uploadDir);
        if (!Files.exists(carpeta)) {
            Files.createDirectories(carpeta);
        }

        String nombreArchivo = "cv_oferente_" + oferente.getId() + ".pdf";
        Path ruta = carpeta.resolve(nombreArchivo);

        Files.copy(archivo.getInputStream(), ruta, StandardCopyOption.REPLACE_EXISTING);

        oferente.setCvNombreArchivo(nombreArchivo);
        oferenteRepository.save(oferente);

        return "redirect:/oferente/cv";
    }

    @GetMapping("/cv/ver")
    public ResponseEntity<byte[]> verCvPropio(Authentication authentication) throws IOException {
        String correo = authentication.getName();
        Oferente oferente = oferenteRepository.findByUsuarioCorreo(correo).orElse(null);

        if (oferente == null || oferente.getCvNombreArchivo() == null) {
            return ResponseEntity.notFound().build();
        }

        Path ruta = Paths.get(uploadDir).resolve(oferente.getCvNombreArchivo());

        if (!Files.exists(ruta)) {
            return ResponseEntity.notFound().build();
        }

        byte[] contenido = Files.readAllBytes(ruta);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + oferente.getCvNombreArchivo() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(contenido);
    }
}