package com.saludvida.api.service;

import com.saludvida.api.dto.TriajeDto;
import com.saludvida.api.model.HistoriaClinica;
import com.saludvida.api.model.Triaje;
import com.saludvida.api.model.Usuario;
import com.saludvida.api.repository.HistoriaClinicaRepository;
import com.saludvida.api.repository.TriajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class TriajeService {

    @Autowired
    private TriajeRepository triajeRepository;

    @Autowired
    private HistoriaClinicaRepository historiaClinicaRepository;

    public Triaje registrarTriaje(Long idHistoriaClinica, TriajeDto triajeDto, Usuario usuario) {
        HistoriaClinica historia = historiaClinicaRepository.findById(idHistoriaClinica)
                .orElseThrow(
                        () -> new RuntimeException("No se encontró la historia clínica con ID: " + idHistoriaClinica));

        Triaje nuevoTriaje = new Triaje();
        nuevoTriaje.setHistoriaClinica(historia);
        nuevoTriaje.setRegistradoPor(usuario);

        nuevoTriaje.setPeso(triajeDto.getPeso());
        nuevoTriaje.setAltura(triajeDto.getAltura());
        nuevoTriaje.setPresionArterial(triajeDto.getPresionArterial());
        nuevoTriaje.setTemperatura(triajeDto.getTemperatura());
        nuevoTriaje.setSaturacionOxigeno(triajeDto.getSaturacionOxigeno());

        // Calcular IMC si se proporcionan peso y altura
        if (triajeDto.getPeso() != null && triajeDto.getAltura() != null && triajeDto.getAltura() > 0) {
            double alturaEnMetros = triajeDto.getAltura() / 100.0;
            double imc = triajeDto.getPeso() / (alturaEnMetros * alturaEnMetros);
            // Redondear a 2 decimales
            BigDecimal imcRedondeado = new BigDecimal(imc).setScale(2, RoundingMode.HALF_UP);
            nuevoTriaje.setImc(imcRedondeado.doubleValue());
        }

        return triajeRepository.save(nuevoTriaje);
    }

    public List<Triaje> getTriajesPorHistoria(Long idHistoriaClinica) {
        return triajeRepository.findByHistoriaClinica_IdHistoriaClinicaOrderByFechaRegistroDesc(idHistoriaClinica);
    }
}