package cr.ac.una.proyectobolsaempleo.controller;

import cr.ac.una.proyectobolsaempleo.dto.admin.CaracteristicaRequest;
import cr.ac.una.proyectobolsaempleo.dto.admin.CaracteristicaResponse;
import cr.ac.una.proyectobolsaempleo.dto.auth.ApiMessageResponse;
import cr.ac.una.proyectobolsaempleo.dto.admin.UsuarioPendienteResponse;
import cr.ac.una.proyectobolsaempleo.service.AdminService;
import cr.ac.una.proyectobolsaempleo.service.CaracteristicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminApiController {

    private final AdminService adminService;
    private final CaracteristicaService caracteristicaService;

    @GetMapping("/empresas/pendientes")
    public ResponseEntity<List<UsuarioPendienteResponse>> listarEmpresasPendientes() {
        return ResponseEntity.ok(adminService.listarEmpresasPendientes());
    }

    @PutMapping("/empresas/{id}/aprobar")
    public ResponseEntity<ApiMessageResponse> aprobarEmpresa(@PathVariable Long id) {
        adminService.aprobarEmpresa(id);
        return ResponseEntity.ok(new ApiMessageResponse("Empresa aprobada correctamente."));
    }

    @PutMapping("/empresas/{id}/rechazar")
    public ResponseEntity<ApiMessageResponse> rechazarEmpresa(@PathVariable Long id) {
        adminService.rechazarEmpresa(id);
        return ResponseEntity.ok(new ApiMessageResponse("Empresa rechazada correctamente."));
    }

    @GetMapping("/oferentes/pendientes")
    public ResponseEntity<List<UsuarioPendienteResponse>> listarOferentesPendientes() {
        return ResponseEntity.ok(adminService.listarOferentesPendientes());
    }

    @PutMapping("/oferentes/{id}/aprobar")
    public ResponseEntity<ApiMessageResponse> aprobarOferente(@PathVariable Long id) {
        adminService.aprobarOferente(id);
        return ResponseEntity.ok(new ApiMessageResponse("Oferente aprobado correctamente."));
    }

    @PutMapping("/oferentes/{id}/rechazar")
    public ResponseEntity<ApiMessageResponse> rechazarOferente(@PathVariable Long id) {
        adminService.rechazarOferente(id);
        return ResponseEntity.ok(new ApiMessageResponse("Oferente rechazado correctamente."));
    }

    @GetMapping("/caracteristicas")
    public ResponseEntity<List<CaracteristicaResponse>> listarCaracteristicas() {
        return ResponseEntity.ok(caracteristicaService.listarArbol());
    }

    @GetMapping("/caracteristicas/planas")
    public ResponseEntity<List<CaracteristicaResponse>> listarCaracteristicasPlanas() {
        return ResponseEntity.ok(caracteristicaService.listarTodasPlanas());
    }

    @PostMapping("/caracteristicas")
    public ResponseEntity<CaracteristicaResponse> crearCaracteristica(
            @Valid @RequestBody CaracteristicaRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(caracteristicaService.crear(request));
    }

    @PutMapping("/caracteristicas/{id}")
    public ResponseEntity<CaracteristicaResponse> actualizarCaracteristica(
            @PathVariable Long id,
            @Valid @RequestBody CaracteristicaRequest request) {

        return ResponseEntity.ok(caracteristicaService.actualizar(id, request));
    }

    @DeleteMapping("/caracteristicas/{id}")
    public ResponseEntity<ApiMessageResponse> eliminarCaracteristica(@PathVariable Long id) {
        caracteristicaService.eliminar(id);
        return ResponseEntity.ok(new ApiMessageResponse("Característica eliminada correctamente."));
    }
}