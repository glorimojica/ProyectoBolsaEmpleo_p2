package cr.ac.una.proyectobolsaempleo.controller;

import cr.ac.una.proyectobolsaempleo.model.Caracteristica;
import cr.ac.una.proyectobolsaempleo.model.Usuario;
import cr.ac.una.proyectobolsaempleo.repository.CaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.EmpresaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteRepository;
import cr.ac.una.proyectobolsaempleo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import cr.ac.una.proyectobolsaempleo.model.Puesto;
import cr.ac.una.proyectobolsaempleo.repository.PuestoRepository;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private OferenteRepository oferenteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CaracteristicaRepository caracteristicaRepository;

    @GetMapping("/dashboard")
    public String dashboardAdmin() {
        return "admin/dashboard";
    }

    @Autowired
    private PuestoRepository puestoRepository;

    @GetMapping("/empresas-pendientes")
    public String verEmpresasPendientes(Model model) {
        model.addAttribute("empresas", empresaRepository.findByUsuarioEstado("PENDIENTE"));
        return "admin/empresas-pendientes";
    }

    @GetMapping("/oferentes-pendientes")
    public String verOferentesPendientes(Model model) {
        model.addAttribute("oferentes", oferenteRepository.findByUsuarioEstado("PENDIENTE"));
        return "admin/oferentes-pendientes";
    }

    @PostMapping("/aprobar-empresa/{usuarioId}")
    public String aprobarEmpresa(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario != null) {
            usuario.setActivo(true);
            usuario.setEstado("APROBADO");
            usuario.setComentarioRevision(null);
            usuarioRepository.save(usuario);
        }

        return "redirect:/admin/empresas-pendientes";
    }

    @PostMapping("/aprobar-oferente/{usuarioId}")
    public String aprobarOferente(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario != null) {
            usuario.setActivo(true);
            usuario.setEstado("APROBADO");
            usuario.setComentarioRevision(null);
            usuarioRepository.save(usuario);
        }

        return "redirect:/admin/oferentes-pendientes";
    }

    @PostMapping("/rechazar-empresa/{usuarioId}")
    public String rechazarEmpresa(@PathVariable Long usuarioId,
                                  @RequestParam("comentario") String comentario) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario != null) {
            usuario.setActivo(false);
            usuario.setEstado("RECHAZADO");
            usuario.setComentarioRevision(comentario == null ? null : comentario.trim());
            usuarioRepository.save(usuario);
        }

        return "redirect:/admin/empresas-pendientes";
    }

    @PostMapping("/rechazar-oferente/{usuarioId}")
    public String rechazarOferente(@PathVariable Long usuarioId,
                                   @RequestParam("comentario") String comentario) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

        if (usuario != null) {
            usuario.setActivo(false);
            usuario.setEstado("RECHAZADO");
            usuario.setComentarioRevision(comentario == null ? null : comentario.trim());
            usuarioRepository.save(usuario);
        }

        return "redirect:/admin/oferentes-pendientes";
    }

    @GetMapping("/caracteristicas")
    public String listarCaracteristicas(Model model) {
        model.addAttribute("caracteristicasRaiz",
                caracteristicaRepository.findByPadreIsNullOrderByNombreAsc());
        return "admin/caracteristicas";
    }

    @GetMapping("/caracteristicas/nueva")
    public String formularioNuevaCaracteristica(Model model) {
        model.addAttribute("todas", caracteristicaRepository.findAllByOrderByNombreAsc());
        return "admin/nueva-caracteristica";
    }

    @PostMapping("/caracteristicas")
    public String guardarCaracteristica(@RequestParam("nombre") String nombre,
                                        @RequestParam(value = "padreId", required = false) Long padreId,
                                        Model model) {

        String nombreLimpio = nombre == null ? "" : nombre.trim();

        if (nombreLimpio.isEmpty()) {
            model.addAttribute("error", "El nombre es obligatorio.");
            model.addAttribute("todas", caracteristicaRepository.findAllByOrderByNombreAsc());
            return "admin/nueva-caracteristica";
        }

        Caracteristica padre = null;
        if (padreId != null) {
            padre = caracteristicaRepository.findById(padreId).orElse(null);
        }

        Caracteristica caracteristica = Caracteristica.builder()
                .nombre(nombreLimpio)
                .padre(padre)
                .build();

        caracteristicaRepository.save(caracteristica);

        return "redirect:/admin/caracteristicas";
    }

    @GetMapping("/reportes/puestos-pdf")
    public void generarReportePdf(@RequestParam("mes") int mes,
                                  @RequestParam("anio") int anio,
                                  HttpServletResponse response) throws Exception {

        List<Puesto> puestos = puestoRepository.findByMesYAnio(mes, anio);

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "inline; filename=reporte_puestos_" + mes + "_" + anio + ".pdf");

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        document.add(new Paragraph("Reporte mensual de puestos"));
        document.add(new Paragraph("Mes: " + mes + "   Año: " + anio));
        document.add(new Paragraph("Cantidad de puestos: " + puestos.size()));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);

        table.addCell("ID");
        table.addCell("Título");
        table.addCell("Empresa");
        table.addCell("Salario");
        table.addCell("Tipo");
        table.addCell("Estado");

        for (Puesto puesto : puestos) {
            table.addCell(String.valueOf(puesto.getId()));
            table.addCell(puesto.getTitulo());
            table.addCell(puesto.getEmpresa().getNombre());
            table.addCell(String.valueOf(puesto.getSalario()));
            table.addCell(puesto.isPublico() ? "Público" : "Privado");
            table.addCell(puesto.isActivo() ? "Activo" : "Inactivo");
        }

        document.add(table);
        document.close();
    }
}