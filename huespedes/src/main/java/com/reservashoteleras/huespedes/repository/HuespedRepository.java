package com.reservashoteleras.huespedes.repository;

import com.reservashoteleras.commons.enums.EstadoRegistro;
import com.reservashoteleras.huespedes.entity.Huesped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HuespedRepository extends JpaRepository<Huesped, Long> {

    List<Huesped> findByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Huesped> findByIdAndEstadoRegistro(Long id, EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCaseAndEstadoRegistro(String email, EstadoRegistro estadoRegistro);

    boolean existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(String email, EstadoRegistro estadoRegistro, Long id);

    boolean existsByTelefonoAndEstadoRegistro(String telefono, EstadoRegistro estadoRegistro);

    boolean existsByTelefonoAndEstadoRegistroAndIdNot(String telefono, EstadoRegistro estadoRegistro, Long id);

    boolean existsByDocumentoAndEstadoRegistro(String documento, EstadoRegistro estadoRegistro);

    boolean existsByDocumentoAndEstadoRegistroAndIdNot(String documento, EstadoRegistro estadoRegistro, Long id);

}// FIN DE LA CLASE
