package cr.ac.una.proyectobolsaempleo.controller;

import cr.ac.una.proyectobolsaempleo.dto.publico.CaracteristicaResponse;
import cr.ac.una.proyectobolsaempleo.dto.publico.PuestoPublicoResponse;
import cr.ac.una.proyectobolsaempleo.service.impl.PublicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicApiController {

    private final PublicService publicService;

    @GetMapping("/puestos/recientes")
    public ResponseEntity<List<PuestoPublicoResponse>> listarPuestosRecientes() {
        return ResponseEntity.ok(publicService.listarPuestosRecientes());
    }

    @GetMapping("/puestos")
    public ResponseEntity<List<PuestoPublicoResponse>> buscarPuestosPublicos(
            @RequestParam(value = "caracteristicaId", required = false) Long caracteristicaId,
            @RequestParam(value = "nivel", required = false) Integer nivel) {

        return ResponseEntity.ok(publicService.buscarPuestosPublicos(caracteristicaId, nivel));
    }

    @GetMapping("/puestos/{id}")
    public ResponseEntity<PuestoPublicoResponse> obtenerDetallePuesto(@PathVariable Long id) {
        return ResponseEntity.ok(publicService.obtenerDetallePuesto(id));
    }

    @GetMapping("/caracteristicas")
    public ResponseEntity<List<CaracteristicaResponse>> listarCaracteristicas() {
        return ResponseEntity.ok(publicService.listarArbolCaracteristicas());
    }
}
