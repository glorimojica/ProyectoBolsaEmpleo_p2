package cr.ac.una.proyectobolsaempleo.service;

import cr.ac.una.proyectobolsaempleo.dto.auth.ApiMessageResponse;
import cr.ac.una.proyectobolsaempleo.dto.auth.AuthResponse;
import cr.ac.una.proyectobolsaempleo.dto.auth.LoginRequest;
import cr.ac.una.proyectobolsaempleo.dto.auth.MeResponse;
import cr.ac.una.proyectobolsaempleo.dto.auth.RegisterEmpresaRequest;
import cr.ac.una.proyectobolsaempleo.dto.auth.RegisterOferenteRequest;
import cr.ac.una.proyectobolsaempleo.model.Administrador;
import cr.ac.una.proyectobolsaempleo.model.Empresa;
import cr.ac.una.proyectobolsaempleo.model.Oferente;
import cr.ac.una.proyectobolsaempleo.model.Usuario;
import cr.ac.una.proyectobolsaempleo.repository.AdministradorRepository;
import cr.ac.una.proyectobolsaempleo.repository.EmpresaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteRepository;
import cr.ac.una.proyectobolsaempleo.repository.UsuarioRepository;
import cr.ac.una.proyectobolsaempleo.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaRepository empresaRepository;
    private final OferenteRepository oferenteRepository;
    private final AdministradorRepository administradorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public ApiMessageResponse registrarEmpresa(RegisterEmpresaRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese correo.");
        }

        Usuario usuario = Usuario.builder()
                .correo(request.getCorreo())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol("EMPRESA")
                .activo(false)
                .estado("PENDIENTE")
                .comentarioRevision(null)
                .build();

        usuarioRepository.save(usuario);

        Empresa empresa = Empresa.builder()
                .nombre(request.getNombre())
                .telefono(request.getTelefono())
                .localizacion(request.getLocalizacion())
                .descripcion(request.getDescripcion())
                .usuario(usuario)
                .build();

        empresaRepository.save(empresa);

        return new ApiMessageResponse("Registro de empresa enviado. Pendiente de aprobación.");
    }

    @Transactional
    public ApiMessageResponse registrarOferente(RegisterOferenteRequest request) {
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un usuario con ese correo.");
        }

        Usuario usuario = Usuario.builder()
                .correo(request.getCorreo())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol("OFERENTE")
                .activo(false)
                .estado("PENDIENTE")
                .comentarioRevision(null)
                .build();

        usuarioRepository.save(usuario);

        Oferente oferente = Oferente.builder()
                .identificacion(request.getIdentificacion())
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .telefono(request.getTelefono())
                .residencia(request.getResidencia())
                .nacionalidad(request.getNacionalidad())
                .usuario(usuario)
                .build();

        oferenteRepository.save(oferente);

        return new ApiMessageResponse("Registro de oferente enviado. Pendiente de aprobación.");
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Usuario usuario = buscarUsuarioParaLogin(request.getUsuario());

        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o clave incorrectos.");
        }

        validarUsuarioPuedeIngresar(usuario);

        String token = jwtService.generateToken(usuario);
        String nombre = obtenerNombreUsuario(usuario);

        return AuthResponse.builder()
                .token(token)
                .usuarioId(usuario.getId())
                .nombre(nombre)
                .correo(usuario.getCorreo())
                .rol(usuario.getRol())
                .estado(usuario.getEstado())
                .build();
    }

    @Transactional(readOnly = true)
    public MeResponse me(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));

        validarUsuarioPuedeIngresar(usuario);

        String nombre = obtenerNombreUsuario(usuario);

        return MeResponse.builder()
                .usuarioId(usuario.getId())
                .nombre(nombre)
                .correo(usuario.getCorreo())
                .rol(usuario.getRol())
                .estado(usuario.getEstado())
                .build();
    }

    private Usuario buscarUsuarioParaLogin(String usuarioLogin) {
        return usuarioRepository.findByCorreo(usuarioLogin)
                .orElseGet(() -> administradorRepository.findByIdentificacion(usuarioLogin)
                        .map(Administrador::getUsuario)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o clave incorrectos.")));
    }

    private void validarUsuarioPuedeIngresar(Usuario usuario) {
        if ("PENDIENTE".equals(usuario.getEstado())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Su registro aún está pendiente de aprobación.");
        }

        if ("RECHAZADO".equals(usuario.getEstado())) {
            String comentario = usuario.getComentarioRevision();

            if (comentario == null || comentario.isBlank()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Su registro fue rechazado.");
            }

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Su registro fue rechazado. Razón: " + comentario);
        }

        if (!usuario.isActivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El usuario está inactivo.");
        }
    }

    private String obtenerNombreUsuario(Usuario usuario) {
        if ("EMPRESA".equals(usuario.getRol())) {
            return empresaRepository.findByUsuarioCorreo(usuario.getCorreo())
                    .map(Empresa::getNombre)
                    .orElse(usuario.getCorreo());
        }

        if ("OFERENTE".equals(usuario.getRol())) {
            return oferenteRepository.findByUsuarioCorreo(usuario.getCorreo())
                    .map(oferente -> oferente.getNombre() + " " + oferente.getApellido())
                    .orElse(usuario.getCorreo());
        }

        if ("ADMIN".equals(usuario.getRol())) {
            return administradorRepository.findByUsuarioCorreo(usuario.getCorreo())
                    .map(Administrador::getNombre)
                    .orElse("Administrador");
        }

        return usuario.getCorreo();
    }
}