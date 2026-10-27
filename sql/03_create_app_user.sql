-- =====================================================================
--  03_create_app_user.sql  —  OPTIONAL: least-privilege login for the app
--
--  Using 'root' in an application is like giving every visitor the master
--  key to the building. This creates a user that can ONLY read/write data
--  inside college_db — it cannot drop databases or create users.
--
--  Run as root. Change the password before running.
--  Then set:  DB_USER=seminar_app   DB_PASSWORD=<the password below>
-- =====================================================================

CREATE USER IF NOT EXISTS 'seminar_app'@'localhost' IDENTIFIED BY 'Change_Me_123!';

GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE
ON college_db.* TO 'seminar_app'@'localhost';

-- J02_CreateTableDemo needs CREATE; grant it only if you will run that demo
-- with this user:
-- GRANT CREATE ON college_db.* TO 'seminar_app'@'localhost';

SHOW GRANTS FOR 'seminar_app'@'localhost';
