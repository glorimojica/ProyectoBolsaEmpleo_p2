package cr.ac.una.proyectobolsaempleo.service;

import cr.ac.una.proyectobolsaempleo.dto.admin.UsuarioPendienteResponse;
import cr.ac.una.proyectobolsaempleo.model.Empresa;
import cr.ac.una.proyectobolsaempleo.model.Oferente;
import cr.ac.una.proyectobolsaempleo.model.Usuario;
import cr.ac.una.proyectobolsaempleo.repository.EmpresaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteRepository;
import cr.ac.una.proyectobolsaempleo.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {

    private static final String ESTADO_PENDIENTE = "PENDIENTE";
    private static final String ESTADO_APROBADO = "APROBADO";
    private static final String ESTADO_RECHAZADO = "RECHAZADO";

    private final EmpresaRepository empresaRepository;
    private final OferenteRepository oferenteRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<UsuarioPendienteResponse> listarEmpresasPendientes() {
        return empresaRepository.findByUsuarioEstado(ESTADO_PENDIENTE)
                .stream()
                .map(this::mapearEmpresaPendiente)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UsuarioPendienteResponse> listarOferentesPendientes() {
        return oferenteRepository.findByUsuarioEstado(ESTADO_PENDIENTE)
                .stream()
                .map(this::mapearOferentePendiente)
                .toList();
    }

    public void aprobarEmpresa(Long empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));

        Usuario usuario = empresa.getUsuario();

        usuario.setEstado(ESTADO_APROBADO);
        usuario.setActivo(true);
        usuario.setComentarioRevision(null);

        usuarioRepository.save(usuario);
    }

    public void rechazarEmpresa(Long empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));

        Usuario usuario = empresa.getUsuario();

        usuario.setEstado(ESTADO_RECHAZADO);
        usuario.setActivo(false);
        usuario.setComentarioRevision("Registro rechazado por el administrador.");

        usuarioRepository.save(usuario);
    }

    public void aprobarOferente(Long oferenteId) {
        Oferente oferente = oferenteRepository.findById(oferenteId)
                .orElseThrow(() -> new EntityNotFoundException("Oferente no encontrado"));

        Usuario usuario = oferente.getUsuario();

        usuario.setEstado(ESTADO_APROBADO);
        usuario.setActivo(true);
        usuario.setComentarioRevision(null);

        usuarioRepository.save(usuario);
    }

    public void rechazarOferente(Long oferenteId) {
        Oferente oferente = oferenteRepository.findById(oferenteId)
                .orElseThrow(() -> new EntityNotFoundException("Oferente no encontrado"));

        Usuario usuario = oferente.getUsuario();

        usuario.setEstado(ESTADO_RECHAZADO);
        usuario.setActivo(false);
        usuario.setComentarioRevision("Registro rechazado por el administrador.");

        usuarioRepository.save(usuario);
    }

    private UsuarioPendienteResponse mapearEmpresaPendiente(Empresa empresa) {
        Usuario usuario = empresa.getUsuario();

        return UsuarioPendienteResponse.builder()
                .id(empresa.getId())
                .usuarioId(usuario.getId())
                .nombre(empresa.getNombre())
                .correo(usuario.getCorreo())
                .telefono(empresa.getTelefono())
                .estado(usuario.getEstado())
                .tipo("EMPRESA")
                .build();
    }

    private UsuarioPendienteResponse mapearOferentePendiente(Oferente oferente) {
        Usuario usuario = oferente.getUsuario();

        String nombreCompleto = oferente.getNombre();

        if (oferente.getApellido() != null && !oferente.getApellido().isBlank()) {
            nombreCompleto += " " + oferente.getApellido();
        }

        return UsuarioPendienteResponse.builder()
                .id(oferente.getId())
                .usuarioId(usuario.getId())
                .nombre(nombreCompleto)
                .correo(usuario.getCorreo())
                .telefono(oferente.getTelefono())
                .estado(usuario.getEstado())
                .tipo("OFERENTE")
                .build();
    }
}