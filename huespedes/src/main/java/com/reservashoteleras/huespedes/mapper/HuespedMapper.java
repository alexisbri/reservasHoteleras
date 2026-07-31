package com.reservashoteleras.huespedes.mapper;

import com.reservashoteleras.commons.dto.huespedes.HuespedRequest;
import com.reservashoteleras.commons.dto.huespedes.HuespedResponse;
import com.reservashoteleras.commons.mapper.CommonMapper;
import com.reservashoteleras.huespedes.entity.Huesped;
import org.springframework.stereotype.Component;

@Component
public class HuespedMapper implements CommonMapper<HuespedRequest, HuespedResponse, Huesped> {

    @Override
    public Huesped requestAEntidad(HuespedRequest request) {
        if (request == null) return null;

        return Huesped.crear(
                request.nombre(),
                request.apellidoPaterno(),
                request.apellidoMaterno(),
                request.email(),
                request.telefono(),
                request.documento(),
                request.nacionalidad());
    }

    @Override
    public HuespedResponse entidadAResponse(Huesped entidad) {
        if (entidad == null) return null;

        return new HuespedResponse(
                entidad.getId(),
                String.join(" ", entidad.getNombre(), entidad.getApellidoPaterno(), entidad.getApellidoMaterno()),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getDocumento(),
                entidad.getNacionalidad(),
                entidad.getEstadoRegistro() == null ? null : entidad.getEstadoRegistro().name());
    }
}
