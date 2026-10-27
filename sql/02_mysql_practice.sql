-- =====================================================================
--  02_mysql_practice.sql  —  Live demo queries for the MySQL module
--  Run 01_setup.sql first. Run these ONE AT A TIME in Workbench
--  (Ctrl+Enter runs the statement under the cursor).
-- =====================================================================
USE college_db;

-- ---------- DQL: reading ----------
SELECT * FROM students;                                   -- every column, every row
SELECT name, email FROM students;                         -- only some columns

-- ---------- Filtering: WHERE, AND, OR, LIKE, IS NULL ----------
SELECT name, age FROM students WHERE age > 20;
SELECT name, department FROM students WHERE department = 'CSE' AND age >= 20;
SELECT name, department FROM students WHERE department = 'IT' OR department = 'ECE';
SELECT name FROM students WHERE name LIKE 'K%';           -- starts with K
SELECT name, email FROM students WHERE email LIKE '%@example.com';
SELECT name FROM students WHERE age IS NULL;              -- NOT  "age = NULL"  (that never matches)

-- ---------- Sorting and limiting ----------
SELECT name, age FROM students ORDER BY age DESC;
SELECT name, age FROM students ORDER BY age DESC LIMIT 2; -- top 2 oldest

-- ---------- Aggregate functions ----------
SELECT COUNT(*)  AS total_students FROM students;
SELECT COUNT(age) AS students_with_age FROM students;     -- COUNT(column) skips NULLs
SELECT AVG(age), MIN(age), MAX(age) FROM students;

-- ---------- GROUP BY and HAVING ----------
SELECT department, COUNT(*) AS cnt
FROM students
GROUP BY department;

SELECT department, COUNT(*) AS cnt
FROM students
GROUP BY department
HAVING COUNT(*) > 1;          -- HAVING filters groups; WHERE filters rows

-- ---------- JOINs ----------
-- INNER JOIN: only students who have at least one enrollment
SELECT s.name, c.course_name, e.marks
FROM students s
INNER JOIN enrollments e ON s.id = e.student_id
INNER JOIN courses c     ON c.course_id = e.course_id
ORDER BY s.name;

-- LEFT JOIN: every student, even with no enrollment (Meena shows NULL)
SELECT s.name, e.course_id
FROM students s
LEFT JOIN enrollments e ON s.id = e.student_id
ORDER BY s.id;

-- ---------- Constraints in action (each of these FAILS on purpose) ----------
-- INSERT INTO students (name, email) VALUES ('Dup', 'arun@example.com');     -- Duplicate entry (UNIQUE)
-- INSERT INTO students (name, email, age) VALUES ('Old', 'old@x.com', 99);   -- Check constraint 'chk_age' is violated
-- INSERT INTO students (name, email, phone) VALUES ('Ph', 'ph@x.com', '12AB567890'); -- 'chk_phone' violated
-- INSERT INTO enrollments (student_id, course_id) VALUES (999, 1);           -- foreign key constraint fails

-- ---------- DDL: changing structure ----------
ALTER TABLE students ADD COLUMN city VARCHAR(50);
DESCRIBE students;
ALTER TABLE students DROP COLUMN city;

-- ---------- Indexes ----------
-- Speeds up searches on 'department' (like the index at the back of a book)
CREATE INDEX idx_students_department ON students(department);
SHOW INDEX FROM students;
EXPLAIN SELECT * FROM students WHERE department = 'CSE';

-- ---------- DML: changing data (always use WHERE!) ----------
UPDATE students SET age = 21 WHERE id = 1;
-- DELETE FROM students WHERE id = 5;    -- uncomment to try; re-run 01_setup.sql to restore

-- ---------- Transaction control ----------
START TRANSACTION;
UPDATE accounts SET balance = balance - 500 WHERE account_id = 101;
SELECT * FROM accounts;      -- shows 4500 inside this transaction
ROLLBACK;
SELECT * FROM accounts;      -- back to 5000 — nothing was saved
