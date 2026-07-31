package com.reservashoteleras.commons.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "reservas")
public interface ReservaClient {

    @GetMapping("/internos/huespedes/{idHuesped}/tiene-reservas-bloqueantes")
    Boolean tieneHuespedReservasBloqueantes(@PathVariable Long idHuesped);

}// FIN DE LA CLASE
