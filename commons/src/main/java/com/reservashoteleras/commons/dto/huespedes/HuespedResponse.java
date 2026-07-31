package com.reservashoteleras.commons.dto.huespedes;

public record HuespedResponse(
        Long id,
        String nombreCompleto,
        String email,
        String telefono,
        String documento,
        String nacionalidad,
        String estadoRegistro
) {
}
