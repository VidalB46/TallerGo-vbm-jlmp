-- Tabla de Usuarios (Clientes)
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(255) NOT NULL,
  phone VARCHAR(20),
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  role ENUM('CLIENT', 'ADMIN') DEFAULT 'CLIENT'
);

-- Tabla de Talleres
CREATE TABLE IF NOT EXISTS workshops (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nif VARCHAR(20) UNIQUE NOT NULL,
  name VARCHAR(150) NOT NULL,
  phone VARCHAR(20),
  location VARCHAR(255),
  email VARCHAR(100),
  schedule VARCHAR(100)
);

-- Tabla de marcas
CREATE TABLE IF NOT EXISTS brands (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  country VARCHAR(100)
);

-- Tabla de vehículos
CREATE TABLE IF NOT EXISTS vehicles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  brand_id INT NOT NULL,
  user_id BIGINT NOT NULL,
  model VARCHAR(150) NOT NULL,
  vin VARCHAR(50) UNIQUE,
  color VARCHAR(50),
  year INT,
  km INT,
  matricula VARCHAR(20) UNIQUE,
  CONSTRAINT fk_vehicles_brand FOREIGN KEY (brand_id) REFERENCES brands(id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_vehicles_user FOREIGN KEY (user_id) REFERENCES users(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Catálogo de Servicios (e.g., Cambio de Aceite)
CREATE TABLE IF NOT EXISTS services (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL
);

-- Relación N:M entre Talleres y Servicios (con precio y duración)
CREATE TABLE IF NOT EXISTS workshop_services (
  workshop_id INT,
  service_id INT,
  price DECIMAL(10, 2) NOT NULL,
  duration_minutes INT,
  PRIMARY KEY (workshop_id, service_id),
  CONSTRAINT fk_ws_workshop FOREIGN KEY (workshop_id) REFERENCES workshops(id),
  CONSTRAINT fk_ws_service FOREIGN KEY (service_id) REFERENCES services(id)
);

-- Citas
-- Basado en [cite: 41, 42, 43, 44, 45, 46, 47, 48, 49, 50]
CREATE TABLE IF NOT EXISTS appointments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,   -- ID_Cliente
  workshop_id INT NOT NULL,  -- ID_Taller
  vehicle_id BIGINT NOT NULL,-- ID_Vehiculo
  start_date DATETIME NOT NULL,
  end_date DATETIME,
  status ENUM('SOLICITADO', 'CONFIRMADO', 'CANCELADO') DEFAULT 'SOLICITADO',
  notes TEXT,
  media_url VARCHAR(255),    -- Para fotos/videos
  CONSTRAINT fk_appt_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_appt_workshop FOREIGN KEY (workshop_id) REFERENCES workshops(id),
  CONSTRAINT fk_appt_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

-- Reparaciones
CREATE TABLE IF NOT EXISTS repairs (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  appointment_id BIGINT UNIQUE NOT NULL,
  vehicle_id BIGINT NOT NULL,
  entry_date DATE,
  notes TEXT,
  status ENUM('STANDBY', 'ACTIVO', 'FINALIZADO') DEFAULT 'STANDBY',
  CONSTRAINT fk_repair_appt FOREIGN KEY (appointment_id) REFERENCES appointments(id),
  CONSTRAINT fk_repair_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

-- Presupuestos
CREATE TABLE IF NOT EXISTS budgets (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  repair_id BIGINT NOT NULL,
  total_gross DECIMAL(10, 2),
  total_net DECIMAL(10, 2),
  CONSTRAINT fk_budget_repair FOREIGN KEY (repair_id) REFERENCES repairs(id)
);

-- Reseñas
CREATE TABLE IF NOT EXISTS reviews (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  workshop_id INT NOT NULL,
  user_id BIGINT NOT NULL,
  rating INT CHECK (rating >= 1 AND rating <= 5),
  comment TEXT,
  CONSTRAINT fk_review_workshop FOREIGN KEY (workshop_id) REFERENCES workshops(id),
  CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id)
);