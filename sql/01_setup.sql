-- =====================================================================
--  01_setup.sql  —  Creates (or RESETS) the college_db database
--  Run this ONCE before the demos, and again any time you want a clean
--  database (for example, right before the seminar starts).
--
--  How to run:
--    MySQL Workbench : File > Open SQL Script > Execute (lightning icon)
--    Command line    : mysql -u root -p < 01_setup.sql
--
--  Needs MySQL 8.0.16 or newer (CHECK constraints are enforced from 8.0.16).
-- =====================================================================

DROP DATABASE IF EXISTS college_db;
CREATE DATABASE college_db;
USE college_db;

-- ---------------------------------------------------------------------
-- 1. students  — the ONE table used throughout JDBC, Regex and the project
-- ---------------------------------------------------------------------
CREATE TABLE students (
    id          INT          PRIMARY KEY AUTO_INCREMENT,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    phone       CHAR(10)     UNIQUE,                 -- NULL allowed (optional)
    age         INT,                                 -- NULL allowed (unknown)
    department  VARCHAR(10)  NOT NULL DEFAULT 'CSE',
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,

    -- The database double-checks what Java already validated.
    CONSTRAINT chk_age   CHECK (age IS NULL OR age BETWEEN 16 AND 60),
    CONSTRAINT chk_phone CHECK (phone IS NULL OR phone REGEXP '^[0-9]{10}$')
);

INSERT INTO students (name, email, phone, age, department) VALUES
('Arun Kumar',  'arun@example.com',    '9876543210', 20, 'CSE'),
('Priya S',     'priya@example.com',   '9123456780', 21, 'ECE'),
('Karthik R',   'karthik@example.com', '9988776655', 22, 'CSE'),
('Divya M',     'divya@example.com',   '9000011111', 19, 'IT'),
('Meena L',     'meena@example.com',   NULL,         NULL, 'MECH');

-- ---------------------------------------------------------------------
-- 2. courses + enrollments  — used only for FOREIGN KEY and JOIN examples
-- ---------------------------------------------------------------------
CREATE TABLE courses (
    course_id    INT          PRIMARY KEY AUTO_INCREMENT,
    course_name  VARCHAR(100) NOT NULL,
    credits      INT          NOT NULL DEFAULT 3
);

INSERT INTO courses (course_name, credits) VALUES
('Java Programming', 4),
('Database Systems', 4),
('Web Technology',   3);

CREATE TABLE enrollments (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id    INT NOT NULL,
    course_id     INT NOT NULL,
    marks         INT,
    FOREIGN KEY (student_id) REFERENCES students(id)  ON DELETE CASCADE,
    FOREIGN KEY (course_id)  REFERENCES courses(course_id)
);

INSERT INTO enrollments (student_id, course_id, marks) VALUES
(1, 1, 88), (1, 2, 92),
(2, 1, 75),
(3, 2, 81), (3, 3, 67),
(4, 1, 95);
-- Meena (id 5) has no enrollments on purpose — shows the INNER vs LEFT JOIN difference.

-- ---------------------------------------------------------------------
-- 3. accounts  — used only for the TRANSACTION (bank transfer) demo
-- ---------------------------------------------------------------------
CREATE TABLE accounts (
    account_id  INT           PRIMARY KEY,
    holder      VARCHAR(100)  NOT NULL,
    balance     DECIMAL(10,2) NOT NULL,
    CONSTRAINT chk_balance CHECK (balance >= 0)
);

INSERT INTO accounts VALUES
(101, 'Arun',  5000.00),
(102, 'Priya', 3000.00);

-- ---------------------------------------------------------------------
-- 4. Stored procedure  — used by the CallableStatement demo
--    IN  min_age : input value
--    OUT total   : number of students at or above that age
-- ---------------------------------------------------------------------
DELIMITER //
CREATE PROCEDURE count_students_above_age(IN min_age INT, OUT total INT)
BEGIN
    SELECT COUNT(*) INTO total FROM students WHERE age >= min_age;
END //
DELIMITER ;

-- ---------------------------------------------------------------------
-- Check
-- ---------------------------------------------------------------------
SELECT * FROM students;
