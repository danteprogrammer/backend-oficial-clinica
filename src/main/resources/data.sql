INSERT IGNORE INTO `roles` (`id_rol`, `nombre`, `descripcion`) VALUES
(1, 'ADMIN', 'Administrador del sistema con todos los permisos'),
(2, 'RECEPCIONISTA', 'Usuario de recepción para gestionar pacientes y citas'),
(3, 'MEDICO', 'Usuario médico para registrar consultas y diagnósticos');

INSERT IGNORE INTO `usuarios` (`id_usuario`, `nombre_usuario`, `clave`, `estado`, `id_rol`) VALUES
(1, 'admin', '$2b$12$tVebN1WpmhtD48TQDi7W1ea1dNYOF2iig/OAa4sClnHvu4w.Ygqo6', 'ACTIVO', 1);
INSERT IGNORE INTO `usuarios` (`id_usuario`, `nombre_usuario`, `clave`, `estado`, `id_rol`) VALUES
(2, 'recepcionista', '$2b$12$p5StX2XT8QTz4sT0rM0vfeHIy2RDAjhAwTDsmUUXvwnE3VBkULL5q', 'ACTIVO', 2);

INSERT IGNORE INTO `consultorios` (`id_consultorio`, `numero`, `piso`, `descripcion`, `especialidad`, `estado`) VALUES
(1, '101', 1, 'Consultorio de Cardiología', 'Cardiología', 'Disponible'),
(2, '102', 1, 'Consultorio de Dermatología', 'Dermatología', 'Disponible'),
(3, '201', 2, 'Consultorio de Pediatría', 'Pediatría', 'Disponible'),
(4, '202', 2, 'Consultorio de Traumatología', 'Traumatología', 'Mantenimiento'),
(5, '301', 3, 'Consultorio de Ginecología', 'Ginecología', 'Disponible'),
(6, '302', 3, 'Consultorio de Oftalmología', 'Oftalmología', 'Disponible');

INSERT IGNORE INTO `paciente` (`id_paciente`, `nombres`, `apellidos`, `dni`, `fecha_nacimiento`, `telefono`, `email`, `direccion`, `estado`, `sexo`) VALUES
(1, 'Juan', 'Pérez', '10234567', '1985-05-15', '961234567', 'juan.perez@email.com', 'Calle Ficticia 123', 'Activo', 'Masculino'),
(2, 'María', 'González', '20876543', '1990-08-22', '987654321', 'maria.gonzalez@email.com', 'Avenida Principal 456', 'Activo', 'Femenino'),
(3, 'Carlos', 'Rodríguez', '51720384', '1978-12-10', '954318207', 'carlos.rodriguez@email.com', 'Plaza Central 789', 'Activo', 'Masculino'),
(4, 'Ana', 'Martínez', '33091827', '1995-03-30', '965402118', 'ana.martinez@email.com', 'Barrio Norte 321', 'Activo', 'Femenino'),
(5, 'Pedro', 'López', '87450123', '1982-07-18', '999770123', 'pedro.lopez@email.com', 'Zona Sur 654', 'Activo', 'Masculino'),
(6, 'Laura', 'García', '66023489', '1998-11-25', '912345678', 'laura.garcia@email.com', 'Centro Histórico 987', 'Activo', 'Femenino');


INSERT IGNORE INTO `historiaclinica` (`id_historia_clinica`, `fecha_creacion`, `id_paciente`, `antecedentes`, `alergias`, `enfermedades_cronicas`) VALUES
(1, CURDATE(), 1, NULL, 'Polvo', 'Hipertensión'),
(2, CURDATE(), 2, 'Cirugía de apéndice', NULL, NULL),
(3, CURDATE(), 3, NULL, 'Penicilina', NULL),
(4, CURDATE(), 4, NULL, NULL, 'Asma'),
(5, CURDATE(), 5, NULL, NULL, NULL),
(6, CURDATE(), 6, 'Fractura de brazo', 'Maní', NULL);

-- Datos de prueba para médicos
INSERT IGNORE INTO `medicos` (`id_medico`, `dni`, `nombres`, `apellidos`, `sexo`, `especialidad`, `telefono`, `email`, `licencia_medica`, `estado`) VALUES
(1, '40321578', 'Dr. Roberto', 'Sánchez', 'Masculino', 'Cardiología', '976543210', 'roberto.sanchez@clinica.com', 'LIC001', 'Activo'),
(2, '72904561', 'Dra. Carmen', 'López', 'Femenino', 'Dermatología', '957012468', 'carmen.lopez@clinica.com', 'LIC002', 'Activo'),
(3, '55678902', 'Dr. Miguel', 'Torres', 'Masculino', 'Pediatría', '948601357', 'miguel.torres@clinica.com', 'LIC003', 'Activo'),
(4, '31415926', 'Dra. Patricia', 'Ramírez', 'Femenino', 'Traumatología', '959887766', 'patricia.ramirez@clinica.com', 'LIC004', 'Activo'),
(5, '31415937', 'Dr. Alejandro', 'Morales', 'Masculino', 'Ginecología', '991122334', 'alejandro.morales@clinica.com', 'LIC005', 'Activo'),
(6, '31415985', 'Dra. Isabel', 'Fernández', 'Femenino', 'Oftalmología', '923456789', 'isabel.fernandez@clinica.com', 'LIC006', 'Activo');

-- Insertar horarios para los médicos
INSERT IGNORE INTO `horarios` (`id_medico`, `dia_semana`, `hora_inicio`, `hora_fin`) VALUES
(1, 'MONDAY', '09:00:00', '13:00:00'), -- Dr. Sanchez (Cardiología) Lunes AM
(1, 'WEDNESDAY', '09:00:00', '13:00:00'), -- Dr. Sanchez (Cardiología) Miércoles AM
(2, 'TUESDAY', '14:00:00', '18:00:00'),  -- Dra. López (Dermatología) Martes PM
(2, 'THURSDAY', '14:00:00', '18:00:00'), -- Dra. López (Dermatología) Jueves PM
(3, 'FRIDAY', '08:00:00', '12:00:00'),   -- Dr. Torres (Pediatría) Viernes AM
(4, 'MONDAY', '15:00:00', '19:00:00'),  -- Dra. Ramírez (Traumatología) Lunes PM
(5, 'TUESDAY', '09:00:00', '13:00:00'),  -- Dr. Morales (Ginecología) Martes AM
(6, 'WEDNESDAY', '10:00:00', '14:00:00');-- Dra. Fernández (Oftalmología) Miércoles

INSERT IGNORE INTO `turnos` (`id_turno`, `id_paciente`, `id_consultorio`, `fecha`, `hora`, `motivo`, `observaciones`, `estado`) VALUES
(1, 1, 1, '2025-12-25', '10:00:00', 'Control de rutina', 'Paciente con hipertensión controlada', 'Pendiente'),
(2, 2, 4, '2025-12-26', '14:30:00', 'Consulta prenatal', 'Primer trimestre', 'Confirmado'),
(3, 3, 5, '2025-12-27', '09:15:00', 'Revisión de fractura', 'Control post-operatorio', 'EnProceso');

INSERT IGNORE INTO `citas` (`id_cita`, `id_paciente`, `id_medico`, `id_consultorio`, `fecha`, `hora`, `motivo`, `observaciones`, `estado`) VALUES
(1, 1, 1, 1, '2025-12-25', '10:00:00', 'Control de rutina', 'Paciente con hipertensión controlada', 'Pendiente'),
(2, 2, 4, 4, '2025-12-26', '14:30:00', 'Consulta prenatal', 'Primer trimestre', 'Confirmada'),
(3, 3, 5, 5, '2025-12-27', '09:15:00', 'Revisión de fractura', 'Control post-operatorio', 'EnProceso'),
(4, 4, 2, 2, '2025-12-28', '11:00:00', 'Consulta dermatológica', 'Revisión de lunar sospechoso', 'Pendiente'),
(5, 5, 3, 3, '2025-12-29', '15:30:00', 'Control pediátrico', 'Vacunación de rutina', 'Confirmada'),
(6, 6, 6, 6, '2025-12-30', '08:45:00', 'Examen de la vista', 'Control anual de oftalmología', 'Pendiente');

INSERT IGNORE INTO `seguromedico` (`id_seguro`, `id_paciente`, `nombre_aseguradora`, `numero_poliza`, `cobertura`) VALUES
(1, 1, 'Seguros Pacífico', 'POL-10234567', 'Plan Completo'),
(2, 2, 'Rimac Seguros', 'POL-20876543', 'Plan Ambulatorio'),
(3, 3, 'La Positiva', 'POL-51720384', 'Plan Oncológico'),
(4, 4, 'Mapfre', 'POL-33091827', 'Plan Básico');