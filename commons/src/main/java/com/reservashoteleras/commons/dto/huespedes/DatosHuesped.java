package com.reservashoteleras.commons.dto.huespedes;

/**
 * Resumen del huésped, usado dentro de la respuesta de una Reserva.
 */
public record DatosHuesped(
        String nombreCompleto,
        String documento,
        String telefono
) {
}
