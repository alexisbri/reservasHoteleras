package com.reservashoteleras.commons.enums;

import com.reservashoteleras.commons.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum EstadoHabitacion {

    DISPONIBLE(1L, "Lista para asignarse"),
    OCUPADA(2L, "Asignada a una reserva"),
    LIMPIEZA(3L, "En limpieza"),
    MANTENIMIENTO(4L, "En reparación");

    private final Long codigo;
    private final String descripcion;

    public static EstadoHabitacion obtenerPorCodigo(Long codigo) {
        for (EstadoHabitacion estado : values()) {
            if (Objects.equals(estado.codigo, codigo)) {
                return estado;
            }
        }
        throw new RecursoNoEncontradoException("Código de estado de habitación no válido: " + codigo);
    }

}
