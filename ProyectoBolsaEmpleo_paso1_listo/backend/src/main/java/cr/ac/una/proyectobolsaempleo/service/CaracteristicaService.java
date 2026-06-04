package cr.ac.una.proyectobolsaempleo.service;

import cr.ac.una.proyectobolsaempleo.dto.admin.CaracteristicaRequest;
import cr.ac.una.proyectobolsaempleo.dto.admin.CaracteristicaResponse;
import cr.ac.una.proyectobolsaempleo.model.Caracteristica;
import cr.ac.una.proyectobolsaempleo.repository.CaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.OferenteCaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.PuestoCaracteristicaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CaracteristicaService {

    private final CaracteristicaRepository caracteristicaRepository;
    private final PuestoCaracteristicaRepository puestoCaracteristicaRepository;
    private final OferenteCaracteristicaRepository oferenteCaracteristicaRepository;

    @Transactional(readOnly = true)
    public List<CaracteristicaResponse> listarArbol() {
        return caracteristicaRepository.findByPadreIsNullOrderByNombreAsc()
                .stream()
                .map(this::mapearCaracteristica)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CaracteristicaResponse> listarTodasPlanas() {
        return caracteristicaRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Caracteristica::getNombre))
                .map(this::mapearCaracteristicaSimple)
                .toList();
    }

    public CaracteristicaResponse crear(CaracteristicaRequest request) {
        String nombre = normalizarNombre(request.getNombre());

        validarNombreDuplicado(nombre, request.getPadreId(), null);

        Caracteristica padre = obtenerPadre(request.getPadreId());

        Caracteristica caracteristica = new Caracteristica();
        caracteristica.setNombre(nombre);
        caracteristica.setPadre(padre);

        Caracteristica guardada = caracteristicaRepository.save(caracteristica);

        return mapearCaracteristicaSimple(guardada);
    }

    public CaracteristicaResponse actualizar(Long id, CaracteristicaRequest request) {
        Caracteristica caracteristica = caracteristicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Característica no encontrada"));

        String nombre = normalizarNombre(request.getNombre());

        if (request.getPadreId() != null && request.getPadreId().equals(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Una característica no puede ser padre de sí misma."
            );
        }

        Caracteristica nuevoPadre = obtenerPadre(request.getPadreId());

        if (nuevoPadre != null && esDescendiente(nuevoPadre, caracteristica)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede asignar como padre una característica hija."
            );
        }

        validarNombreDuplicado(nombre, request.getPadreId(), id);

        caracteristica.setNombre(nombre);
        caracteristica.setPadre(nuevoPadre);

        Caracteristica actualizada = caracteristicaRepository.save(caracteristica);

        return mapearCaracteristicaSimple(actualizada);
    }

    public void eliminar(Long id) {
        Caracteristica caracteristica = caracteristicaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Característica no encontrada"));

        if (caracteristicaRepository.existsByPadre_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede eliminar una característica que tiene hijas."
            );
        }

        if (puestoCaracteristicaRepository.existsByCaracteristica_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede eliminar porque está siendo usada en puestos."
            );
        }

        if (oferenteCaracteristicaRepository.existsByCaracteristica_Id(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se puede eliminar porque está siendo usada por oferentes."
            );
        }

        caracteristicaRepository.delete(caracteristica);
    }

    private Caracteristica obtenerPadre(Long padreId) {
        if (padreId == null) {
            return null;
        }

        return caracteristicaRepository.findById(padreId)
                .orElseThrow(() -> new EntityNotFoundException("Característica padre no encontrada"));
    }

    private String normalizarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "El nombre de la característica es obligatorio."
            );
        }

        return nombre.trim();
    }

    private void validarNombreDuplicado(String nombre, Long padreId, Long idActual) {
        List<Caracteristica> hermanas;

        if (padreId == null) {
            hermanas = caracteristicaRepository.findByPadreIsNullOrderByNombreAsc();
        } else {
            hermanas = caracteristicaRepository.findByPadreIdOrderByNombreAsc(padreId);
        }

        boolean existe = hermanas.stream()
                .anyMatch(c ->
                        c.getNombre().equalsIgnoreCase(nombre)
                                && (idActual == null || !c.getId().equals(idActual))
                );

        if (existe) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ya existe una característica con ese nombre en el mismo nivel."
            );
        }
    }

    private boolean esDescendiente(Caracteristica posiblePadre, Caracteristica caracteristicaActual) {
        Caracteristica padre = posiblePadre.getPadre();

        while (padre != null) {
            if (padre.getId().equals(caracteristicaActual.getId())) {
                return true;
            }

            padre = padre.getPadre();
        }

        return false;
    }

    private CaracteristicaResponse mapearCaracteristica(Caracteristica caracteristica) {
        List<CaracteristicaResponse> hijas = caracteristica.getHijas()
                .stream()
                .sorted(Comparator.comparing(Caracteristica::getNombre))
                .map(this::mapearCaracteristica)
                .toList();

        return CaracteristicaResponse.builder()
                .id(caracteristica.getId())
                .nombre(caracteristica.getNombre())
                .nombreCompleto(caracteristica.getNombreCompleto())
                .padreId(caracteristica.getPadre() != null ? caracteristica.getPadre().getId() : null)
                .hoja(caracteristica.esHoja())
                .hijas(hijas)
                .build();
    }

    private CaracteristicaResponse mapearCaracteristicaSimple(Caracteristica caracteristica) {
        return CaracteristicaResponse.builder()
                .id(caracteristica.getId())
                .nombre(caracteristica.getNombre())
                .nombreCompleto(caracteristica.getNombreCompleto())
                .padreId(caracteristica.getPadre() != null ? caracteristica.getPadre().getId() : null)
                .hoja(caracteristica.esHoja())
                .hijas(List.of())
                .build();
    }
}