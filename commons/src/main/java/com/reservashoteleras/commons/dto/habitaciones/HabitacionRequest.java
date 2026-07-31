package com.reservashoteleras.commons.dto.habitaciones;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record HabitacionRequest(

        @NotNull(message = "El número de habitación es requerido")
        @Positive(message = "El número de habitación debe ser mayor a 0")
        Integer numero,

        @NotBlank(message = "El tipo de habitación es requerido")
        @Size(max = 30, message = "El tipo debe tener máximo 30 caracteres")
        String tipo,

        @NotNull(message = "El precio es requerido")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
        BigDecimal precio,

        @NotNull(message = "La capacidad es requerida")
        @Min(value = 1, message = "La capacidad debe ser mínimo 1")
        Integer capacidad

) {
}
