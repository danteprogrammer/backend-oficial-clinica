INSERT IGNORE INTO `roles` (`id_rol`, `nombre`, `descripcion`) VALUES
(1, 'ADMIN', 'Administrador del sistema con todos los permisos'),
(2, 'RECEPCIONISTA', 'Usuario de recepción para gestionar pacientes y citas'),
(3, 'MEDICO', 'Usuario médico para registrar consultas y diagnósticos');

INSERT IGNORE INTO `usuarios` (`id_usuario`, `nombre_usuario`, `clave`, `estado`, `id_rol`) VALUES
(1, 'admin', '$2b$12$tVebN1WpmhtD48TQDi7W1ea1dNYOF2iig/OAa4sClnHvu4w.Ygqo6', 'ACTIVO', 1);
INSERT IGNORE INTO `usuarios` (`id_usuario`, `nombre_usuario`, `clave`, `estado`, `id_rol`) VALUES
(2, 'recepcionista', '$2b$12$p5StX2XT8QTz4sT0rM0vfeHIy2RDAjhAwTDsmUUXvwnE3VBkULL5q', 'ACTIVO', 2);