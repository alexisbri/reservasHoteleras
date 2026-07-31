package com.reservashoteleras.usuarios.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity // Marca la clase como una entidad para Jpa
@Table(name = "USUARIOS") // Especifica nombre exacto en DB
@Getter // Genera automáticamente los getter de todos los campos
@Builder // Habilita la creación de instancias desde otras clases
@NoArgsConstructor // Genera un constructor sin parámetros
@AllArgsConstructor // Genera un constructor con todos los parámetros
public class Usuarios {

    @Id // Marca el campo como llave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // La clave primaria se generará automáticamente
    @Column(name = "ID_USUARIO") // Especifica nombre exacto en DB
    private Long id;

    @Column(name = "USERNAME", nullable = false, length = 20)
    private String username;

    @Column(name = "PASSWORD", nullable = false, length = 300)
    private String password;

    @Enumerated(EnumType.STRING) // Solo permitirá valores indicados en el ENUM
    @Column(name = "ROL", nullable = false, length = 10)
    private String rol;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false, length = 10)
    private String estado;

}
