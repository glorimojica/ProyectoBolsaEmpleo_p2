package cr.ac.una.proyectobolsaempleo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaController {

    @GetMapping({
            "/",
            "/buscar",
            "/login",
            "/registro/empresa",
            "/registro/oferente",

            "/admin/dashboard",
            "/admin/empresas",
            "/admin/oferentes",
            "/admin/caracteristicas",

            "/empresa/dashboard",
            "/empresa/puestos",
            "/empresa/puestos/nuevo",
            "/empresa/puestos/{id}/requisitos",
            "/empresa/puestos/{id}/candidatos",

            "/oferente/dashboard",
            "/oferente/perfil",
            "/oferente/habilidades",
            "/oferente/cv",
            "/oferente/puestos"
    })
    public String forwardReactRoutes() {
        return "forward:/index.html";
    }
}