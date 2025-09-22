package com.saludvida.api.repository;

import com.saludvida.api.model.Consultorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultorioRepository extends JpaRepository<Consultorio, Integer> {
    // La interfaz JpaRepository ya provee métodos como findAll()
    // para obtener todos los registros, por lo que no necesitas definir nada aquí.
}