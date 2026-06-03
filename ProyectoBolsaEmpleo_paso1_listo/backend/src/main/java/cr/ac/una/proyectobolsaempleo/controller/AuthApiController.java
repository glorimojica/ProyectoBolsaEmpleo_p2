package cr.ac.una.proyectobolsaempleo.controller;

import cr.ac.una.proyectobolsaempleo.dto.auth.ApiMessageResponse;
import cr.ac.una.proyectobolsaempleo.dto.auth.AuthResponse;
import cr.ac.una.proyectobolsaempleo.dto.auth.LoginRequest;
import cr.ac.una.proyectobolsaempleo.dto.auth.MeResponse;
import cr.ac.una.proyectobolsaempleo.dto.auth.RegisterEmpresaRequest;
import cr.ac.una.proyectobolsaempleo.dto.auth.RegisterOferenteRequest;
import cr.ac.una.proyectobolsaempleo.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/register/empresa")
    public ResponseEntity<ApiMessageResponse> registrarEmpresa(
            @Valid @RequestBody RegisterEmpresaRequest request) {

        ApiMessageResponse response = authService.registrarEmpresa(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register/oferente")
    public ResponseEntity<ApiMessageResponse> registrarOferente(
            @Valid @RequestBody RegisterOferenteRequest request) {

        ApiMessageResponse response = authService.registrarOferente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponse> me(Authentication authentication) {
        MeResponse response = authService.me(authentication.getName());
        return ResponseEntity.ok(response);
    }
}
