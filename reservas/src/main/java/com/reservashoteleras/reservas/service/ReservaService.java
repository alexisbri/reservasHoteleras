package com.reservashoteleras.reservas.service;

import com.reservashoteleras.commons.service.CrudService;
import com.reservashoteleras.reservas.dto.ReservaRequest;
import com.reservashoteleras.reservas.dto.ReservaResponse;

public interface ReservaService extends CrudService<ReservaRequest, ReservaResponse> {

    ReservaResponse cambiarEstado(Long idReserva, Long idEstado);

    // --- Usado internamente por Huéspedes vía Feign ---
    boolean tieneHuespedReservasBloqueantes(Long idHuesped);

}
