create database jdbc_practice;
use jdbc_practice;

CREATE TABLE Account (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         username VARCHAR(50) NOT NULL UNIQUE,
                         password VARCHAR(255) NOT NULL,
                         status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
                         role ENUM('ADMIN', 'HR') NOT NULL
);

-- Bảng phòng ban
CREATE TABLE Department (
                            department_id INT AUTO_INCREMENT PRIMARY KEY,
                            department_name VARCHAR(100) NOT NULL UNIQUE CHECK (LENGTH(department_name) >= 10 AND LENGTH(department_name) <= 100),
                            description VARCHAR(255),
                            status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE'
);

-- Bảng nhân viên
CREATE TABLE Employee (
                          employee_id VARCHAR(5) PRIMARY KEY CHECK (employee_id LIKE 'E%'),
                          employee_name VARCHAR(150) NOT NULL CHECK (LENGTH(employee_name) >= 15 AND LENGTH(employee_name) <= 150),
                          email VARCHAR(100) NOT NULL UNIQUE,
                          phone VARCHAR(15) NOT NULL,
                          gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL,
                          salary_level INT NOT NULL CHECK (salary_level > 0),
                          salary DECIMAL(12, 2) NOT NULL CHECK (salary > 0),
                          birth_date DATE NOT NULL,
                          address VARCHAR(255) NOT NULL,
                          status ENUM('ACTIVE', 'INACTIVE', 'ONLEAVE', 'POLICYLEAVE') NOT NULL DEFAULT 'ACTIVE',
                          department_id INT NOT NULL,
                          FOREIGN KEY (department_id) REFERENCES Department(department_id)
);