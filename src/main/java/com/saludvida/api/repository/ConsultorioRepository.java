package com.saludvida.api.repository;

import com.saludvida.api.model.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Integer> {

    List<Consultorio> findByEstado(Consultorio.Estado estado);

    @Query("SELECT c FROM Consultorio c WHERE c.estado = 'Disponible'")
    List<Consultorio> findConsultoriosDisponibles();

    @Query("SELECT c FROM Consultorio c WHERE c.numero = :numero AND c.piso = :piso")
    Optional<Consultorio> findByNumeroAndPiso(@Param("numero") String numero, @Param("piso") Integer piso);
}
