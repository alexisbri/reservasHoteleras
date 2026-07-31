package com.reservashoteleras.reservas.dto;

import com.reservashoteleras.commons.dto.habitaciones.DatosHabitacion;
import com.reservashoteleras.commons.dto.huespedes.DatosHuesped;

import java.time.LocalDate;

public record ReservaResponse(
        Long id,
        DatosHuesped huesped,
        DatosHabitacion habitacion,
        LocalDate fechaEntrada,
        LocalDate fechaSalida,
        String estadoReserva,
        String estadoRegistro
) {
}
