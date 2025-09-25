INSERT IGNORE INTO `roles` (`id_rol`, `nombre`, `descripcion`) VALUES
(1, 'ADMIN', 'Administrador del sistema con todos los permisos'),
(2, 'RECEPCIONISTA', 'Usuario de recepción para gestionar pacientes y citas'),
(3, 'MEDICO', 'Usuario médico para registrar consultas y diagnósticos');

INSERT IGNORE INTO `usuarios` (`id_usuario`, `nombre_usuario`, `clave`, `estado`, `id_rol`) VALUES
(1, 'admin', '$2b$12$tVebN1WpmhtD48TQDi7W1ea1dNYOF2iig/OAa4sClnHvu4w.Ygqo6', 'ACTIVO', 1);
INSERT IGNORE INTO `usuarios` (`id_usuario`, `nombre_usuario`, `clave`, `estado`, `id_rol`) VALUES
(2, 'recepcionista', '$2b$12$p5StX2XT8QTz4sT0rM0vfeHIy2RDAjhAwTDsmUUXvwnE3VBkULL5q', 'ACTIVO', 2);

-- Datos de prueba para consultorios
INSERT IGNORE INTO `consultorios` (`id_consultorio`, `numero`, `piso`, `descripcion`, `estado`) VALUES
(1, '101', 1, 'Consultorio de Cardiología', 'Disponible'),
(2, '102', 1, 'Consultorio de Dermatología', 'Disponible'),
(3, '201', 2, 'Consultorio de Pediatría', 'Ocupado'),
(4, '202', 2, 'Consultorio de Traumatología', 'Mantenimiento'),
(5, '301', 3, 'Consultorio de Ginecología', 'Disponible'),
(6, '302', 3, 'Consultorio de Oftalmología', 'Disponible');

-- Datos de prueba para pacientes
INSERT IGNORE INTO `pacientes` (`id_paciente`, `nombres`, `apellidos`, `dni`, `fecha_nacimiento`, `telefono`, `email`, `direccion`, `estado`) VALUES
(1, 'Juan', 'Pérez', '12345678', '1985-05-15', '555-0101', 'juan.perez@email.com', 'Calle Ficticia 123', 'ACTIVO'),
(2, 'María', 'González', '87654321', '1990-08-22', '555-0102', 'maria.gonzalez@email.com', 'Avenida Principal 456', 'ACTIVO'),
(3, 'Carlos', 'Rodríguez', '11111111', '1978-12-10', '555-0103', 'carlos.rodriguez@email.com', 'Plaza Central 789', 'ACTIVO'),
(4, 'Ana', 'Martínez', '55555555', '1995-03-30', '555-0104', 'ana.martinez@email.com', 'Barrio Norte 321', 'ACTIVO'),
(5, 'Pedro', 'López', '99999999', '1982-07-18', '555-0105', 'pedro.lopez@email.com', 'Zona Sur 654', 'ACTIVO'),
(6, 'Laura', 'García', '77777777', '1998-11-25', '555-0106', 'laura.garcia@email.com', 'Centro Histórico 987', 'ACTIVO');

-- Datos de prueba para médicos
INSERT IGNORE INTO `medicos` (`id_medico`, `dni`, `nombres`, `apellidos`, `sexo`, `especialidad`, `telefono`, `email`, `licencia_medica`, `estado`) VALUES
(1, '11223344', 'Dr. Roberto', 'Sánchez', 'Masculino', 'Cardiología', '555-0201', 'roberto.sanchez@clinica.com', 'LIC001', 'Activo'),
(2, '22334455', 'Dra. Carmen', 'López', 'Femenino', 'Dermatología', '555-0202', 'carmen.lopez@clinica.com', 'LIC002', 'Activo'),
(3, '33445566', 'Dr. Miguel', 'Torres', 'Masculino', 'Pediatría', '555-0203', 'miguel.torres@clinica.com', 'LIC003', 'Activo'),
(4, '44556677', 'Dra. Patricia', 'Ramírez', 'Femenino', 'Traumatología', '555-0204', 'patricia.ramirez@clinica.com', 'LIC004', 'Activo'),
(5, '55667788', 'Dr. Alejandro', 'Morales', 'Masculino', 'Ginecología', '555-0205', 'alejandro.morales@clinica.com', 'LIC005', 'Activo'),
(6, '66778899', 'Dra. Isabel', 'Fernández', 'Femenino', 'Oftalmología', '555-0206', 'isabel.fernandez@clinica.com', 'LIC006', 'Activo');

-- Datos de prueba para turnos
INSERT IGNORE INTO `turnos` (`id_turno`, `id_paciente`, `id_consultorio`, `fecha`, `hora`, `motivo`, `observaciones`, `estado`) VALUES
(1, 1, 1, '2025-12-25', '10:00:00', 'Control de rutina', 'Paciente con hipertensión controlada', 'Pendiente'),
(2, 2, 4, '2025-12-26', '14:30:00', 'Consulta prenatal', 'Primer trimestre', 'Confirmado'),
(3, 3, 5, '2025-12-27', '09:15:00', 'Revisión de fractura', 'Control post-operatorio', 'EnProceso');

-- Datos de prueba para citas
INSERT IGNORE INTO `citas` (`id_cita`, `id_paciente`, `id_medico`, `id_consultorio`, `fecha`, `hora`, `motivo`, `observaciones`, `estado`) VALUES
(1, 1, 1, 1, '2025-12-25', '10:00:00', 'Control de rutina', 'Paciente con hipertensión controlada', 'Pendiente'),
(2, 2, 4, 4, '2025-12-26', '14:30:00', 'Consulta prenatal', 'Primer trimestre', 'Confirmada'),
(3, 3, 5, 5, '2025-12-27', '09:15:00', 'Revisión de fractura', 'Control post-operatorio', 'EnProceso'),
(4, 4, 2, 2, '2025-12-28', '11:00:00', 'Consulta dermatológica', 'Revisión de lunar sospechoso', 'Pendiente'),
(5, 5, 3, 3, '2025-12-29', '15:30:00', 'Control pediátrico', 'Vacunación de rutina', 'Confirmada'),
(6, 6, 6, 6, '2025-12-30', '08:45:00', 'Examen de la vista', 'Control anual de oftalmología', 'Pendiente');
