package com.reservashoteleras.huespedes.service;

import com.reservashoteleras.commons.dto.huespedes.HuespedRequest;
import com.reservashoteleras.commons.dto.huespedes.HuespedResponse;
import com.reservashoteleras.commons.service.CrudService;

public interface HuespedService extends CrudService<HuespedRequest, HuespedResponse> {

    HuespedResponse obtenerHuespedActivoPorId(Long id);

    HuespedResponse obtenerHuespedSinEstadoPorId(Long id);

}
