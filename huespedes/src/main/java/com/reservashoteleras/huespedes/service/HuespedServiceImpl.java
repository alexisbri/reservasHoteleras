package com.reservashoteleras.huespedes.service;

import com.reservashoteleras.commons.clients.ReservaClient;
import com.reservashoteleras.commons.dto.huespedes.HuespedRequest;
import com.reservashoteleras.commons.dto.huespedes.HuespedResponse;
import com.reservashoteleras.commons.enums.EstadoRegistro;
import com.reservashoteleras.commons.exceptions.EntidadRelacionadaException;
import com.reservashoteleras.commons.exceptions.RecursoNoEncontradoException;
import com.reservashoteleras.huespedes.entity.Huesped;
import com.reservashoteleras.huespedes.mapper.HuespedMapper;
import com.reservashoteleras.huespedes.repository.HuespedRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class HuespedServiceImpl implements HuespedService {

    private final HuespedRepository huespedRepository;
    private final HuespedMapper huespedMapper;
    private final ReservaClient reservaClient;

    @Override
    @Transactional(readOnly = true)
    public HuespedResponse obtenerHuespedActivoPorId(Long id) {
        log.info("Buscando huésped activo con id {}", id);
        return huespedMapper.entidadAResponse(obtenerHuespedActivoOException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public HuespedResponse obtenerHuespedSinEstadoPorId(Long id) {
        log.info("Buscando huésped sin filtro de estado con id {}", id);
        return huespedMapper.entidadAResponse(huespedRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Huésped no encontrado con id " + id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<HuespedResponse> listar() {
        log.info("Listando huéspedes activos...");
        return huespedRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(huespedMapper::entidadAResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HuespedResponse obtenerPorId(Long id) {
        log.info("Buscando huésped activo con id {}", id);
        return huespedMapper.entidadAResponse(obtenerHuespedActivoOException(id));
    }

    @Override
    public HuespedResponse registrar(HuespedRequest request) {
        log.info("Registrando nuevo huésped: {}", request.nombre());

        validarDatosUnicos(request);

        Huesped huesped = huespedMapper.requestAEntidad(request);

        huespedRepository.save(huesped);

        log.info("Huésped registrado con éxito: {}", huesped.getNombre());

        return huespedMapper.entidadAResponse(huesped);
    }

    @Override
    public HuespedResponse actualizar(HuespedRequest request, Long id) {
        Huesped huesped = obtenerHuespedActivoOException(id);

        log.info("Actualizando huésped con id: {}", id);

        validarCambiosUnicos(request, id);

        huesped.actualizar(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono(),
                request.documento(),
                request.nacionalidad());

        log.info("Huésped actualizado con éxito: {}", huesped.getNombre());

        return huespedMapper.entidadAResponse(huesped);
    }

    @Override
    public void eliminar(Long id) {
        Huesped huesped = obtenerHuespedActivoOException(id);

        log.info("Eliminando huésped con id {}", id);

        validarSinReservasBloqueantes(id);

        huesped.eliminar();

        log.info("Huésped con id {} ha sido eliminado", id);
    }

    private void validarSinReservasBloqueantes(Long idHuesped) {
        log.info("Validando que el huésped {} no tenga reservas EN_CURSO...", idHuesped);

        Boolean tieneReservasBloqueantes = reservaClient.tieneHuespedReservasBloqueantes(idHuesped);

        if (Boolean.TRUE.equals(tieneReservasBloqueantes)) {
            throw new EntidadRelacionadaException(
                    "No se puede eliminar al huésped con id " + idHuesped
                            + " porque tiene una reserva en estado EN_CURSO");
        }
    }

    private Huesped obtenerHuespedActivoOException(Long id) {
        return huespedRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Huésped activo no encontrado con id " + id));
    }

    private void validarDatosUnicos(HuespedRequest request) {
        log.info("Validando email único...");
        if (huespedRepository.existsByEmailIgnoreCaseAndEstadoRegistro(request.email().trim(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe un huésped activo registrado con el email: " + request.email());
        }

        log.info("Validando teléfono único...");
        if (huespedRepository.existsByTelefonoAndEstadoRegistro(request.telefono().trim(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe un huésped activo registrado con el teléfono: " + request.telefono());
        }

        log.info("Validando documento único...");
        if (huespedRepository.existsByDocumentoAndEstadoRegistro(request.documento().trim(), EstadoRegistro.ACTIVO)) {
            throw new IllegalArgumentException("Ya existe un huésped activo registrado con el documento: " + request.documento());
        }
    }

    private void validarCambiosUnicos(HuespedRequest request, Long id) {
        log.info("Validando cambio de email único...");
        if (huespedRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(
                request.email().trim(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("Ya existe un huésped activo registrado con el email: " + request.email());
        }

        log.info("Validando cambio de teléfono único...");
        if (huespedRepository.existsByTelefonoAndEstadoRegistroAndIdNot(
                request.telefono().trim(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("Ya existe un huésped activo registrado con el teléfono: " + request.telefono());
        }

        log.info("Validando cambio de documento único...");
        if (huespedRepository.existsByDocumentoAndEstadoRegistroAndIdNot(
                request.documento().trim(), EstadoRegistro.ACTIVO, id)) {
            throw new IllegalArgumentException("Ya existe un huésped activo registrado con el documento: " + request.documento());
        }
    }

}// FIN DE LA CLASE HUESPEDSERVICEIMPL
