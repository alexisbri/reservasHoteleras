package com.reservashoteleras.habitaciones.repository;

import com.reservashoteleras.commons.enums.EstadoHabitacion;
import com.reservashoteleras.commons.enums.EstadoRegistro;
import com.reservashoteleras.habitaciones.entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {

    List<Habitacion> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Habitacion> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    Optional<Habitacion> findByIdAndEstadoRegistroAndEstadoHabitacion(
            Long id, EstadoRegistro estadoRegistro, EstadoHabitacion estadoHabitacion);

    boolean existsByNumeroAndEstadoRegistro(Integer numero, EstadoRegistro estadoRegistro);

    boolean existsByNumeroAndEstadoRegistroAndIdNot(Integer numero, EstadoRegistro estadoRegistro, Long id);

}
