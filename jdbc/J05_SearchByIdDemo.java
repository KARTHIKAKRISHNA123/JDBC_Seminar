import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * J05 — Search one student by ID (and handle "not found").
 *
 * Concepts: PreparedStatement + executeQuery(), using if (rs.next()) for a single row.
 */
public class J05_SearchByIdDemo {

    static void findById(Connection con, int id) throws SQLException {
        String sql = "SELECT id, name, email, department FROM students WHERE id = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {                   // at most one row because id is the PRIMARY KEY
                    System.out.println("Found -> " + rs.getInt("id") + " | "
                            + rs.getString("name") + " | "
                            + rs.getString("email") + " | "
                            + rs.getString("department"));
                } else {
                    System.out.println("No student with id " + id);
                }
            }
        }
    }

    public static void main(String[] args) {
        try (Connection con = DB.getConnection()) {
            findById(con, 3);
            findById(con, 99);
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
Found -> 3 | Karthik R | karthik@example.com | CSE
No student with id 99
*/
