package com.reservashoteleras.commons.dto.habitaciones;

import java.math.BigDecimal;

public record DatosHabitacion(
        Integer numero,
        String tipo,
        BigDecimal precio
) {
}
