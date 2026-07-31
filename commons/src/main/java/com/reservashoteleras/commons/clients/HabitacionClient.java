package com.reservashoteleras.commons.clients;

import com.reservashoteleras.commons.dto.habitaciones.HabitacionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "habitaciones")
public interface HabitacionClient {

    @GetMapping("/internos/{id}/activa-disponible")
    HabitacionResponse obtenerHabitacionActivaDisponiblePorId(@PathVariable Long id);

    @GetMapping("/internos/{id}")
    HabitacionResponse obtenerHabitacionSinEstadoPorId(@PathVariable Long id);

    @PutMapping("/internos/{id}/estado/{idEstado}")
    void actualizarEstadoHabitacion(@PathVariable Long id, @PathVariable Long idEstado);

}// FIN DE LA CLASE
