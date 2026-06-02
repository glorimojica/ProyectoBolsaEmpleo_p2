package cr.ac.una.proyectobolsaempleo.service.impl;


import cr.ac.una.proyectobolsaempleo.dto.publico.CaracteristicaResponse;
import cr.ac.una.proyectobolsaempleo.dto.publico.PuestoPublicoResponse;
import cr.ac.una.proyectobolsaempleo.dto.publico.RequisitoPuestoResponse;
import cr.ac.una.proyectobolsaempleo.model.Caracteristica;
import cr.ac.una.proyectobolsaempleo.model.Puesto;
import cr.ac.una.proyectobolsaempleo.model.PuestoCaracteristica;
import cr.ac.una.proyectobolsaempleo.repository.CaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.PuestoCaracteristicaRepository;
import cr.ac.una.proyectobolsaempleo.repository.PuestoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublicService {

    private final PuestoRepository puestoRepository;
    private final PuestoCaracteristicaRepository puestoCaracteristicaRepository;
    private final CaracteristicaRepository caracteristicaRepository;

    public List<PuestoPublicoResponse> listarPuestosRecientes() {
        return puestoRepository.findTop5ByPublicoTrueAndActivoTrueOrderByFechaCreacionDesc()
                .stream()
                .map(this::mapearPuesto)
                .toList();
    }

    public List<PuestoPublicoResponse> buscarPuestosPublicos(Long caracteristicaId, Integer nivel) {
        if (nivel != null && (nivel < 1 || nivel > 5)) {
            nivel = null;
        }

        return puestoRepository.buscarPublicos(caracteristicaId, nivel)
                .stream()
                .map(this::mapearPuesto)
                .toList();
    }

    public PuestoPublicoResponse obtenerDetallePuesto(Long id) {
        Puesto puesto = puestoRepository.findByIdAndPublicoTrueAndActivoTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Puesto público no encontrado"));

        return mapearPuesto(puesto);
    }

    public List<CaracteristicaResponse> listarArbolCaracteristicas() {
        return caracteristicaRepository.findByPadreIsNullOrderByNombreAsc()
                .stream()
                .map(this::mapearCaracteristica)
                .toList();
    }

    private PuestoPublicoResponse mapearPuesto(Puesto puesto) {
        List<RequisitoPuestoResponse> requisitos = puestoCaracteristicaRepository.findByPuestoId(puesto.getId())
                .stream()
                .sorted(Comparator.comparing(pc -> pc.getCaracteristica().getNombre()))
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
}
