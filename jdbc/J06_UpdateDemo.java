import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * J06 — UPDATE: change one student's age.
 *
 * Concepts: executeUpdate() returns rows affected; 0 means the WHERE matched nothing.
 * WARNING to teach: "UPDATE students SET age = 22" with NO WHERE changes EVERY row.
 */
public class J06_UpdateDemo {
    public static void main(String[] args) {

        String sql = "UPDATE students SET age = ? WHERE id = ?";

        try (Connection con = DB.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, 21);   // new age
            ps.setInt(2, 1);    // for student id 1
            System.out.println("Rows updated (id 1):  " + ps.executeUpdate());

            ps.setInt(1, 30);   // the same PreparedStatement can be reused with new values
            ps.setInt(2, 99);   // id 99 does not exist
            System.out.println("Rows updated (id 99): " + ps.executeUpdate());

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
Rows updated (id 1):  1
Rows updated (id 99): 0
*/
