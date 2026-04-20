-- =======================================================
-- 0. LIMPIEZA DE DATOS
-- =======================================================
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE reviews;
TRUNCATE TABLE user_roles;
TRUNCATE TABLE user_profiles;
TRUNCATE TABLE budget_lines;
TRUNCATE TABLE budgets;
TRUNCATE TABLE repairs;
TRUNCATE TABLE appointments;
TRUNCATE TABLE workshop_services;
TRUNCATE TABLE services;
TRUNCATE TABLE workshops;
TRUNCATE TABLE mechanics;
TRUNCATE TABLE vehicles;
TRUNCATE TABLE brands;
TRUNCATE TABLE users;
TRUNCATE TABLE roles;
SET FOREIGN_KEY_CHECKS = 1;

-- =======================================================
-- 1. DATOS DE SEGURIDAD
-- =======================================================
INSERT IGNORE INTO roles (id, name, display_name, description) VALUES
(1, 'ROLE_ADMIN', 'Administrador', 'Acceso total a la gestión del taller'),
(2, 'ROLE_CLIENT', 'Cliente', 'Usuario cliente de Taller Go');

INSERT IGNORE INTO users (id, email, password_hash, active, account_non_locked, last_password_change, password_expires_at, failed_login_attempts, email_verified, must_change_password) VALUES
(1, 'leo@email.com', '$2a$12$HnF3pSI.kpCNujmMgcQDA.fbGt2TFPbmMDH.rT4wUKLvOKEzsvlTC', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE, FALSE),
(2, 'vidal@email.com', '$2a$12$HnF3pSI.kpCNujmMgcQDA.fbGt2TFPbmMDH.rT4wUKLvOKEzsvlTC', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE, FALSE),
(3, 'maria@email.com', '$2a$12$HnF3pSI.kpCNujmMgcQDA.fbGt2TFPbmMDH.rT4wUKLvOKEzsvlTC', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE, FALSE),
(4, 'jmorpat', '$2a$12$HnF3pSI.kpCNujmMgcQDA.fbGt2TFPbmMDH.rT4wUKLvOKEzsvlTC', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE, FALSE),
(5, 'VidalB46', '$2a$12$HnF3pSI.kpCNujmMgcQDA.fbGt2TFPbmMDH.rT4wUKLvOKEzsvlTC', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE, FALSE);

INSERT IGNORE INTO user_roles (user_id, role_id) VALUES (1, 1), (2, 1), (3, 2);

INSERT IGNORE INTO user_profiles (user_id, first_name, last_name, phone_number, locale) VALUES
(1, 'Leo', 'Morillo', '600111222', 'es_ES'),
(2, 'Vidal', 'Bañez', '600333444', 'es_ES'),
(3, 'Maria', 'Lopez', '600555666', 'es_ES');

-- =======================================================
-- 2. DATOS DE TALLER GO (Entidades Maestras)
-- =======================================================
INSERT IGNORE INTO brands (id, name, country) VALUES
(1, 'SEAT', 'España'), (2, 'RENAULT', 'Francia'), (3, 'PEUGEOT', 'Francia'),
(4, 'FORD', 'EEUU'), (5, 'TOYOTA', 'Japón'), (6, 'VOLKSWAGEN', 'Alemania'),
(7, 'BMW', 'Alemania'), (8, 'AUDI', 'Alemania');

INSERT IGNORE INTO vehicles (id, brand_id, user_id, model, vin, color, year, km, matricula) VALUES
(1, 1, 1, 'Leon', 'VBN1234567890', 'Blanco', 2018, 45000, '1234ABC'),
(2, 2, 1, 'Clio', 'VBN0987654321', 'Gris', 2016, 82000, '2345BCD'),
(3, 5, 2, 'Corolla', 'TYT1122334455', 'Negro', 2020, 23000, '3456CDE'),
(4, 6, 3, 'Golf', 'VKS9988776655', 'Azul', 2015, 120000, '4567DEF'),
(5, 4, 3, 'Focus', 'FRD4455667788', 'Rojo', 2017, 67000, '5678EFG');

INSERT IGNORE INTO workshops (id, nif, name, phone, location, email, schedule) VALUES
(1, 'B12345678', 'Taller Mecánica Rápida', '910001122', 'Calle Motor 4', 'info@mecanica.com', '09:00 - 18:00'),
(2, 'B87654321', 'Garaje Oficial Vidal', '930003344', 'Av. Central 20', 'taller@vidal.com', '08:00 - 19:00');

INSERT IGNORE INTO mechanics (id, name, specialty, workshop_id) VALUES
(1, 'Carlos López', 'Motor', 1), (2, 'Ana Martín', 'Electricidad', 1),
(3, 'Luis García', 'Neumáticos', 2), (4, 'Marta Pérez', 'Carrocería', 2),
(5, 'Javier Torres', 'General', 1);

INSERT IGNORE INTO services (id, name) VALUES 
(1, 'Cambio de Aceite'), (2, 'Revisión General'), (3, 'Cambio de Neumáticos'), (4, 'Reparación Frenos');

INSERT IGNORE INTO workshop_services (id, workshop_id, service_id, price, duration_minutes) VALUES
(1, 1, 1, 50.00, 45), (2, 1, 4, 120.00, 120), (3, 2, 2, 80.00, 60), (4, 2, 3, 200.00, 90);

-- =======================================================
-- 3. FLUJO DE PRUEBA (Cita -> Reparación -> Presupuesto)
-- =======================================================
INSERT IGNORE INTO appointments (id, user_id, workshop_id, vehicle_id, start_date, end_date, status, notes) VALUES
(1, 1, 1, 1, '2025-10-20 09:00:00', '2025-10-20 11:00:00', 'EN_TALLER', 'Ruido al frenar');

INSERT IGNORE INTO repairs (id, appointment_id, vehicle_id, entry_date, status, notes) VALUES
(1, 1, 1, '2025-10-20', 'STANDBY', 'Pastillas de freno desgastadas');

-- Presupuesto pendiente de aceptar
INSERT IGNORE INTO budgets (id, repair_id, total_gross, total_net, accepted) VALUES
(1, 1, 100.00, 121.00, FALSE);