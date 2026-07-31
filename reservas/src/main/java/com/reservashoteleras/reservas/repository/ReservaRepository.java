package com.reservashoteleras.reservas.repository;

import com.reservashoteleras.commons.enums.EstadoRegistro;
import com.reservashoteleras.commons.enums.EstadoReserva;
import com.reservashoteleras.reservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    List<Reserva> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Reserva> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    boolean existsByIdHuespedAndEstadoRegistroAndEstadoReserva(
            Long idHuesped, EstadoRegistro estadoRegistro, EstadoReserva estadoReserva);

}// FIN DE LA CLASE
