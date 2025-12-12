-- Marcas
INSERT IGNORE INTO brands (id, name, country) VALUES
(1, 'SEAT', 'España'),
(2, 'RENAULT', 'Francia'),
(3, 'PEUGEOT', 'Francia'),
(4, 'FORD', 'EEUU'),
(5, 'TOYOTA', 'Japón'),
(6, 'VOLKSWAGEN', 'Alemania'),
(7, 'BMW', 'Alemania'),
(8, 'AUDI', 'Alemania');

-- Vehículos
INSERT IGNORE INTO vehicles (id, brand_id, model, color, year, km, matricula) VALUES
(1, 1, 'Leon', 'Blanco', 2018, 45000, '1234ABC'),
(2, 2, 'Clio', 'Gris', 2016, 82000, '2345BCD'),
(3, 5, 'Corolla', 'Negro', 2020, 23000, '3456CDE'),
(4, 6, 'Golf', 'Azul', 2015, 120000, '4567DEF'),
(5, 4, 'Focus', 'Rojo', 2017, 67000, '5678EFG');
