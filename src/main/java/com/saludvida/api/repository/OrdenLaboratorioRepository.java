package com.saludvida.api.repository;

import com.saludvida.api.model.OrdenLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrdenLaboratorioRepository extends JpaRepository<OrdenLaboratorio, Integer> {

    // Para el médico: ver todas las órdenes de una historia
    List<OrdenLaboratorio> findByHistoriaClinica_IdHistoriaClinicaOrderByFechaOrdenDesc(Integer idHistoriaClinica);

    // Para el laboratorista: ver órdenes que no están completadas
    @Query("SELECT o FROM OrdenLaboratorio o " +
            "JOIN FETCH o.historiaClinica hc " +
            "JOIN FETCH hc.paciente p " +
            "JOIN FETCH o.medico m " +
            "WHERE o.estado IN (com.saludvida.api.model.OrdenLaboratorio.EstadoOrden.PENDIENTE, com.saludvida.api.model.OrdenLaboratorio.EstadoOrden.EN_PROCESO) "
            +
            "ORDER BY o.fechaOrden ASC")
    List<OrdenLaboratorio> findOrdenesPendientesYEnProcesoConInfo();

}