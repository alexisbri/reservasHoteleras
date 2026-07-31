package com.reservashoteleras.reservas.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record ReservaRequest(

        @NotNull(message = "El id del huésped es requerido")
        @Positive(message = "El id del huésped debe ser positivo")
        Long idHuesped,

        @NotNull(message = "El id de la habitación es requerido")
        @Positive(message = "El id de la habitación debe ser positivo")
        Long idHabitacion,

        @NotNull(message = "La fecha de entrada es requerida")
        LocalDate fechaEntrada,

        @NotNull(message = "La fecha de salida es requerida")
        LocalDate fechaSalida

) {
}
