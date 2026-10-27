import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * J07 — DELETE: remove the student inserted by J03.
 *
 * Concepts: DELETE removes matching ROWS, not the table. Always use a WHERE clause.
 * We delete by email here so the demo works no matter which id Rahul received.
 */
public class J07_DeleteDemo {
    public static void main(String[] args) {

        String sql = "DELETE FROM students WHERE email = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "rahul@example.com");
            int rows = ps.executeUpdate();
            System.out.println("Rows deleted: " + rows);   // run again -> 0 (already gone)

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
Rows deleted: 1
*/
