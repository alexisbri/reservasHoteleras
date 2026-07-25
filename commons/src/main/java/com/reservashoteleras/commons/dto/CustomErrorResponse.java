package com.reservashoteleras.commons.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) {
}
