package com.reservashoteleras.reservas.mapper;

import com.reservashoteleras.commons.dto.habitaciones.DatosHabitacion;
import com.reservashoteleras.commons.dto.habitaciones.HabitacionResponse;
import com.reservashoteleras.commons.dto.huespedes.DatosHuesped;
import com.reservashoteleras.commons.dto.huespedes.HuespedResponse;
import com.reservashoteleras.commons.mapper.CommonMapper;
import com.reservashoteleras.reservas.dto.ReservaRequest;
import com.reservashoteleras.reservas.dto.ReservaResponse;
import com.reservashoteleras.reservas.entity.Reserva;
import org.springframework.stereotype.Component;

@Component
public class ReservaMapper implements CommonMapper<ReservaRequest, ReservaResponse, Reserva> {

    @Override
    public Reserva requestAEntidad(ReservaRequest request) {
        if (request == null) return null;

        return Reserva.crear(request.idHabitacion(), request.idHuesped(),
                request.fechaEntrada(), request.fechaSalida());
    }

    @Override
    public ReservaResponse entidadAResponse(Reserva entidad) {
        if (entidad == null) return null;

        return new ReservaResponse(
                entidad.getId(),
                null,
                null,
                entidad.getFechaEntrada(),
                entidad.getFechaSalida(),
                entidad.getEstadoReserva() == null ? null : entidad.getEstadoReserva().name(),
                entidad.getEstadoRegistro() == null ? null : entidad.getEstadoRegistro().name());
    }

    // -- Sobrecarga que enriquece la respuesta con los datos de Huésped y Habitación (vía Feign)

    public ReservaResponse entidadAResponse(Reserva entidad, HuespedResponse huesped, HabitacionResponse habitacion) {
        if (entidad == null) return null;

        return new ReservaResponse(
                entidad.getId(),
                huespedResponseADatosHuesped(huesped),
                habitacionResponseADatosHabitacion(habitacion),
                entidad.getFechaEntrada(),
                entidad.getFechaSalida(),
                entidad.getEstadoReserva() == null ? null : entidad.getEstadoReserva().name(),
                entidad.getEstadoRegistro() == null ? null : entidad.getEstadoRegistro().name());
    }

    private DatosHuesped huespedResponseADatosHuesped(HuespedResponse huesped) {
        if (huesped == null) return null;

        return new DatosHuesped(huesped.nombreCompleto(), huesped.documento(), huesped.telefono());
    }

    private DatosHabitacion habitacionResponseADatosHabitacion(HabitacionResponse habitacion) {
        if (habitacion == null) return null;

        return new DatosHabitacion(habitacion.numero(), habitacion.tipo(), habitacion.precio());
    }

}// FIN DE LA CLASE RESERVAMAPPER
