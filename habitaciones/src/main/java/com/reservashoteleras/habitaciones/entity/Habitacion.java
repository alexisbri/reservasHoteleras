package com.reservashoteleras.habitaciones.entity;

import com.reservashoteleras.commons.enums.EstadoHabitacion;
import com.reservashoteleras.commons.enums.EstadoRegistro;
import com.reservashoteleras.commons.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "HABITACIONES")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Habitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HABITACION")
    private Long id;

    @Column(name = "NUMERO", nullable = false)
    private Integer numero;

    @Column(name = "TIPO", nullable = false, length = 30)
    private String tipo;

    @Column(name = "PRECIO", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "CAPACIDAD", nullable = false)
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_HABITACION", nullable = false, length = 20)
    private EstadoHabitacion estadoHabitacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false, length = 30)
    private EstadoRegistro estadoRegistro;

    public static Habitacion crear(Integer numero, String tipo, BigDecimal precio, Integer capacidad) {

        validarDatos(numero, tipo, precio, capacidad);

        return Habitacion.builder()
                .numero(numero)
                .tipo(tipo.trim())
                .precio(precio)
                .capacidad(capacidad)
                .estadoHabitacion(EstadoHabitacion.DISPONIBLE)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    public void actualizar(Integer numero, String tipo, BigDecimal precio, Integer capacidad) {
        validarNoEliminada();

        validarDatos(numero, tipo, precio, capacidad);

        this.numero = numero;
        this.tipo = tipo.trim();
        this.precio = precio;
        this.capacidad = capacidad;
    }

    public void cambiarEstado(EstadoHabitacion nuevoEstado) {
        validarNoEliminada();

        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado de la habitación es requerido");
        }

        if (this.estadoHabitacion == EstadoHabitacion.OCUPADA && nuevoEstado == EstadoHabitacion.DISPONIBLE) {
            throw new IllegalStateException(
                    "No se puede cambiar manualmente a DISPONIBLE una habitación OCUPADA");
        }

        this.estadoHabitacion = nuevoEstado;
    }

    public void sincronizarEstado(EstadoHabitacion nuevoEstado) {
        validarNoEliminada();

        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado de la habitación es requerido");
        }

        this.estadoHabitacion = nuevoEstado;
    }

    public void eliminar() {
        validarNoEliminada();

        if (this.estadoHabitacion == EstadoHabitacion.OCUPADA) {
            throw new IllegalStateException("No se puede eliminar una habitación OCUPADA");
        }

        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    private void validarNoEliminada() {
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO) {
            throw new IllegalStateException("La habitación ya está eliminada");
        }
    }

    private static void validarDatos(Integer numero, String tipo, BigDecimal precio, Integer capacidad) {
        if (numero == null || numero <= 0) {
            throw new IllegalArgumentException("El número de habitación es requerido y debe ser mayor a 0");
        }

        StringCustomUtils.validarTamanio(tipo, 1, 30,
                "El tipo de habitación es requerido y debe tener máximo 30 caracteres");

        if (precio == null || precio.signum() <= 0) {
            throw new IllegalArgumentException("El precio es requerido y debe ser mayor a 0");
        }

        if (capacidad == null || capacidad < 1) {
            throw new IllegalArgumentException("La capacidad es requerida y debe ser mínimo 1");
        }
    }
}

