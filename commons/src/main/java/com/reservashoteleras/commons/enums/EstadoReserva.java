package com.reservashoteleras.commons.enums;

import com.reservashoteleras.commons.exceptions.RecursoNoEncontradoException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@Getter
public enum EstadoReserva {

    CONFIRMADA(1L, "Reserva creada") {
        @Override
        public Set<EstadoReserva> puedeCambiar() {
            return EnumSet.of(EN_CURSO, CANCELADA);
        }
    },
    EN_CURSO(2L, "Check-in realizado") {
        @Override
        public Set<EstadoReserva> puedeCambiar() {
            return EnumSet.of(FINALIZADA);
        }
    },
    FINALIZADA(3L, "Check-out realizado") {
        @Override
        public Set<EstadoReserva> puedeCambiar() {
            return Set.of();
        }
    },
    CANCELADA(4L, "Reserva cancelada") {
        @Override
        public Set<EstadoReserva> puedeCambiar() {
            return Set.of();
        }
    };

    private final Long codigo;
    private final String descripcion;

    public abstract Set<EstadoReserva> puedeCambiar();

    public boolean puedeCambiarA(EstadoReserva nuevoEstado) {
        return this.puedeCambiar().contains(nuevoEstado);
    }

    public static EstadoReserva obtenerPorCodigo(Long codigo) {
        for (EstadoReserva estado : values()) {
            if (Objects.equals(estado.codigo, codigo)) {
                return estado;
            }
        }
        throw new RecursoNoEncontradoException("Código de estado de reserva no válido: " + codigo);
    }

}
