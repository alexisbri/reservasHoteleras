package com.proyecto.authorization.dto;

public record CustomErrorResponse(
        int codigo,
        String mensaje
) { }
