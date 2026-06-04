package cr.ac.una.proyectobolsaempleo.service;

import cr.ac.una.proyectobolsaempleo.dto.empresa.CandidatoResponse;
import cr.ac.una.proyectobolsaempleo.dto.empresa.PuestoEmpresaRequest;
import cr.ac.una.proyectobolsaempleo.dto.empresa.PuestoEmpresaResponse;
import cr.ac.una.proyectobolsaempleo.dto.empresa.RequisitoPuestoRequest;
import cr.ac.una.proyectobolsaempleo.dto.empresa.RequisitoPuestoResponse;
import cr.ac.una.proyectobolsaempleo.model.Caracteristica;
import cr.ac.una.proyectobolsaempleo.model.Empresa;
import cr.ac.una.proyectobolsaempleo.model.Oferente;
import cr.ac.una.proyectobolsaempleo.model.OferenteCaracteristica;
import cr.ac.una.proyectobolsaempleo.model.Puesto;
import cr.ac.una.proyectobolsaempleo.model.PuestoCaracteristica;
import cr.ac.una.proyectobolsaempleo.model.Usuario;
import cr.ac.una.proyectobolsaempleo.repository.CaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.EmpresaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteCaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteRepository;
import cr.ac.una.proyectobolsaempleo.repository.PuestoCaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.PuestoRepository;
import cr.ac.una.proyectobolsaempleo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmpresaService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final PuestoRepository puestoRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final PuestoCaracteristicaRepository puestoCaracteristicaRepository;
    private final OferenteRepository oferenteRepository;
    private final OferenteCaracteristicaRepository oferenteCaracteristicaRepository;

    @Transactional(readOnly = true)
    public List<PuestoEmpresaResponse> listarMisPuestos() {
        Empresa empresa = obtenerEmpresaAutenticada();

        return puestoRepository.findByEmpresaIdOrderByFechaCreacionDesc(empresa.getId())
                .stream()
                .map(this::mapearPuesto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PuestoEmpresaResponse obtenerMiPuesto(Long puestoId) {
        Empresa empresa = obtenerEmpresaAutenticada();

        Puesto puesto = puestoRepository.findByIdAndEmpresaId(puestoId, empresa.getId())
                .orElseThrow(() -> new EntityNotFoundException("Puesto no encontrado"));

        return mapearPuesto(puesto);
    }

    public PuestoEmpresaResponse crearPuesto(PuestoEmpresaRequest request) {
        Empresa empresa = obtenerEmpresaAutenticada();

        Puesto puesto = new Puesto();
        puesto.setTitulo(request.getTitulo().trim());
        puesto.setDescripcion(request.getDescripcion().trim());
        puesto.setSalario(request.getSalario());
        puesto.setPublico(Boolean.TRUE.equals(request.getPublico()));
        puesto.setActivo(true);
        puesto.setFechaCreacion(LocalDateTime.now());
        puesto.setEmpresa(empresa);

        Puesto guardado = puestoRepository.save(puesto);

        return mapearPuesto(guardado);
    }

    public void desactivarPuesto(Long puestoId) {
        Empresa empresa = obtenerEmpresaAutenticada();

        Puesto puesto = puestoRepository.findByIdAndEmpresaId(puestoId, empresa.getId())
                .orElseThrow(() -> new EntityNotFoundException("Puesto no encontrado"));

        puesto.setActivo(false);

        puestoRepository.save(puesto);
    }

    public RequisitoPuestoResponse agregarOActualizarRequisito(Long puestoId, RequisitoPuestoRequest request) {
        Empresa empresa = obtenerEmpresaAutenticada();

        Puesto puesto = puestoRepository.findByIdAndEmpresaId(puestoId, empresa.getId())
                .orElseThrow(() -> new EntityNotFoundException("Puesto no encontrado"));

        Caracteristica caracteristica = caracteristicaRepository.findById(request.getCaracteristicaId())
                .orElseThrow(() -> new EntityNotFoundException("Característica no encontrada"));

        if (!caracteristica.esHoja()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo se pueden usar características finales, no categorías padre."
            );
        }

        PuestoCaracteristica requisito = puestoCaracteristicaRepository
                .findByPuestoIdAndCaracteristicaId(puestoId, caracteristica.getId())
                .orElseGet(PuestoCaracteristica::new);

        requisito.setPuesto(puesto);
        requisito.setCaracteristica(caracteristica);
        requisito.setNivel(request.getNivel());

        PuestoCaracteristica guardado = puestoCaracteristicaRepository.save(requisito);

        return mapearRequisito(guardado);
    }

    @Transactional(readOnly = true)
    public List<CandidatoResponse> buscarCandidatos(Long puestoId) {
        Empresa empresa = obtenerEmpresaAutenticada();

        Puesto puesto = puestoRepository.findByIdAndEmpresaId(puestoId, empresa.getId())
                .orElseThrow(() -> new EntityNotFoundException("Puesto no encontrado"));

        List<PuestoCaracteristica> requisitos = puestoCaracteristicaRepository.findByPuestoId(puesto.getId());

        if (requisitos.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El puesto no tiene requisitos registrados."
            );
        }

        List<Oferente> oferentes = oferenteRepository.findByUsuarioEstadoAndUsuarioActivoTrue("APROBADO");

        return oferentes.stream()
                .map(oferente -> calcularCoincidencia(oferente, requisitos))
                .filter(candidato -> candidato.getRequisitosCumplidos() > 0)
                .sorted(Comparator.comparing(CandidatoResponse::getPorcentajeCoincidencia).reversed())
                .toList();
    }

    private CandidatoResponse calcularCoincidencia(Oferente oferente, List<PuestoCaracteristica> requisitos) {
        List<OferenteCaracteristica> habilidades = oferenteCaracteristicaRepository.findByOferenteId(oferente.getId());

        Map<Long, Integer> mapaHabilidades = habilidades.stream()
                .collect(Collectors.toMap(
                        h -> h.getCaracteristica().getId(),
                        OferenteCaracteristica::getNivel,
                        Math::max
                ));

        int cumplidos = 0;

        for (PuestoCaracteristica requisito : requisitos) {
            Long caracteristicaId = requisito.getCaracteristica().getId();
            Integer nivelOferente = mapaHabilidades.get(caracteristicaId);

            if (nivelOferente != null && nivelOferente >= requisito.getNivel()) {
                cumplidos++;
            }
        }

        int total = requisitos.size();
        double porcentaje = total == 0 ? 0.0 : (cumplidos * 100.0) / total;

        Usuario usuario = oferente.getUsuario();

        return CandidatoResponse.builder()
                .oferenteId(oferente.getId())
                .nombre(oferente.getNombre() + " " + oferente.getApellido())
                .correo(usuario.getCorreo())
                .telefono(oferente.getTelefono())
                .residencia(oferente.getResidencia())
                .requisitosCumplidos(cumplidos)
                .totalRequisitos(total)
                .porcentajeCoincidencia(porcentaje)
                .build();
    }

    private Empresa obtenerEmpresaAutenticada() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        return empresaRepository.findByUsuarioCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));
    }

    private PuestoEmpresaResponse mapearPuesto(Puesto puesto) {
        List<RequisitoPuestoResponse> requisitos = puestoCaracteristicaRepository.findByPuestoId(puesto.getId())
                .stream()
                .sorted(Comparator.comparing(pc -> pc.getCaracteristica().getNombreCompleto()))
                .map(this::mapearRequisito)
                .toList();

        return PuestoEmpresaResponse.builder()
                .id(puesto.getId())
                .titulo(puesto.getTitulo())
                .descripcion(puesto.getDescripcion())
                .salario(puesto.getSalario())
                .publico(puesto.isPublico())
                .activo(puesto.isActivo())
                .fechaCreacion(puesto.getFechaCreacion())
                .requisitos(requisitos)
                .build();
    }

    private RequisitoPuestoResponse mapearRequisito(PuestoCaracteristica requisito) {
        Caracteristica caracteristica = requisito.getCaracteristica();

        return RequisitoPuestoResponse.builder()
                .caracteristicaId(caracteristica.getId())
                .caracteristica(caracteristica.getNombre())
                .nombreCompleto(caracteristica.getNombreCompleto())
                .nivel(requisito.getNivel())
                .build();
    }
}
