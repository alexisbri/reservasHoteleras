package com.reservashoteleras.commons.dto.habitaciones;

import java.math.BigDecimal;

public record HabitacionResponse(
        Long id,
        Integer numero,
        String tipo,
        BigDecimal precio,
        Integer capacidad,
        String estadoHabitacion,
        String estadoRegistro
) {
}
