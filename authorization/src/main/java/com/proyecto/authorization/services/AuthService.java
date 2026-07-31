package com.proyecto.authorization.services;

import com.proyecto.authorization.dto.LoginRequest;
import com.proyecto.authorization.dto.TokenResponse;

public interface AuthService {

    TokenResponse autenticar(LoginRequest request) throws Exception;
}

