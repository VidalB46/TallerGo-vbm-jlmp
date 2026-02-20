-- =======================================================
-- 1. TABLAS DE SEGURIDAD Y USUARIOS (Estilo Profesor)
-- =======================================================

-- Crear tabla users (Avanzada)
CREATE TABLE IF NOT EXISTS users (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   email VARCHAR(100) NOT NULL UNIQUE,
   password_hash VARCHAR(500) NOT NULL,
   active BOOLEAN NOT NULL DEFAULT TRUE,
   account_non_locked BOOLEAN NOT NULL DEFAULT TRUE,
   last_password_change DATETIME NULL,
   password_expires_at DATETIME NULL,
   failed_login_attempts INT DEFAULT 0,
   email_verified BOOLEAN NOT NULL DEFAULT FALSE,
   must_change_password BOOLEAN NOT NULL DEFAULT FALSE
);

-- Crear tabla para perfil de usuario (Relación 1:1 con users)
CREATE TABLE IF NOT EXISTS user_profiles (
   user_id BIGINT NOT NULL,
   first_name VARCHAR(100) NOT NULL,
   last_name VARCHAR(100) NOT NULL,
   phone_number VARCHAR(30) NULL,
   profile_image VARCHAR(255) NULL,
   bio VARCHAR(500) NULL,
   locale VARCHAR(10) NULL,
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   CONSTRAINT pk_user_profiles PRIMARY KEY (user_id),
   CONSTRAINT fk_user_profiles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Tabla de roles
CREATE TABLE IF NOT EXISTS roles (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(50) NOT NULL UNIQUE,
   display_name VARCHAR(100) NOT NULL,
   description VARCHAR(255) NULL
);

-- Tabla intermedia N:M entre users y roles
CREATE TABLE IF NOT EXISTS user_roles (
   user_id BIGINT NOT NULL,
   role_id BIGINT NOT NULL,
   CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id),
   CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
   CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Tabla para gestionar tokens de recuperación de contraseña
CREATE TABLE IF NOT EXISTS password_reset_tokens (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 user_id BIGINT NOT NULL,
 token_hash VARCHAR(64) NOT NULL,
 expires_at DATETIME NOT NULL,
 used_at DATETIME NULL,
 created_at DATETIME NOT NULL,
 request_ip VARCHAR(45) NULL,
 user_agent VARCHAR(255) NULL,
 CONSTRAINT fk_prt_user FOREIGN KEY (user_id) REFERENCES users(id),
 INDEX idx_prt_user_id (user_id),
 INDEX idx_prt_token_hash (token_hash),
 INDEX idx_prt_expires_at (expires_at)
);

-- =======================================================
-- 2. TABLAS ESPECÍFICAS DE TALLER GO
-- =======================================================

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

-- Tabla de vehículos (Ligada al nuevo sistema de users)
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
  CONSTRAINT fk_vehicles_brand FOREIGN KEY (brand_id) REFERENCES brands(id) ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_vehicles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Catálogo de Servicios
CREATE TABLE IF NOT EXISTS services (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL
);

-- Relación N:M entre Talleres y Servicios
CREATE TABLE IF NOT EXISTS workshop_services (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  workshop_id INT NOT NULL,
  service_id INT NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  duration_minutes INT,
  CONSTRAINT uk_workshop_service UNIQUE (workshop_id, service_id),
  CONSTRAINT fk_ws_workshop FOREIGN KEY (workshop_id) REFERENCES workshops(id),
  CONSTRAINT fk_ws_service FOREIGN KEY (service_id) REFERENCES services(id)
);

-- Citas
CREATE TABLE IF NOT EXISTS appointments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  workshop_id INT NOT NULL,
  vehicle_id BIGINT NOT NULL,
  start_date DATETIME NOT NULL,
  end_date DATETIME,
  status ENUM('SOLICITADO', 'CONFIRMADO', 'CANCELADO') DEFAULT 'SOLICITADO',
  notes TEXT,
  media_url VARCHAR(255),
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


-- ─────────────────────────────
-- Tabla Taller
-- ─────────────────────────────
CREATE TABLE IF NOT EXISTS talleres (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(100) NOT NULL,
                          direccion VARCHAR(150) NOT NULL,
                          telefono VARCHAR(20)
);

-- ─────────────────────────────
-- Tabla Mecanico
-- ─────────────────────────────
CREATE TABLE IF NOT EXISTS mecanicos (
                           id INT AUTO_INCREMENT PRIMARY KEY,
                           nombre VARCHAR(100) NOT NULL,
                           especialidad VARCHAR(50) NOT NULL,
                           taller_id INT NOT NULL,
                           CONSTRAINT fk_taller FOREIGN KEY (taller_id)
                               REFERENCES talleres(id)
                               ON DELETE CASCADE
);