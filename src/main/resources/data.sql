-- =======================================================
-- 0. DATOS INICIALES (INSERT IGNORE: no borra usuarios registrados)
-- =======================================================

-- =======================================================
-- 1. DATOS DE SEGURIDAD
-- =======================================================
INSERT IGNORE INTO roles (id, name, display_name, description) VALUES
(1, 'ROLE_ADMIN', 'Administrador', 'Acceso total a la gestión del taller'),
(2, 'ROLE_CLIENT', 'Cliente', 'Usuario cliente de Taller Go'),
(3, 'ROLE_WORKSHOP_ADMIN', 'Admin de Taller', 'Gestiona las citas y operaciones de un taller concreto');

INSERT IGNORE INTO users (
    id, email, password_hash, active, account_non_locked,
    last_password_change, password_expires_at, failed_login_attempts,
    email_verified, must_change_password, workshop_id
) VALUES
(1, 'leo@email.com', '$2a$12$HnF3pSI.kpCNujmMgcQDA.fbGt2TFPbmMDH.rT4wUKLvOKEzsvlTC', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE, FALSE, NULL),
(2, 'vidal@email.com', '$2a$12$HnF3pSI.kpCNujmMgcQDA.fbGt2TFPbmMDH.rT4wUKLvOKEzsvlTC', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE, FALSE, NULL),
(3, 'maria@email.com', '$2a$12$HnF3pSI.kpCNujmMgcQDA.fbGt2TFPbmMDH.rT4wUKLvOKEzsvlTC', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE, FALSE, NULL),
(4, 'jmorpat', '$2a$12$HnF3pSI.kpCNujmMgcQDA.fbGt2TFPbmMDH.rT4wUKLvOKEzsvlTC', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE, FALSE, NULL),
(5, 'VidalB46', '$2a$12$HnF3pSI.kpCNujmMgcQDA.fbGt2TFPbmMDH.rT4wUKLvOKEzsvlTC', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE, FALSE, NULL),
(6, 'taller@email.com', '$2a$12$HnF3pSI.kpCNujmMgcQDA.fbGt2TFPbmMDH.rT4wUKLvOKEzsvlTC', TRUE, TRUE, NOW(), DATE_ADD(NOW(), INTERVAL 3 MONTH), 0, TRUE, FALSE, 1);

INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
(1, 1),
(2, 1),
(3, 2),
(6, 3);

INSERT IGNORE INTO user_profiles (user_id, first_name, last_name, phone_number, locale) VALUES
(1, 'Leo', 'Morillo', '600111222', 'es_ES'),
(2, 'Vidal', 'Bañez', '600333444', 'es_ES'),
(3, 'Maria', 'Lopez', '600555666', 'es_ES'),
(6, 'Admin', 'Taller', '600777888', 'es_ES');
-- =======================================================
-- 2. DATOS DE TALLER GO (Entidades Maestras)
-- =======================================================
INSERT IGNORE INTO brands (id, name, country) VALUES
(1,  'SEAT',       'España'),        (2,  'RENAULT',    'Francia'),
(3,  'PEUGEOT',    'Francia'),       (4,  'FORD',       'EEUU'),
(5,  'TOYOTA',     'Japón'),         (6,  'VOLKSWAGEN', 'Alemania'),
(7,  'BMW',        'Alemania'),      (8,  'AUDI',       'Alemania'),
(9,  'MERCEDES',   'Alemania'),      (10, 'HONDA',      'Japón'),
(11, 'NISSAN',     'Japón'),         (12, 'HYUNDAI',    'Corea del Sur'),
(13, 'KIA',        'Corea del Sur'), (14, 'OPEL',       'Alemania'),
(15, 'CITROEN',    'Francia'),       (16, 'FIAT',       'Italia'),
(17, 'ALFA ROMEO', 'Italia'),        (18, 'VOLVO',      'Suecia'),
(19, 'MAZDA',      'Japón'),         (20, 'SKODA',      'República Checa');

INSERT IGNORE INTO vehicles (id, brand_id, user_id, model, vin, color, year, km, matricula) VALUES
(1,  1, 1, 'Leon',       'VBN1234567890', 'Blanco',  2018,  45000, '1234ABC'),
(2,  2, 1, 'Clio',       'VBN0987654321', 'Gris',    2016,  82000, '2345BCD'),
(3,  5, 2, 'Corolla',    'TYT1122334455', 'Negro',   2020,  23000, '3456CDE'),
(4,  6, 3, 'Golf',       'VKS9988776655', 'Azul',    2015, 120000, '4567DEF'),
(5,  4, 3, 'Focus',      'FRD4455667788', 'Rojo',    2017,  67000, '5678EFG'),
(6,  7, 1, 'Serie 3',    'BMW3456789012', 'Negro',   2019,  38000, '6789FGH'),
(7,  8, 2, 'A4',         'AUD5678901234', 'Gris',    2021,  15000, '7890GHI'),
(8,  3, 2, '308',        'PGT6789012345', 'Blanco',  2017,  74000, '8901HIJ'),
(9,  1, 3, 'Ibiza',      'SET7890123456', 'Rojo',    2016,  99000, '9012IJK'),
(10, 5, 1, 'Yaris',      'TYT8901234567', 'Azul',    2022,   8000, '0123JKL'),
(11, 2, 3, 'Megane',     'REN9012345678', 'Gris',    2018,  53000, '1234KLM'),
(12, 6, 2, 'Passat',     'VKS0123456789', 'Plata',   2014, 145000, '2345LMN'),
(13, 4, 1, 'Mondeo',     'FRD1234509876', 'Verde',   2015,  88000, '3456MNO'),
(14, 7, 3, 'Serie 1',    'BMW2345610987', 'Blanco',  2020,  27000, '4567NOP'),
(15, 8, 2, 'A3',         'AUD3456721098', 'Negro',   2019,  41000, '5678OPQ'),
(16, 3, 1, '2008',       'PGT4567832109', 'Naranja', 2023,   4000, '6789PQR'),
(17, 1, 3, 'Arona',      'SET5678943210', 'Azul',    2021,  19000, '7890QRS'),
(18, 6, 2, 'Tiguan',     'VKS6789054321', 'Gris',    2016, 112000, '8901RST'),
(19, 5, 1, 'RAV4',       'TYT7890165432', 'Blanco',  2018,  62000, '9012STU'),
(20, 2, 3, 'Arkana',     'REN8901276543', 'Rojo',    2022,  11000, '0123TUV');

INSERT IGNORE INTO workshops (id, nif, name, phone, location, email, schedule) VALUES
(1,  'B12345678', 'Taller Mecánica Rápida',      '910001122', 'Calle Motor 4, Madrid',            'info@mecanica.com',       '09:00 - 18:00'),
(2,  'B87654321', 'Garaje Oficial Vidal',         '930003344', 'Av. Central 20, Barcelona',        'taller@vidal.com',        '08:00 - 19:00'),
(3,  'B11223344', 'Taller Hermanos Castillo',     '944112233', 'Calle Autonomía 18, Bilbao',        'info@tallercastillo.com', '08:30 - 17:30'),
(4,  'B22334455', 'Taller El Rápido',             '954667788', 'Calle Feria 12, Sevilla',          'elrapido@taller.com',     '09:00 - 20:00'),
(5,  'B33445566', 'Mecánica Precisión Valencia',  '963778899', 'Av. del Cid 30, Valencia',         'precision@mecanica.com',  '08:00 - 18:00'),
(6,  'B44556677', 'Garaje Central Zaragoza',      '976889900', 'Paseo Sagasta 55, Zaragoza',       'central@garaje.com',      '09:00 - 19:00'),
(7,  'B55667788', 'Taller Martínez e Hijos',      '952990011', 'Calle Larios 8, Málaga',           'martinez@taller.com',     '08:00 - 17:00'),
(8,  'B66778899', 'AutoTaller Murcia',            '968101122', 'Av. Juan Carlos I 14, Murcia',     'info@autotallermurcia.com','09:00 - 18:30'),
(9,  'B77889900', 'Taller 24h Alicante',          '965212233', 'Rambla Méndez Núñez 3, Alicante',  '24h@talleralicante.com',  '00:00 - 24:00'),
(10, 'B88990011', 'Mecánica Express Valladolid',  '983323344', 'Calle Santiago 22, Valladolid',    'express@mecanica.com',    '08:30 - 18:30');

INSERT IGNORE INTO mechanics (id, name, specialty, workshop_id) VALUES
(1,  'Carlos López',     'GENERAL_MECHANICS', 1), (2,  'Ana Martín',      'ELECTRICAL',        1),
(3,  'Luis García',      'TIRES_ALIGNMENT',   2), (4,  'Marta Pérez',     'BODYWORK_PAINT',    2),
(5,  'Javier Torres',    'GENERAL_MECHANICS', 1),
(6,  'Pedro Fernández',  'GENERAL_MECHANICS', 3), (7,  'Sofía Ruiz',      'ELECTRICAL',        3),
(8,  'Miguel Sánchez',   'BODYWORK_PAINT',    4), (9,  'Laura Gómez',     'GENERAL_MECHANICS', 4),
(10, 'Tomás Navarro',    'TIRES_ALIGNMENT',   5), (11, 'Elena Castillo',  'GENERAL_MECHANICS', 5),
(12, 'Rubén Moreno',     'ELECTRICAL',        6), (13, 'Carmen Jiménez',  'GENERAL_MECHANICS', 6),
(14, 'David Romero',     'BODYWORK_PAINT',    7), (15, 'Isabel Molina',   'GENERAL_MECHANICS', 7),
(16, 'Sergio Álvarez',   'TIRES_ALIGNMENT',   8), (17, 'Patricia Vega',   'ELECTRICAL',        8),
(18, 'Alberto Herrera',  'GENERAL_MECHANICS', 9), (19, 'Natalia Reyes',   'GENERAL_MECHANICS', 9),
(20, 'Francisco Mora',   'BODYWORK_PAINT',   10), (21, 'Beatriz Santos',  'GENERAL_MECHANICS',10);

INSERT IGNORE INTO services (id, name) VALUES 
(1, 'Cambio de Aceite'), (2, 'Revisión General'), (3, 'Cambio de Neumáticos'), (4, 'Reparación Frenos');

INSERT IGNORE INTO workshop_services (id, workshop_id, service_id, price, duration_minutes) VALUES
(1,  1, 1,  50.00, 45),  (2,  1, 4, 120.00, 120), (3,  2, 2,  80.00,  60), (4,  2, 3, 200.00, 90),
(5,  3, 1,  45.00, 40),  (6,  3, 2,  75.00,  60), (7,  4, 3, 190.00,  85), (8,  4, 4, 110.00, 100),
(9,  5, 1,  55.00, 50),  (10, 5, 4, 130.00, 110), (11, 6, 2,  85.00,  65), (12, 6, 3, 210.00,  90),
(13, 7, 1,  48.00, 45),  (14, 7, 2,  78.00,  60), (15, 8, 3, 195.00,  80), (16, 8, 4, 115.00, 105),
(17, 9, 1,  60.00, 45),  (18, 9, 2,  90.00,  70), (19,10, 3, 205.00,  90), (20,10, 4, 125.00, 115);

-- =======================================================
-- 3. FLUJO DE PRUEBA (Cita -> Reparación -> Presupuesto)
-- =======================================================
INSERT IGNORE INTO appointments (id, user_id, workshop_id, vehicle_id, start_date, end_date, status, notes) VALUES
(1, 1, 1, 1, '2025-10-20 09:00:00', '2025-10-20 11:00:00', 'EN_TALLER', 'Ruido al frenar');

INSERT IGNORE INTO repairs (id, appointment_id, vehicle_id, entry_date, status, notes) VALUES
(1, 1, 1, '2025-10-20', 'STANDBY', 'Pastillas de freno desgastadas');
