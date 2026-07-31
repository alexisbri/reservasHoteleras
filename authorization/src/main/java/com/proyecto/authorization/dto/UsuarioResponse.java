package com.proyecto.authorization.dto;

import java.util.Set;

public record UsuarioResponse(
        String username,
        Set<String> roles
) {}
