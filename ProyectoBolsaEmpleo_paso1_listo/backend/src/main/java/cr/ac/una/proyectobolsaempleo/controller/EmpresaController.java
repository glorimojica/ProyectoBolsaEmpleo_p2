package cr.ac.una.proyectobolsaempleo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import cr.ac.una.proyectobolsaempleo.model.Empresa;
import cr.ac.una.proyectobolsaempleo.model.Puesto;
import cr.ac.una.proyectobolsaempleo.repository.EmpresaRepository;
import cr.ac.una.proyectobolsaempleo.repository.PuestoRepository;
import cr.ac.una.proyectobolsaempleo.model.Caracteristica;
import cr.ac.una.proyectobolsaempleo.model.PuestoCaracteristica;
import cr.ac.una.proyectobolsaempleo.repository.CaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.PuestoCaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.model.CandidatoMatch;
import cr.ac.una.proyectobolsaempleo.model.Oferente;
import cr.ac.una.proyectobolsaempleo.model.OferenteCaracteristica;
import cr.ac.una.proyectobolsaempleo.repository.OferenteCaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteRepository;
import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

@Controller
@RequestMapping("/empresa")
public class EmpresaController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PuestoRepository puestoRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @Autowired
    private PuestoCaracteristicaRepository puestoCaracteristicaRepository;

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private OferenteCaracteristicaRepository oferenteCaracteristicaRepository;

    @GetMapping("/dashboard")
    public String dashboardEmpresa() {
        return "empresa/dashboard";
    }

    @GetMapping("/puestos")
    public String listarPuestos(Authentication authentication, Model model) {
        String correo = authentication.getName();
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo).orElse(null);
        if (empresa == null) {
            return "redirect:/login";
        }
        model.addAttribute("puestos", puestoRepository.findByEmpresaId(empresa.getId()));
        return "empresa/puestos";
    }

    @GetMapping("/puestos/nuevo")
    public String mostrarFormularioNuevoPuesto(Model model) {
        model.addAttribute("puesto", new Puesto());
        return "empresa/crearPuesto";
    }
    @PostMapping("/puestos")
    public String guardarPuesto(@ModelAttribute Puesto puesto, Authentication authentication) {
        String correo = authentication.getName();
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo).orElse(null);

        if (empresa == null || !empresa.getUsuario().isActivo()) {
            return "redirect:/login";
        }

        puesto.setEmpresa(empresa);
        puesto.setActivo(true);
        puesto.setFechaCreacion(LocalDateTime.now());

        puestoRepository.save(puesto);
        return "redirect:/empresa/puestos";
    }

    @GetMapping("/puestos/{id}")
    public String verDetallePuesto(@PathVariable Long id, Authentication authentication, Model model) {
        String correo = authentication.getName();
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo).orElse(null);
        if (empresa == null) {
            return "redirect:/login";
        }
        Puesto puesto = puestoRepository.findByIdAndEmpresaId(id, empresa.getId()).orElse(null);
        if (puesto == null) {
            return "redirect:/empresa/puestos";
        }
        model.addAttribute("puesto", puesto);
        return "empresa/detallePuesto";
    }

    @PostMapping("/puestos/{id}/toggle")
    public String cambiarEstado(@PathVariable Long id, Authentication authentication) {
        String correo = authentication.getName();
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo).orElse(null);
        if (empresa == null) {
            return "redirect:/login";
        }
        Puesto puesto = puestoRepository.findByIdAndEmpresaId(id, empresa.getId()).orElse(null);
        if (puesto != null) {
            puesto.setActivo(!puesto.isActivo());
            puestoRepository.save(puesto);
        }
        return "redirect:/empresa/puestos";
    }

    @GetMapping("/puestos/{id}/requisitos")
    public String verRequisitos(@PathVariable Long id, Authentication authentication, Model model) {
        String correo = authentication.getName();
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo).orElse(null);
        if (empresa == null) {
            return "redirect:/login";
        }
        Puesto puesto = puestoRepository.findByIdAndEmpresaId(id, empresa.getId()).orElse(null);
        if (puesto == null) {
            return "redirect:/empresa/puestos";
        }
        model.addAttribute("puesto", puesto);
        model.addAttribute("requisitos", puestoCaracteristicaRepository.findByPuestoId(id));
        model.addAttribute("caracteristicas", caracteristicaRepository.findAll());

        return "empresa/requisitosPuesto";
    }

    @PostMapping("/puestos/{id}/requisitos")
    public String agregarRequisito(@PathVariable Long id, @RequestParam("caracteristicaId") Long caracteristicaId, @RequestParam("nivel") Integer nivel, Authentication authentication, Model model) {
        String correo = authentication.getName();
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo).orElse(null);
        if (empresa == null) {
            return "redirect:/login";
        }
        Puesto puesto = puestoRepository.findByIdAndEmpresaId(id, empresa.getId()).orElse(null);
        if (puesto == null) {
            return "redirect:/empresa/puestos";
        }
        if (puestoCaracteristicaRepository.existsByPuestoIdAndCaracteristicaId(id, caracteristicaId)) {
            model.addAttribute("error", "Ese requisito ya fue agregado.");
            model.addAttribute("puesto", puesto);
            model.addAttribute("requisitos", puestoCaracteristicaRepository.findByPuestoId(id));
            model.addAttribute("caracteristicas", caracteristicaRepository.findAll());
            return "empresa/requisitosPuesto";
        }
        Caracteristica caracteristica = caracteristicaRepository.findById(caracteristicaId).orElse(null);
        if (caracteristica == null) {
            return "redirect:/empresa/puestos/" + id + "/requisitos";
        }
        PuestoCaracteristica requisito = PuestoCaracteristica.builder().puesto(puesto).caracteristica(caracteristica).nivel(nivel).build();
        puestoCaracteristicaRepository.save(requisito);

        return "redirect:/empresa/puestos/" + id + "/requisitos";
    }

    @PostMapping("/puestos/{puestoId}/requisitos/{reqId}/eliminar")
    public String eliminarRequisito(@PathVariable Long puestoId, @PathVariable Long reqId, Authentication authentication) {
        String correo = authentication.getName();
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo).orElse(null);
        if (empresa == null) {
            return "redirect:/login";
        }
        Puesto puesto = puestoRepository.findByIdAndEmpresaId(puestoId, empresa.getId()).orElse(null);
        if (puesto == null) {
            return "redirect:/empresa/puestos";
        }
        PuestoCaracteristica requisito = puestoCaracteristicaRepository.findByIdAndPuestoId(reqId, puestoId).orElse(null);
        if (requisito != null) {
            puestoCaracteristicaRepository.delete(requisito);
        }

        return "redirect:/empresa/puestos/" + puestoId + "/requisitos";
    }

    @GetMapping("/puestos/{id}/candidatos")
    public String verCantidatos(@PathVariable Long id, Authentication authentication, Model model) {
        String correo = authentication.getName();
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo).orElse(null);
        if (empresa == null) {
            return "redirect:/login";
        }
        Puesto puesto = puestoRepository.findByIdAndEmpresaId(id, empresa.getId()).orElse(null);
        if (puesto == null) {
            return "redirect:/empresa/puestos";
        }
        List<PuestoCaracteristica> requisitos = puestoCaracteristicaRepository.findByPuestoId(puesto.getId());
        List<Oferente> oferentes = oferenteRepository.findAll();
        List<CandidatoMatch> candidatos = new ArrayList<>();
        for (Oferente oferente : oferentes) {
            if (oferente.getUsuario() == null || !oferente.getUsuario().isActivo()) {
                continue;
            }
            List<OferenteCaracteristica> habilidades = oferenteCaracteristicaRepository.findByOferenteId(oferente.getId());
            int cumplidos = calcularRequisitosCumplidos(requisitos, habilidades);
            int total = requisitos.size();
            int porcentaje;
            if (total==0){
                porcentaje = 100;
            }else{
                porcentaje = (cumplidos*100)/total;
            }
            candidatos.add(new CandidatoMatch(oferente, porcentaje, cumplidos, total));
        }
        candidatos.sort(Comparator.comparingInt(CandidatoMatch::getPorcentajeMatch).reversed());
        model.addAttribute("puesto", puesto);
        model.addAttribute("candidatos",candidatos);
        return "empresa/candidatosPuesto";
    }

    private int calcularRequisitosCumplidos(List<PuestoCaracteristica>requisitos, List<OferenteCaracteristica> habilidades) {
        int cumplidos = 0;
        for (PuestoCaracteristica requisito : requisitos) {
            for (OferenteCaracteristica habilidad : habilidades) {
                boolean mismaCaracteristica=requisito.getCaracteristica().getId().equals(habilidad.getCaracteristica().getId());
                boolean nivelSuficiente = habilidad.getNivel() >= requisito.getNivel();
                if (mismaCaracteristica && nivelSuficiente){
                    cumplidos++;
                    break;
                }
            }
        }
        return cumplidos;
    }

    @GetMapping("/candidatos/{id}")
    public String verDetalleCandidato(@PathVariable Long id, Authentication authentication, Model model) {
        String correo = authentication.getName();
        Empresa empresa = empresaRepository.findByUsuarioCorreo(correo).orElse(null);
        if (empresa == null) {
            return "redirect:/login";
        }
        Oferente oferente = oferenteRepository.findById(id).orElse(null);
        if (oferente == null || oferente.getUsuario() == null || !oferente.getUsuario().isActivo()) {
            return "redirect:/empresa/puestos";
        }
        List<OferenteCaracteristica> habilidades = oferenteCaracteristicaRepository.findByOferenteId(oferente.getId());
        model.addAttribute("oferente", oferente);
        model.addAttribute("habilidades", habilidades);
        return "empresa/detalleCandidato";
    }
}


