package com.reservashoteleras.reservas.service;

import com.reservashoteleras.commons.clients.HabitacionClient;
import com.reservashoteleras.commons.clients.HuespedClient;
import com.reservashoteleras.commons.dto.habitaciones.HabitacionResponse;
import com.reservashoteleras.commons.dto.huespedes.HuespedResponse;
import com.reservashoteleras.commons.enums.EstadoHabitacion;
import com.reservashoteleras.commons.enums.EstadoRegistro;
import com.reservashoteleras.commons.enums.EstadoReserva;
import com.reservashoteleras.commons.exceptions.RecursoNoEncontradoException;
import com.reservashoteleras.reservas.dto.ReservaRequest;
import com.reservashoteleras.reservas.dto.ReservaResponse;
import com.reservashoteleras.reservas.entity.Reserva;
import com.reservashoteleras.reservas.mapper.ReservaMapper;
import com.reservashoteleras.reservas.repository.ReservaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ReservaServiceImpl implements ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaMapper reservaMapper;
    private final HuespedClient huespedClient;
    private final HabitacionClient habitacionClient;

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponse> listar() {
        log.info("Listando reservas activas...");

        return reservaRepository.findByEstadoRegistro(EstadoRegistro.ACTIVO).stream()
                .map(reserva -> reservaMapper.entidadAResponse(
                        reserva,
                        obtenerHuespedSinEstado(reserva.getIdHuesped()),
                        obtenerHabitacionSinEstado(reserva.getIdHabitacion())))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponse obtenerPorId(Long id) {
        Reserva reserva = obtenerReservaOException(id);

        return reservaMapper.entidadAResponse(
                reserva,
                obtenerHuespedSinEstado(reserva.getIdHuesped()),
                obtenerHabitacionSinEstado(reserva.getIdHabitacion()));
    }

    @Override
    public ReservaResponse registrar(ReservaRequest request) {
        log.info("Registrando nueva reserva: {}", request);

        // Lanza 404 vía FeignException si el huésped no existe o no está ACTIVO
        HuespedResponse huesped = obtenerHuespedActivo(request.idHuesped());

        // Lanza 404/409 vía FeignException si la habitación no existe, no está ACTIVA o no está DISPONIBLE
        HabitacionResponse habitacion = obtenerHabitacionActivaDisponible(request.idHabitacion());

        Reserva reserva = reservaMapper.requestAEntidad(request);

        reservaRepository.save(reserva);

        log.info("Marcando la habitación {} como OCUPADA por la nueva reserva", request.idHabitacion());
        habitacionClient.actualizarEstadoHabitacion(request.idHabitacion(), EstadoHabitacion.OCUPADA.getCodigo());

        log.info("Reserva registrada exitosamente con id: {}", reserva.getId());

        return reservaMapper.entidadAResponse(reserva, huesped, habitacion);
    }

    @Override
    public ReservaResponse actualizar(ReservaRequest request, Long id) {
        Reserva reserva = obtenerReservaOException(id);

        log.info("Actualizando fechas de la reserva con id: {}", id);

        // Lanza 409 si intenta cambiar huésped/habitación, o si el estado actual no lo permite
        reserva.actualizarFechas(request.idHabitacion(), request.idHuesped(),
                request.fechaEntrada(), request.fechaSalida());

        log.info("Reserva con id {} actualizada correctamente", id);

        return reservaMapper.entidadAResponse(
                reserva,
                obtenerHuespedSinEstado(reserva.getIdHuesped()),
                obtenerHabitacionSinEstado(reserva.getIdHabitacion()));
    }

    @Override
    public ReservaResponse cambiarEstado(Long idReserva, Long idEstado) {
        Reserva reserva = obtenerReservaOException(idReserva);

        EstadoReserva nuevoEstado = EstadoReserva.obtenerPorCodigo(idEstado);

        log.info("Cambiando estado de la reserva {} a {}", idReserva, nuevoEstado);

        reserva.actualizarEstado(nuevoEstado);

        sincronizarEstadoHabitacion(reserva.getIdHabitacion(), nuevoEstado);

        log.info("Estado de la reserva {} actualizado a {}", idReserva, nuevoEstado);

        return reservaMapper.entidadAResponse(
                reserva,
                obtenerHuespedSinEstado(reserva.getIdHuesped()),
                obtenerHabitacionSinEstado(reserva.getIdHabitacion()));
    }

    @Override
    public void eliminar(Long id) {
        Reserva reserva = obtenerReservaOException(id);

        log.info("Eliminando reserva con id: {}", id);

        // Lanza 409 si la reserva está CONFIRMADA o EN_CURSO (regla validada dentro de la entidad)
        reserva.eliminar();

        log.info("Reserva con id {} ha sido marcada como eliminada", id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneHuespedReservasBloqueantes(Long idHuesped) {
        log.info("Verificando reservas EN_CURSO del huésped {}", idHuesped);

        return reservaRepository.existsByIdHuespedAndEstadoRegistroAndEstadoReserva(
                idHuesped, EstadoRegistro.ACTIVO, EstadoReserva.EN_CURSO);
    }

    /**
     * Sincroniza el estado de la habitación según la nueva etapa de la reserva:
     * - Check-in (EN_CURSO): la habitación permanece OCUPADA, no requiere sincronización.
     * - Check-out (FINALIZADA) o Cancelación (CANCELADA): la habitación vuelve a DISPONIBLE.
     */
    private void sincronizarEstadoHabitacion(Long idHabitacion, EstadoReserva nuevoEstado) {
        if (nuevoEstado == EstadoReserva.FINALIZADA || nuevoEstado == EstadoReserva.CANCELADA) {
            log.info("Liberando la habitación {} (reserva -> {})", idHabitacion, nuevoEstado);
            habitacionClient.actualizarEstadoHabitacion(idHabitacion, EstadoHabitacion.DISPONIBLE.getCodigo());
        }
    }

    private Reserva obtenerReservaOException(Long id) {
        return reservaRepository.findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() -> new RecursoNoEncontradoException("Reserva activa no encontrada con id " + id));
    }

    private HuespedResponse obtenerHuespedActivo(Long id) {
        log.info("Buscando huésped activo con id {} en el servicio remoto...", id);
        return huespedClient.obtenerHuespedActivoPorId(id);
    }

    private HuespedResponse obtenerHuespedSinEstado(Long id) {
        log.info("Buscando huésped sin estado con id {} en el servicio remoto...", id);
        return huespedClient.obtenerHuespedSinEstadoPorId(id);
    }

    private HabitacionResponse obtenerHabitacionActivaDisponible(Long id) {
        log.info("Buscando habitación activa y disponible con id {} en el servicio remoto...", id);
        return habitacionClient.obtenerHabitacionActivaDisponiblePorId(id);
    }

    private HabitacionResponse obtenerHabitacionSinEstado(Long id) {
        log.info("Buscando habitación sin estado con id {} en el servicio remoto...", id);
        return habitacionClient.obtenerHabitacionSinEstadoPorId(id);
    }

}// FIN DE LA CLASE RESERVASERVICEIMPL
