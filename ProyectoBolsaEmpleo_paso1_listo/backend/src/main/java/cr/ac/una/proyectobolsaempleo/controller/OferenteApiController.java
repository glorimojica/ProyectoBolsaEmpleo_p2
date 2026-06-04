package cr.ac.una.proyectobolsaempleo.controller;

import cr.ac.una.proyectobolsaempleo.dto.auth.ApiMessageResponse;
import cr.ac.una.proyectobolsaempleo.dto.oferente.HabilidadRequest;
import cr.ac.una.proyectobolsaempleo.dto.oferente.HabilidadResponse;
import cr.ac.una.proyectobolsaempleo.dto.oferente.OferentePerfilRequest;
import cr.ac.una.proyectobolsaempleo.dto.oferente.OferentePerfilResponse;
import cr.ac.una.proyectobolsaempleo.dto.publico.PuestoPublicoResponse;
import cr.ac.una.proyectobolsaempleo.service.OferenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/v1/oferente")
@RequiredArgsConstructor
public class OferenteApiController {

    private final OferenteService oferenteService;

    @GetMapping("/perfil")
    public ResponseEntity<OferentePerfilResponse> obtenerPerfil() {
        return ResponseEntity.ok(oferenteService.obtenerPerfil());
    }

    @PutMapping("/perfil")
    public ResponseEntity<OferentePerfilResponse> actualizarPerfil(
            @Valid @RequestBody OferentePerfilRequest request) {

        return ResponseEntity.ok(oferenteService.actualizarPerfil(request));
    }

    @GetMapping("/habilidades")
    public ResponseEntity<List<HabilidadResponse>> listarHabilidades() {
        return ResponseEntity.ok(oferenteService.listarHabilidades());
    }

    @PostMapping("/habilidades")
    public ResponseEntity<HabilidadResponse> agregarOActualizarHabilidad(
            @Valid @RequestBody HabilidadRequest request) {

        return ResponseEntity.ok(oferenteService.agregarOActualizarHabilidad(request));
    }

    @DeleteMapping("/habilidades/{id}")
    public ResponseEntity<ApiMessageResponse> eliminarHabilidad(@PathVariable Long id) {
        oferenteService.eliminarHabilidad(id);
        return ResponseEntity.ok(new ApiMessageResponse("Habilidad eliminada correctamente."));
    }

    @PostMapping(value = "/cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OferentePerfilResponse> subirCv(
            @RequestParam("archivo") MultipartFile archivo) {

        return ResponseEntity.ok(oferenteService.subirCv(archivo));
    }

    @GetMapping("/cv/archivo")
    public ResponseEntity<Resource> verMiCv() throws MalformedURLException {
        Path ruta = oferenteService.obtenerRutaCvAutenticado();
        Resource recurso = new UrlResource(ruta.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + ruta.getFileName() + "\"")
                .body(recurso);
    }

    @GetMapping("/puestos")
    public ResponseEntity<List<PuestoPublicoResponse>> listarPuestosDisponibles() {
        return ResponseEntity.ok(oferenteService.listarPuestosDisponibles());
    }
}
