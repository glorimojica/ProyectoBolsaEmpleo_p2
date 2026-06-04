package cr.ac.una.proyectobolsaempleo.controller;

import cr.ac.una.proyectobolsaempleo.dto.auth.ApiMessageResponse;
import cr.ac.una.proyectobolsaempleo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminApiController {

    private final AdminService adminService;

    @GetMapping("/empresas/pendientes")
    public ResponseEntity<?> listarEmpresasPendientes() {
        return ResponseEntity.ok(adminService.listarEmpresasPendientes());
    }

    @PutMapping("/empresas/{id}/aprobar")
    public ResponseEntity<?> aprobarEmpresa(@PathVariable Long id) {
        adminService.aprobarEmpresa(id);
        return ResponseEntity.ok(new ApiMessageResponse("Empresa aprobada correctamente"));
    }

    @PutMapping("/empresas/{id}/rechazar")
    public ResponseEntity<?> rechazarEmpresa(@PathVariable Long id) {
        adminService.rechazarEmpresa(id);
        return ResponseEntity.ok(new ApiMessageResponse("Empresa rechazada correctamente"));
    }

    @GetMapping("/oferentes/pendientes")
    public ResponseEntity<?> listarOferentesPendientes() {
        return ResponseEntity.ok(adminService.listarOferentesPendientes());
    }

    @PutMapping("/oferentes/{id}/aprobar")
    public ResponseEntity<?> aprobarOferente(@PathVariable Long id) {
        adminService.aprobarOferente(id);
        return ResponseEntity.ok(new ApiMessageResponse("Oferente aprobado correctamente"));
    }

    @PutMapping("/oferentes/{id}/rechazar")
    public ResponseEntity<?> rechazarOferente(@PathVariable Long id) {
        adminService.rechazarOferente(id);
        return ResponseEntity.ok(new ApiMessageResponse("Oferente rechazado correctamente"));
    }
}