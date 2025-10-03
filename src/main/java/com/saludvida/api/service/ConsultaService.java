package com.saludvida.api.service;

import com.saludvida.api.model.Consulta;
import com.saludvida.api.model.HistoriaClinica;
import com.saludvida.api.repository.ConsultaRepository;
import com.saludvida.api.repository.HistoriaClinicaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final HistoriaClinicaRepository historiaClinicaRepository;

    @Transactional
    public Consulta registrarConsulta(Consulta consulta, Integer idHistoriaClinica) {
        HistoriaClinica historia = historiaClinicaRepository.findById(idHistoriaClinica)
                .orElseThrow(() -> new EntityNotFoundException("Historia Clínica no encontrada"));

        consulta.setHistoriaClinica(historia);
        consulta.setFechaConsulta(LocalDate.now());

        if (consulta.getPeso() != null && consulta.getAltura() != null && consulta.getAltura() > 0) {
            double alturaEnMetros = consulta.getAltura() / 100.0;
            double imc = consulta.getPeso() / (alturaEnMetros * alturaEnMetros);
            consulta.setImc(Math.round(imc * 100.0) / 100.0); 
        }

        return consultaRepository.save(consulta);
    }
}
