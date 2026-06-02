package cr.ac.una.proyectobolsaempleo.controller;

import cr.ac.una.proyectobolsaempleo.repository.CaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.PuestoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private PuestoRepository puestoRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("puestos", puestoRepository.findTop5ByPublicoTrueAndActivoTrueOrderByIdDesc());
        return "public/index";
    }

    @GetMapping("/buscar-puestos")
    public String buscarPuestos(@RequestParam(value = "caracteristicaId", required = false) Long caracteristicaId,
                                @RequestParam(value = "nivel", required = false) Integer nivel,
                                Model model) {

        if (nivel != null && (nivel < 1 || nivel > 5)) {
            nivel = null;
        }

        model.addAttribute("caracteristicas", caracteristicaRepository.findByHijasIsEmptyOrderByNombreAsc());
        model.addAttribute("puestos", puestoRepository.buscarPublicos(caracteristicaId, nivel));
        model.addAttribute("caracteristicaId", caracteristicaId);
        model.addAttribute("nivel", nivel);

        return "public/buscar-puestos";
    }
}