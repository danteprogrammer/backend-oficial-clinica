package com.saludvida.api.repository;

import com.saludvida.api.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository; 

import java.util.List; 

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {

    List<Consulta> findAllByHistoriaClinica_IdHistoriaClinicaOrderByFechaConsultaDesc(Integer idHistoriaClinica);
}