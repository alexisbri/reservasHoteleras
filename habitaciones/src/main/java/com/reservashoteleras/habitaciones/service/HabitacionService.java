package com.reservashoteleras.habitaciones.service;

import com.reservashoteleras.commons.dto.habitaciones.HabitacionRequest;
import com.reservashoteleras.commons.dto.habitaciones.HabitacionResponse;
import com.reservashoteleras.commons.service.CrudService;

public interface HabitacionService extends CrudService<HabitacionRequest, HabitacionResponse> {

    HabitacionResponse obtenerHabitacionPorIdSinEstado(Long id);

    void cambiarEstado(Long id, Long idEstado);

    // --- Usados internamente por Reservas vía Feign ---

    HabitacionResponse obtenerHabitacionActivaDisponiblePorId(Long id);

    void sincronizarEstado(Long id, Long idEstado);

}
