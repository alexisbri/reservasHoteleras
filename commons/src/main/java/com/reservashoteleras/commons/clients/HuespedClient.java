package com.reservashoteleras.commons.clients;

import com.reservashoteleras.commons.dto.huespedes.HuespedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "huespedes")
public interface HuespedClient {

    @GetMapping("/internos/{id}/activo")
    HuespedResponse obtenerHuespedActivoPorId(@PathVariable Long id);

    @GetMapping("/internos/{id}")
    HuespedResponse obtenerHuespedSinEstadoPorId(@PathVariable Long id);

}// FIN DE LA CLASE
