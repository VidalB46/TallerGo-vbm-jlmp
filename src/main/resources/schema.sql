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
  model VARCHAR(150) NOT NULL,
  color VARCHAR(50),
  year INT,
  km INT,
  matricula VARCHAR(20) UNIQUE,
  CONSTRAINT fk_vehicles_brand FOREIGN KEY (brand_id) REFERENCES brands(id)
    ON DELETE RESTRICT ON UPDATE CASCADE
);
