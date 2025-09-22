package com.saludvida.api.service;

import com.saludvida.api.model.Consultorio;
import com.saludvida.api.repository.ConsultorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultorioService {

    private final ConsultorioRepository consultorioRepository;

    // Método para obtener todos los consultorios de la base de datos
    public List<Consultorio> obtenerTodos() {
        return consultorioRepository.findAll();
    }
}