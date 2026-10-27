import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * J10 — Get the AUTO_INCREMENT id MySQL assigned to a new row.
 *
 * Real use: after registering a student, show "Your registration number is 7".
 * Concepts: Statement.RETURN_GENERATED_KEYS, getGeneratedKeys().
 */
public class J10_GeneratedKeysDemo {
    public static void main(String[] args) {

        String sql = "INSERT INTO students (name, email, age) VALUES (?, ?, ?)";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, "Sneha P");
            ps.setString(2, "sneha@example.com");
            ps.setInt(3, 20);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    System.out.println("New student saved with id = " + keys.getInt(1));
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
New student saved with id = 7
*/
