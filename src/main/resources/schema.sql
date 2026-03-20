-- =======================================================
-- 0. LIMPIEZA DE ESTRUCTURA (Para que se apliquen los cambios)
-- =======================================================
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS budgets;
DROP TABLE IF EXISTS repairs;
DROP TABLE IF EXISTS appointments;
DROP TABLE IF EXISTS workshop_services;
DROP TABLE IF EXISTS services;
DROP TABLE IF EXISTS mechanics;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS brands;
DROP TABLE IF EXISTS password_reset_tokens;
DROP TABLE IF EXISTS user_profiles;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS workshops;
SET FOREIGN_KEY_CHECKS = 1;

-- =======================================================
-- 1. TABLAS DE SEGURIDAD Y USUARIOS
-- =======================================================
CREATE TABLE users (
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

CREATE TABLE user_profiles (
   user_id BIGINT PRIMARY KEY,
   first_name VARCHAR(100) NOT NULL,
   last_name VARCHAR(100) NOT NULL,
   phone_number VARCHAR(30) NULL,
   profile_image VARCHAR(255) NULL,
   bio VARCHAR(500) NULL,
   locale VARCHAR(10) NULL,
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   CONSTRAINT fk_user_profiles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE roles (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(50) NOT NULL UNIQUE,
   display_name VARCHAR(100) NOT NULL,
   description VARCHAR(255) NULL
);

CREATE TABLE user_roles (
   user_id BIGINT NOT NULL,
   role_id BIGINT NOT NULL,
   PRIMARY KEY (user_id, role_id),
   CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
   CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- =======================================================
-- 2. TABLAS ESPECÍFICAS DE TALLER GO
-- =======================================================
CREATE TABLE workshops (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nif VARCHAR(20) UNIQUE NOT NULL,
  name VARCHAR(150) NOT NULL,
  phone VARCHAR(20),
  location VARCHAR(255),
  email VARCHAR(100),
  schedule VARCHAR(100)
);

CREATE TABLE brands (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL UNIQUE,
  country VARCHAR(100)
);

CREATE TABLE vehicles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  brand_id INT NOT NULL,
  user_id BIGINT NOT NULL,
  model VARCHAR(150) NOT NULL,
  vin VARCHAR(50) UNIQUE,
  color VARCHAR(50),
  year INT,
  km INT,
  matricula VARCHAR(20) UNIQUE,
  CONSTRAINT fk_vehicles_brand FOREIGN KEY (brand_id) REFERENCES brands(id),
  CONSTRAINT fk_vehicles_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE services (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(150) NOT NULL
);

CREATE TABLE workshop_services (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  workshop_id INT NOT NULL,
  service_id INT NOT NULL,
  price DECIMAL(10, 2) NOT NULL,
  duration_minutes INT,
  CONSTRAINT uk_workshop_service UNIQUE (workshop_id, service_id),
  CONSTRAINT fk_ws_workshop FOREIGN KEY (workshop_id) REFERENCES workshops(id),
  CONSTRAINT fk_ws_service FOREIGN KEY (service_id) REFERENCES services(id)
);

CREATE TABLE appointments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  workshop_id INT NOT NULL,
  vehicle_id BIGINT NOT NULL,
  start_date DATETIME NOT NULL,
  end_date DATETIME,
  status VARCHAR(20) DEFAULT 'SOLICITADO',
  notes TEXT,
  CONSTRAINT fk_appt_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_appt_workshop FOREIGN KEY (workshop_id) REFERENCES workshops(id),
  CONSTRAINT fk_appt_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

CREATE TABLE repairs (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  appointment_id BIGINT UNIQUE NOT NULL,
  vehicle_id BIGINT NOT NULL,
  entry_date DATE,
  notes TEXT,
  status VARCHAR(20) DEFAULT 'STANDBY',
  CONSTRAINT fk_repair_appt FOREIGN KEY (appointment_id) REFERENCES appointments(id),
  CONSTRAINT fk_repair_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

CREATE TABLE budgets (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  repair_id BIGINT UNIQUE NOT NULL,
  total_gross DECIMAL(10, 2),
  total_net DECIMAL(10, 2),
  accepted BOOLEAN DEFAULT FALSE,
  CONSTRAINT fk_budget_repair FOREIGN KEY (repair_id) REFERENCES repairs(id)
);

CREATE TABLE reviews (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  workshop_id INT NOT NULL,
  user_id BIGINT NOT NULL,
  rating INT CHECK (rating >= 1 AND rating <= 5),
  comment TEXT,
  CONSTRAINT fk_review_workshop FOREIGN KEY (workshop_id) REFERENCES workshops(id),
  CONSTRAINT fk_review_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE mechanics (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   specialty VARCHAR(50) NOT NULL,
   workshop_id INT NOT NULL,
   CONSTRAINT fk_mechanic_workshop FOREIGN KEY (workshop_id) REFERENCES workshops(id) ON DELETE CASCADE
);