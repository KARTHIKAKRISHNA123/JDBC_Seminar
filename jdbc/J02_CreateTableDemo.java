import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * J02 — Create (or verify) the students table from Java.
 *
 * Concepts: Statement, execute() for DDL, CREATE TABLE IF NOT EXISTS.
 * Safe to run any number of times: IF NOT EXISTS skips creation when the table is already there.
 */
public class J02_CreateTableDemo {
    public static void main(String[] args) {

        String sql = """
            CREATE TABLE IF NOT EXISTS students (
                id          INT          PRIMARY KEY AUTO_INCREMENT,
                name        VARCHAR(100) NOT NULL,
                email       VARCHAR(150) NOT NULL UNIQUE,
                phone       CHAR(10)     UNIQUE,
                age         INT,
                department  VARCHAR(10)  NOT NULL DEFAULT 'CSE',
                created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                CONSTRAINT chk_age   CHECK (age IS NULL OR age BETWEEN 16 AND 60),
                CONSTRAINT chk_phone CHECK (phone IS NULL OR phone REGEXP '^[0-9]{10}$')
            )
            """;   // a Java 15+ text block: multi-line string, no + needed

        try (Connection con = DB.getConnection();
             Statement st = con.createStatement()) {

            // execute() returns true only when the SQL produces a ResultSet (a SELECT).
            // DDL like CREATE TABLE produces no rows, so it returns false.
            boolean producedRows = st.execute(sql);

            System.out.println("execute() returned: " + producedRows);
            System.out.println("Table 'students' is ready.");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
execute() returned: false
Table 'students' is ready.
*/
