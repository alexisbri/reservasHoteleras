package com.reservashoteleras.reservas.entity;

import com.reservashoteleras.commons.enums.EstadoRegistro;
import com.reservashoteleras.commons.enums.EstadoReserva;
import com.reservashoteleras.commons.utils.ValoresNumericosUtils;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "RESERVAS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RESERVA")
    private Long id;

    @Column(name = "ID_HABITACION", nullable = false)
    private Long idHabitacion;

    @Column(name = "ID_HUESPED", nullable = false)
    private Long idHuesped;

    // Nombre de columna tal cual fue creada en la base de datos (FECHA_ENTREDA)
    @Column(name = "FECHA_ENTREDA", nullable = false)
    private LocalDate fechaEntrada;

    @Column(name = "FECHA_SALIDA", nullable = false)
    private LocalDate fechaSalida;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_RESERVA", nullable = false, length = 20)
    private EstadoReserva estadoReserva;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO_REGISTRO", nullable = false, length = 30)
    private EstadoRegistro estadoRegistro;

    public static Reserva crear(Long idHabitacion, Long idHuesped, LocalDate fechaEntrada, LocalDate fechaSalida) {

        validarIds(idHabitacion, idHuesped);
        validarFechas(fechaEntrada, fechaSalida);

        return Reserva.builder()
                .idHabitacion(idHabitacion)
                .idHuesped(idHuesped)
                .fechaEntrada(fechaEntrada)
                .fechaSalida(fechaSalida)
                .estadoReserva(EstadoReserva.CONFIRMADA)
                .estadoRegistro(EstadoRegistro.ACTIVO)
                .build();
    }

    /**
     * Aplica las reglas de modificación de fechas según el estado actual de la reserva.
     * - CONFIRMADA: se pueden modificar fechaEntrada y fechaSalida.
     * - EN_CURSO: solo se puede modificar fechaSalida (fechaEntrada debe permanecer igual).
     * - FINALIZADA / CANCELADA: no se permite ninguna modificación.
     */
    public void actualizarFechas(Long idHabitacionSolicitado, Long idHuespedSolicitado,
                                  LocalDate nuevaFechaEntrada, LocalDate nuevaFechaSalida) {

        validarNoEliminada();

        if (!this.idHabitacion.equals(idHabitacionSolicitado) || !this.idHuesped.equals(idHuespedSolicitado)) {
            throw new IllegalStateException("No se puede cambiar la habitación ni el huésped de una reserva existente");
        }

        switch (this.estadoReserva) {
            case CONFIRMADA -> {
                validarFechas(nuevaFechaEntrada, nuevaFechaSalida);
                this.fechaEntrada = nuevaFechaEntrada;
                this.fechaSalida = nuevaFechaSalida;
            }
            case EN_CURSO -> {
                if (!this.fechaEntrada.equals(nuevaFechaEntrada)) {
                    throw new IllegalStateException(
                            "La reserva ya tiene check-in realizado: no se puede modificar la fecha de entrada");
                }
                validarFechas(this.fechaEntrada, nuevaFechaSalida);
                this.fechaSalida = nuevaFechaSalida;
            }
            default -> throw new IllegalStateException(
                    "La reserva con estado " + this.estadoReserva + " no puede modificarse");
        }
    }

    public void actualizarEstado(EstadoReserva nuevoEstado) {
        validarNoEliminada();

        if (nuevoEstado == null) {
            throw new IllegalArgumentException("El nuevo estado de la reserva es requerido");
        }

        if (!this.estadoReserva.puedeCambiarA(nuevoEstado)) {
            throw new IllegalStateException("La reserva con estado " + this.estadoReserva
                    + " solo puede cambiar a: " + this.estadoReserva.puedeCambiar());
        }

        this.estadoReserva = nuevoEstado;
    }

    public void eliminar() {
        validarNoEliminada();

        if (this.estadoReserva == EstadoReserva.CONFIRMADA || this.estadoReserva == EstadoReserva.EN_CURSO) {
            throw new IllegalStateException(
                    "No se puede eliminar una reserva " + this.estadoReserva
                            + "; cancélela o complete el check-out primero");
        }

        this.estadoRegistro = EstadoRegistro.ELIMINADO;
    }

    private void validarNoEliminada() {
        if (this.estadoRegistro == EstadoRegistro.ELIMINADO) {
            throw new IllegalStateException("La reserva ya está eliminada");
        }
    }

    private static void validarIds(Long idHabitacion, Long idHuesped) {
        ValoresNumericosUtils.validarLongPositivo(idHabitacion, "El id de la habitación es requerido y debe ser positivo");
        ValoresNumericosUtils.validarLongPositivo(idHuesped, "El id del huésped es requerido y debe ser positivo");
    }

    private static void validarFechas(LocalDate fechaEntrada, LocalDate fechaSalida) {
        if (fechaEntrada == null || fechaSalida == null) {
            throw new IllegalArgumentException("Las fechas de entrada y salida son requeridas");
        }
        if (!fechaEntrada.isBefore(fechaSalida)) {
            throw new IllegalArgumentException("La fecha de entrada debe ser anterior a la fecha de salida");
        }
    }

}// FIN DE LA CLASE RESERVA
