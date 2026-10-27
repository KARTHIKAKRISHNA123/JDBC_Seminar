import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * J03 — CREATE: insert one student with a PreparedStatement.
 *
 * Concepts: ? placeholders, setString/setInt, parameter indexes start at 1, executeUpdate().
 * Run it twice to see the UNIQUE constraint reject the duplicate email.
 */
public class J03_InsertDemo {
    public static void main(String[] args) {

        String sql = "INSERT INTO students (name, email, phone, age, department) "
                   + "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "Rahul V");            // 1st ?  -> name
            ps.setString(2, "rahul@example.com");  // 2nd ?  -> email
            ps.setString(3, "9871234560");         // 3rd ?  -> phone (text, not a number!)
            ps.setInt(4, 23);                      // 4th ?  -> age
            ps.setString(5, "EEE");                // 5th ?  -> department

            int rows = ps.executeUpdate();         // INSERT/UPDATE/DELETE -> number of rows changed
            System.out.println("Rows inserted: " + rows);

        } catch (SQLException e) {
            System.out.println("Insert failed: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
Rows inserted: 1
*/
