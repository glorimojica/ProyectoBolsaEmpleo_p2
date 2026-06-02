package cr.ac.una.proyectobolsaempleo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/redirigir")
    public String redirigir(Authentication authentication) {
        if (authentication != null && authentication.getAuthorities() != null) {
            boolean esAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

            boolean esEmpresa = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_EMPRESA"));

            boolean esOferente = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_OFERENTE"));

            if (esAdmin) {
                return "redirect:/admin/dashboard";
            }
            if (esEmpresa) {
                return "redirect:/empresa/dashboard";
            }
            if (esOferente) {
                return "redirect:/oferente/dashboard";
            }
        }

        return "redirect:/login?error";
    }
}