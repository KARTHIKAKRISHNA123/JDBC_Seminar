import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * J17 — Reading a SQLException properly.
 *
 * Every SQLException carries three useful things:
 *   getMessage()   -> human-readable text
 *   getSQLState()  -> 5-character standard code (23000 = integrity constraint violation)
 *   getErrorCode() -> MySQL's own number (1062 = duplicate entry)
 *
 * Constraint violations arrive as SQLIntegrityConstraintViolationException,
 * so you can catch them separately and show a friendly message.
 */
public class J17_ExceptionDemo {
    public static void main(String[] args) {

        String sql = "INSERT INTO students (name, email) VALUES (?, ?)";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "Copycat");
            ps.setString(2, "arun@example.com");   // already used by Arun -> UNIQUE violation
            ps.executeUpdate();

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Friendly message : That email is already registered.");
            System.out.println("getMessage()     : " + e.getMessage());
            System.out.println("getSQLState()    : " + e.getSQLState());
            System.out.println("getErrorCode()   : " + e.getErrorCode());

        } catch (SQLException e) {
            System.out.println("Other database error: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
Friendly message : That email is already registered.
getMessage()     : Duplicate entry 'arun@example.com' for key 'students.email'
getSQLState()    : 23000
getErrorCode()   : 1062
*/
