package cr.ac.una.proyectobolsaempleo.service;

import cr.ac.una.proyectobolsaempleo.dto.oferente.HabilidadRequest;
import cr.ac.una.proyectobolsaempleo.dto.oferente.HabilidadResponse;
import cr.ac.una.proyectobolsaempleo.dto.oferente.OferentePerfilRequest;
import cr.ac.una.proyectobolsaempleo.dto.oferente.OferentePerfilResponse;
import cr.ac.una.proyectobolsaempleo.dto.publico.PuestoPublicoResponse;
import cr.ac.una.proyectobolsaempleo.dto.publico.RequisitoPuestoResponse;
import cr.ac.una.proyectobolsaempleo.model.Caracteristica;
import cr.ac.una.proyectobolsaempleo.model.Oferente;
import cr.ac.una.proyectobolsaempleo.model.OferenteCaracteristica;
import cr.ac.una.proyectobolsaempleo.model.Puesto;
import cr.ac.una.proyectobolsaempleo.model.PuestoCaracteristica;
import cr.ac.una.proyectobolsaempleo.repository.CaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteCaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteRepository;
import cr.ac.una.proyectobolsaempleo.repository.PuestoCaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.PuestoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OferenteService {

    private final OferenteRepository oferenteRepository;
    private final CaracteristicaRepository caracteristicaRepository;
    private final OferenteCaracteristicaRepository oferenteCaracteristicaRepository;
    private final PuestoRepository puestoRepository;
    private final PuestoCaracteristicaRepository puestoCaracteristicaRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Transactional(readOnly = true)
    public OferentePerfilResponse obtenerPerfil() {
        return mapearPerfil(obtenerOferenteAutenticado());
    }

    public OferentePerfilResponse actualizarPerfil(OferentePerfilRequest request) {
        Oferente oferente = obtenerOferenteAutenticado();

        oferente.setNombre(request.getNombre().trim());
        oferente.setApellido(request.getApellido().trim());
        oferente.setNacionalidad(request.getNacionalidad().trim());
        oferente.setTelefono(request.getTelefono().trim());
        oferente.setResidencia(request.getResidencia().trim());

        return mapearPerfil(oferenteRepository.save(oferente));
    }

    @Transactional(readOnly = true)
    public List<HabilidadResponse> listarHabilidades() {
        Oferente oferente = obtenerOferenteAutenticado();

        return oferenteCaracteristicaRepository.findByOferenteId(oferente.getId())
                .stream()
                .sorted(Comparator.comparing(h -> h.getCaracteristica().getNombreCompleto()))
                .map(this::mapearHabilidad)
                .toList();
    }

    public HabilidadResponse agregarOActualizarHabilidad(HabilidadRequest request) {
        Oferente oferente = obtenerOferenteAutenticado();

        Caracteristica caracteristica = caracteristicaRepository.findById(request.getCaracteristicaId())
                .orElseThrow(() -> new EntityNotFoundException("Característica no encontrada"));

        if (!caracteristica.esHoja()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Solo se pueden registrar características finales, no categorías padre."
            );
        }

        OferenteCaracteristica habilidad = oferenteCaracteristicaRepository
                .findByOferenteIdAndCaracteristicaId(oferente.getId(), caracteristica.getId())
                .orElseGet(OferenteCaracteristica::new);

        habilidad.setOferente(oferente);
        habilidad.setCaracteristica(caracteristica);
        habilidad.setNivel(request.getNivel());

        return mapearHabilidad(oferenteCaracteristicaRepository.save(habilidad));
    }

    public void eliminarHabilidad(Long habilidadId) {
        Oferente oferente = obtenerOferenteAutenticado();

        OferenteCaracteristica habilidad = oferenteCaracteristicaRepository
                .findByIdAndOferenteId(habilidadId, oferente.getId())
                .orElseThrow(() -> new EntityNotFoundException("Habilidad no encontrada"));

        oferenteCaracteristicaRepository.delete(habilidad);
    }

    public OferentePerfilResponse subirCv(MultipartFile archivo) {
        Oferente oferente = obtenerOferenteAutenticado();

        if (archivo == null || archivo.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe seleccionar un archivo PDF.");
        }

        String nombreOriginal = StringUtils.cleanPath(
                archivo.getOriginalFilename() == null ? "cv.pdf" : archivo.getOriginalFilename()
        );

        if (!nombreOriginal.toLowerCase().endsWith(".pdf")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El currículo debe estar en formato PDF.");
        }

        String tipoContenido = archivo.getContentType();

        if (tipoContenido != null && !tipoContenido.equalsIgnoreCase("application/pdf")) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El archivo seleccionado no parece ser un PDF válido."
            );
        }

        try {
            Path carpetaCv = obtenerCarpetaCv();
            Files.createDirectories(carpetaCv);

            String nombreGuardado = "oferente-" + oferente.getId() + "-" + System.currentTimeMillis() + ".pdf";
            Path destino = carpetaCv.resolve(nombreGuardado).normalize();

            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            oferente.setCvNombreArchivo(nombreGuardado);

            return mapearPerfil(oferenteRepository.save(oferente));
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo guardar el currículo.");
        }
    }

    @Transactional(readOnly = true)
    public Path obtenerRutaCvAutenticado() {
        Oferente oferente = obtenerOferenteAutenticado();

        if (oferente.getCvNombreArchivo() == null || oferente.getCvNombreArchivo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El oferente no tiene currículo registrado.");
        }

        Path ruta = obtenerCarpetaCv().resolve(oferente.getCvNombreArchivo()).normalize();

        if (!Files.exists(ruta)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El archivo del currículo no existe.");
        }

        return ruta;
    }

    @Transactional(readOnly = true)
    public List<PuestoPublicoResponse> listarPuestosDisponibles() {
        return puestoRepository.findByActivoTrueOrderByFechaCreacionDesc()
                .stream()
                .map(this::mapearPuesto)
                .toList();
    }

    private Oferente obtenerOferenteAutenticado() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        return oferenteRepository.findByUsuarioCorreo(correo)
                .orElseThrow(() -> new EntityNotFoundException("Oferente no encontrado"));
    }

    private Path obtenerCarpetaCv() {
        return Paths.get(uploadDir).toAbsolutePath().normalize().resolve("cv");
    }

    private OferentePerfilResponse mapearPerfil(Oferente oferente) {
        return OferentePerfilResponse.builder()
                .id(oferente.getId())
                .identificacion(oferente.getIdentificacion())
                .nombre(oferente.getNombre())
                .apellido(oferente.getApellido())
                .nacionalidad(oferente.getNacionalidad())
                .telefono(oferente.getTelefono())
                .correo(oferente.getUsuario().getCorreo())
                .residencia(oferente.getResidencia())
                .cvNombreArchivo(oferente.getCvNombreArchivo())
                .cvDisponible(oferente.getCvNombreArchivo() != null && !oferente.getCvNombreArchivo().isBlank())
                .build();
    }

    private HabilidadResponse mapearHabilidad(OferenteCaracteristica habilidad) {
        Caracteristica caracteristica = habilidad.getCaracteristica();

        return HabilidadResponse.builder()
                .id(habilidad.getId())
                .caracteristicaId(caracteristica.getId())
                .caracteristica(caracteristica.getNombre())
                .nombreCompleto(caracteristica.getNombreCompleto())
                .nivel(habilidad.getNivel())
                .build();
    }

    private PuestoPublicoResponse mapearPuesto(Puesto puesto) {
        List<RequisitoPuestoResponse> requisitos = puestoCaracteristicaRepository.findByPuestoId(puesto.getId())
                .stream()
                .sorted(Comparator.comparing(pc -> pc.getCaracteristica().getNombreCompleto()))
                .map(this::mapearRequisito)
                .toList();

        return PuestoPublicoResponse.builder()
                .id(puesto.getId())
                .titulo(puesto.getTitulo())
                .descripcion(puesto.getDescripcion())
                .salario(puesto.getSalario())
                .publico(puesto.isPublico())
                .activo(puesto.isActivo())
                .fechaCreacion(puesto.getFechaCreacion())
                .empresaId(puesto.getEmpresa().getId())
                .empresa(puesto.getEmpresa().getNombre())
                .requisitos(requisitos)
                .build();
    }

    private RequisitoPuestoResponse mapearRequisito(PuestoCaracteristica requisito) {
        Caracteristica caracteristica = requisito.getCaracteristica();

        return RequisitoPuestoResponse.builder()
                .caracteristicaId(caracteristica.getId())
                .caracteristica(caracteristica.getNombre())
                .categoria(caracteristica.getNombreCompleto())
                .nivel(requisito.getNivel())
                .build();
    }
}