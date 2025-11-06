package com.saludvida.api.service;

import com.saludvida.api.dto.OrdenLabRequestDto;
import com.saludvida.api.dto.OrdenLaboratorioResponseDto;
import com.saludvida.api.dto.ResultadosLabRequestDto;
import com.saludvida.api.model.HistoriaClinica;
import com.saludvida.api.model.Medico;
import com.saludvida.api.model.OrdenLaboratorio;
import com.saludvida.api.repository.HistoriaClinicaRepository;
import com.saludvida.api.repository.MedicoRepository;
import com.saludvida.api.repository.OrdenLaboratorioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LaboratorioService {

    private final OrdenLaboratorioRepository ordenLaboratorioRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;
    private final MedicoRepository medicoRepository;

    @Transactional
    public OrdenLaboratorio crearOrden(OrdenLabRequestDto request) {
        HistoriaClinica historia = historiaClinicaRepository.findById(request.getIdHistoriaClinica())
                .orElseThrow(() -> new EntityNotFoundException("Historia Clínica no encontrada"));

        Medico medico = medicoRepository.findById(request.getIdMedico())
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado"));

        OrdenLaboratorio orden = OrdenLaboratorio.builder()
                .historiaClinica(historia)
                .medico(medico)
                .examenesSolicitados(request.getExamenesSolicitados())
                .build();

        return ordenLaboratorioRepository.save(orden);
    }

    @Transactional(readOnly = true)
    public List<OrdenLaboratorio> obtenerOrdenesPorHistoria(Integer idHistoria) {
        return ordenLaboratorioRepository.findByHistoriaClinica_IdHistoriaClinicaOrderByFechaOrdenDesc(idHistoria);
    }

    @Transactional(readOnly = true)
    public List<OrdenLaboratorioResponseDto> obtenerOrdenesPendientes() {
        List<OrdenLaboratorio> ordenes = ordenLaboratorioRepository.findOrdenesPendientesYEnProcesoConInfo();

        return ordenes.stream()
                .map(OrdenLaboratorioResponseDto::new) 
                .collect(Collectors.toList());
    }

    @Transactional
    public OrdenLaboratorio actualizarEstado(Integer idOrden, String nuevoEstado) {
        OrdenLaboratorio orden = ordenLaboratorioRepository.findById(idOrden)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));

        try {
            OrdenLaboratorio.EstadoOrden estado = OrdenLaboratorio.EstadoOrden.valueOf(nuevoEstado.toUpperCase());
            orden.setEstado(estado);
            return ordenLaboratorioRepository.save(orden);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
        }
    }

    @Transactional
    public OrdenLaboratorio registrarResultados(Integer idOrden, ResultadosLabRequestDto request) {
        OrdenLaboratorio orden = ordenLaboratorioRepository.findById(idOrden)
                .orElseThrow(() -> new EntityNotFoundException("Orden no encontrada"));

        orden.setResultados(request.getResultados());
        orden.setEstado(OrdenLaboratorio.EstadoOrden.COMPLETADO);
        orden.setFechaResultados(LocalDate.now());

        return ordenLaboratorioRepository.save(orden);
    }
}