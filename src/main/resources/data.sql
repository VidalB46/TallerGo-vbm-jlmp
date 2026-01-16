-- 1. Marcas (Independientes)
INSERT IGNORE INTO brands (id, name, country) VALUES
(1, 'SEAT', 'España'),
(2, 'RENAULT', 'Francia'),
(3, 'PEUGEOT', 'Francia'),
(4, 'FORD', 'EEUU'),
(5, 'TOYOTA', 'Japón'),
(6, 'VOLKSWAGEN', 'Alemania'),
(7, 'BMW', 'Alemania'),
(8, 'AUDI', 'Alemania');

-- 2. Usuarios
-- Usamos IGNORE para que no falle si ya existen
INSERT IGNORE INTO users (id, full_name, phone, email, password, role) VALUES
(1, 'Leo Morillo', '600111222', 'leo@email.com', 'pass123', 'CLIENT'),
(2, 'Vidal Bañez', '600333444', 'vidal@email.com', 'pass123', 'CLIENT'),
(3, 'Maria Lopez', '600555666', 'maria@email.com', 'pass123', 'CLIENT');

-- 3. Vehículos
INSERT IGNORE INTO vehicles (id, brand_id, user_id, model, vin, color, year, km, matricula) VALUES
(1, 1, 1, 'Leon', 'VBN1234567890', 'Blanco', 2018, 45000, '1234ABC'),
(2, 2, 1, 'Clio', 'VBN0987654321', 'Gris', 2016, 82000, '2345BCD'),
(3, 5, 2, 'Corolla', 'TYT1122334455', 'Negro', 2020, 23000, '3456CDE'),
(4, 6, 3, 'Golf', 'VKS9988776655', 'Azul', 2015, 120000, '4567DEF'),
(5, 4, 3, 'Focus', 'FRD4455667788', 'Rojo', 2017, 67000, '5678EFG');

-- 4. Talleres
INSERT IGNORE INTO workshops (id, nif, name, phone, location, email, schedule) VALUES
(1, 'B12345678', 'Taller Mecánica Rápida', '910001122', 'Calle Motor 4', 'info@mecanica.com', '09:00 - 18:00'),
(2, 'B87654321', 'Garaje Oficial Vidal', '930003344', 'Av. Central 20', 'taller@vidal.com', '08:00 - 19:00');

-- 5. Servicios Base
INSERT IGNORE INTO services (id, name) VALUES
(1, 'Cambio de Aceite'),
(2, 'Revisión General'),
(3, 'Cambio de Neumáticos'),
(4, 'Reparación Frenos');

-- 6. Relación Taller-Servicios
INSERT IGNORE INTO workshop_services (id, workshop_id, service_id, price, duration_minutes) VALUES
(1, 1, 1, 50.00, 45),
(2, 1, 4, 120.00, 120),
(3, 2, 2, 80.00, 60),
(4, 2, 3, 200.00, 90);

-- 7. Citas
INSERT IGNORE INTO appointments (id, user_id, workshop_id, vehicle_id, start_date, end_date, status, notes) VALUES
(1, 1, 1, 1, '2025-10-20 09:00:00', '2025-10-20 11:00:00', 'SOLICITADO', 'Ruido al frenar');

-- 8. Reparaciones
INSERT IGNORE INTO repairs (id, appointment_id, vehicle_id, entry_date, status, notes) VALUES
(1, 1, 1, '2025-10-20', 'ACTIVO', 'Pastillas de freno desgastadas');

-- 9. Presupuestos
INSERT IGNORE INTO budgets (id, repair_id, total_gross, total_net) VALUES
(1, 1, 100.00, 121.00);