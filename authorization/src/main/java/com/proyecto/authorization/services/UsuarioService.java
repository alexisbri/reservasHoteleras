package com.proyecto.authorization.services;

import com.proyecto.authorization.dto.UsuarioRequest;
import com.proyecto.authorization.dto.UsuarioResponse;

import java.util.Set;

public interface UsuarioService {

    Set<UsuarioResponse> listar();

    UsuarioResponse registrar(UsuarioRequest request);

    UsuarioResponse eliminar(String username);
}
