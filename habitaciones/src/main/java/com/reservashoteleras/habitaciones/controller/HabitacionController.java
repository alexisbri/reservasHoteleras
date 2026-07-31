package com.reservashoteleras.habitaciones.controller;

import com.reservashoteleras.commons.controller.CommonController;
import com.reservashoteleras.commons.dto.habitaciones.HabitacionRequest;
import com.reservashoteleras.commons.dto.habitaciones.HabitacionResponse;
import com.reservashoteleras.habitaciones.service.HabitacionService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HabitacionController extends CommonController<HabitacionRequest, HabitacionResponse, HabitacionService> {

    public HabitacionController(HabitacionService service) {
        super(service);
    }

    @GetMapping("/id-habitacion/{id}")
    public ResponseEntity<HabitacionResponse> obtenerHabitacionPorIdSinEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id) {
        return ResponseEntity.ok(service.obtenerHabitacionPorIdSinEstado(id));
    }

    @PutMapping("/{id}/estado/{idEstado}")
    public ResponseEntity<Void> cambiarEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long id,
            @PathVariable @Positive(message = "El idEstado debe ser positivo") Long idEstado) {
        service.cambiarEstado(id, idEstado);
        return ResponseEntity.noContent().build();
    }

    // --- Endpoints internos, consumidos por Reservas vía Feign ---

    @GetMapping("/internos/{id}/activa-disponible")
    public ResponseEntity<HabitacionResponse> obtenerHabitacionActivaDisponiblePorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerHabitacionActivaDisponiblePorId(id));
    }

    @GetMapping("/internos/{id}")
    public ResponseEntity<HabitacionResponse> obtenerHabitacionSinEstadoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerHabitacionPorIdSinEstado(id));
    }

    @PutMapping("/internos/{id}/estado/{idEstado}")
    public ResponseEntity<Void> sincronizarEstado(@PathVariable Long id, @PathVariable Long idEstado) {
        service.sincronizarEstado(id, idEstado);
        return ResponseEntity.noContent().build();
    }

}
