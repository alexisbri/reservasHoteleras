package com.reservashoteleras.habitaciones.mapper;

import com.reservashoteleras.commons.dto.habitaciones.HabitacionRequest;
import com.reservashoteleras.commons.dto.habitaciones.HabitacionResponse;
import com.reservashoteleras.commons.mapper.CommonMapper;
import com.reservashoteleras.habitaciones.entity.Habitacion;
import org.springframework.stereotype.Component;

@Component
public class HabitacionMapper implements CommonMapper<HabitacionRequest, HabitacionResponse, Habitacion> {

    @Override
    public Habitacion requestAEntidad(HabitacionRequest request) {
        if (request == null) return null;

        return Habitacion.crear(request.numero(), request.tipo(), request.precio(), request.capacidad());
    }

    @Override
    public HabitacionResponse entidadAResponse(Habitacion entidad) {
        if (entidad == null) return null;

        return new HabitacionResponse(
                entidad.getId(),
                entidad.getNumero(),
                entidad.getTipo(),
                entidad.getPrecio(),
                entidad.getCapacidad(),
                entidad.getEstadoHabitacion() == null ? null : entidad.getEstadoHabitacion().name(),
                entidad.getEstadoRegistro() == null ? null : entidad.getEstadoRegistro().name());
    }
}
