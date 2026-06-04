package cr.ac.una.proyectobolsaempleo.controller;

import cr.ac.una.proyectobolsaempleo.dto.auth.ApiMessageResponse;
import cr.ac.una.proyectobolsaempleo.dto.empresa.CandidatoResponse;
import cr.ac.una.proyectobolsaempleo.dto.empresa.PuestoEmpresaRequest;
import cr.ac.una.proyectobolsaempleo.dto.empresa.PuestoEmpresaResponse;
import cr.ac.una.proyectobolsaempleo.dto.empresa.RequisitoPuestoRequest;
import cr.ac.una.proyectobolsaempleo.dto.empresa.RequisitoPuestoResponse;
import cr.ac.una.proyectobolsaempleo.service.EmpresaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/empresa")
@RequiredArgsConstructor
public class EmpresaApiController {

    private final EmpresaService empresaService;

    @GetMapping("/puestos")
    public ResponseEntity<List<PuestoEmpresaResponse>> listarMisPuestos() {
        return ResponseEntity.ok(empresaService.listarMisPuestos());
    }

    @GetMapping("/puestos/{id}")
    public ResponseEntity<PuestoEmpresaResponse> obtenerMiPuesto(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.obtenerMiPuesto(id));
    }

    @PostMapping("/puestos")
    public ResponseEntity<PuestoEmpresaResponse> crearPuesto(
            @Valid @RequestBody PuestoEmpresaRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaService.crearPuesto(request));
    }

    @PutMapping("/puestos/{id}/desactivar")
    public ResponseEntity<ApiMessageResponse> desactivarPuesto(@PathVariable Long id) {
        empresaService.desactivarPuesto(id);
        return ResponseEntity.ok(new ApiMessageResponse("Puesto desactivado correctamente."));
    }

    @PostMapping("/puestos/{id}/requisitos")
    public ResponseEntity<RequisitoPuestoResponse> agregarRequisito(
            @PathVariable Long id,
            @Valid @RequestBody RequisitoPuestoRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(empresaService.agregarOActualizarRequisito(id, request));
    }

    @GetMapping("/puestos/{id}/candidatos")
    public ResponseEntity<List<CandidatoResponse>> buscarCandidatos(@PathVariable Long id) {
        return ResponseEntity.ok(empresaService.buscarCandidatos(id));
    }
}