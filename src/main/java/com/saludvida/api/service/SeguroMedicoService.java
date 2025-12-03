package com.saludvida.api.service;

import com.saludvida.api.dto.ValidacionSeguroResponse;
import com.saludvida.api.model.Paciente;
import com.saludvida.api.model.SeguroMedico;
import com.saludvida.api.repository.PacienteRepository;
import com.saludvida.api.repository.SeguroMedicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.saludvida.api.dto.DatosSeguroDto;

@Service
@RequiredArgsConstructor
public class SeguroMedicoService {

    private final PacienteRepository pacienteRepository;
    private final SeguroMedicoRepository seguroMedicoRepository;

    public ValidacionSeguroResponse validarSeguroPorPacienteId(Integer idPaciente, DatosSeguroDto datosSeguroInput) {
        Paciente paciente = pacienteRepository.findById(idPaciente)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado con ID: " + idPaciente));

        SeguroMedico seguroRegistrado = seguroMedicoRepository.findByPaciente_IdPaciente(idPaciente).orElse(null);

        String aseguradora = datosSeguroInput != null && datosSeguroInput.getNombreAseguradora() != null
                ? datosSeguroInput.getNombreAseguradora()
                : (seguroRegistrado != null ? seguroRegistrado.getNombreAseguradora() : null);
        
        String poliza = datosSeguroInput != null && datosSeguroInput.getNumeroPoliza() != null
                ? datosSeguroInput.getNumeroPoliza()
                : (seguroRegistrado != null ? seguroRegistrado.getNumeroPoliza() : null);
        
        String cobertura = datosSeguroInput != null && datosSeguroInput.getCobertura() != null
                ? datosSeguroInput.getCobertura()
                : (seguroRegistrado != null ? seguroRegistrado.getCobertura() : null);

        DatosSeguroDto datosSeguroRespuesta = new DatosSeguroDto(aseguradora, poliza, cobertura);

        try {
            String dni = paciente.getDni();
            int ultimoDigito = Integer.parseInt(dni.substring(dni.length() - 1));

            if (ultimoDigito % 2 == 0) {
                if (datosSeguroInput != null && datosSeguroInput.getNombreAseguradora() != null) {
                    SeguroMedico seguroParaGuardar = seguroRegistrado != null ? seguroRegistrado : new SeguroMedico();
                    seguroParaGuardar.setPaciente(paciente);
                    seguroParaGuardar.setNombreAseguradora(aseguradora);
                    seguroParaGuardar.setNumeroPoliza(poliza);
                    seguroParaGuardar.setCobertura(cobertura);
                    seguroMedicoRepository.save(seguroParaGuardar);
                }
                return new ValidacionSeguroResponse("Válido", "Cobertura del seguro activa.", datosSeguroRespuesta);
            } else {
                return new ValidacionSeguroResponse("Inválido",
                        "La póliza del seguro ha expirado o no tiene cobertura.", datosSeguroRespuesta);
            }
        } catch (Exception e) {
            return new ValidacionSeguroResponse("Error", "No se pudo validar el seguro del paciente.",
                    datosSeguroRespuesta);
        }
    }
}