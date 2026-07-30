package com.reservashoteleras.usuarios.repository;

import com.reservashoteleras.usuarios.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuariosRepository extends JpaRepository<Usuarios, Long> {
}
