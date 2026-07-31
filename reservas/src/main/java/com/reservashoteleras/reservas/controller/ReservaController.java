package com.reservashoteleras.reservas.controller;

import com.reservashoteleras.commons.controller.CommonController;
import com.reservashoteleras.reservas.dto.ReservaRequest;
import com.reservashoteleras.reservas.dto.ReservaResponse;
import com.reservashoteleras.reservas.service.ReservaService;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReservaController extends CommonController<ReservaRequest, ReservaResponse, ReservaService> {

    public ReservaController(ReservaService service) {
        super(service);
    }

    @PatchMapping("/{idReserva}/estado/{idEstado}")
    public ResponseEntity<ReservaResponse> cambiarEstado(
            @PathVariable @Positive(message = "El ID debe ser positivo") Long idReserva,
            @PathVariable @Positive(message = "El idEstado debe ser positivo") Long idEstado) {
        return ResponseEntity.ok(service.cambiarEstado(idReserva, idEstado));
    }

    // --- Endpoint interno, consumido por Huéspedes vía Feign ---

    @GetMapping("/internos/huespedes/{idHuesped}/tiene-reservas-bloqueantes")
    public ResponseEntity<Boolean> tieneHuespedReservasBloqueantes(@PathVariable Long idHuesped) {
        return ResponseEntity.ok(service.tieneHuespedReservasBloqueantes(idHuesped));
    }

}
