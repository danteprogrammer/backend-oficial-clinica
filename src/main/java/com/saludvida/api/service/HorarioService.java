package com.saludvida.api.service;

import com.saludvida.api.dto.HorarioRequestDto;
import com.saludvida.api.model.Horario;
import com.saludvida.api.model.Medico;
import com.saludvida.api.repository.HorarioRepository;
import com.saludvida.api.repository.MedicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioService {

    private final HorarioRepository horarioRepository;
    private final MedicoRepository medicoRepository;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Transactional(readOnly = true)
    public List<Horario> getHorariosPorMedico(Integer idMedico) {
        return horarioRepository.findByMedico_IdMedico(idMedico);
    }

    @Transactional
    public Horario crearHorario(HorarioRequestDto dto) {
        Medico medico = medicoRepository.findById(dto.getIdMedico())
                .orElseThrow(() -> new EntityNotFoundException("Médico no encontrado con ID: " + dto.getIdMedico()));

        Horario horario = new Horario();
        horario.setMedico(medico);
        horario.setDiaSemana(dto.getDiaSemana());
        horario.setHoraInicio(LocalTime.parse(dto.getHoraInicio(), TIME_FORMATTER));
        horario.setHoraFin(LocalTime.parse(dto.getHoraFin(), TIME_FORMATTER));

        return horarioRepository.save(horario);
    }

    @Transactional
    public void eliminarHorario(Integer idHorario) {
        if (!horarioRepository.existsById(idHorario)) {
            throw new EntityNotFoundException("Horario no encontrado con ID: " + idHorario);
        }
        horarioRepository.deleteById(idHorario);
    }
}