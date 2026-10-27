import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * J13 — Savepoints: undo PART of a transaction.
 *
 * Analogy: a "checkpoint" in a video game. You fail after the checkpoint,
 * you restart from the checkpoint, not from the beginning.
 */
public class J13_SavepointDemo {
    public static void main(String[] args) {

        String insert = "INSERT INTO students (name, email) VALUES (?, ?)";

        try (Connection con = DB.getConnection()) {
            con.setAutoCommit(false);

            try (PreparedStatement ps = con.prepareStatement(insert)) {
                ps.setString(1, "Anitha J");
                ps.setString(2, "anitha@example.com");
                ps.executeUpdate();
                System.out.println("Inserted Anitha");

                Savepoint afterAnitha = con.setSavepoint("after_anitha");

                ps.setString(1, "Bala G");
                ps.setString(2, "bala@example.com");
                ps.executeUpdate();
                System.out.println("Inserted Bala");

                con.rollback(afterAnitha);              // undo only what came after the savepoint
                System.out.println("Rolled back to savepoint (Bala undone)");

                con.commit();                           // Anitha is saved
                System.out.println("Committed");
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }

            con.setAutoCommit(true);
            try (PreparedStatement check = con.prepareStatement(
                     "SELECT COUNT(*) FROM students WHERE email IN (?, ?)")) {
                check.setString(1, "anitha@example.com");
                check.setString(2, "bala@example.com");
                try (ResultSet rs = check.executeQuery()) {
                    rs.next();
                    System.out.println("Rows in table for Anitha/Bala: " + rs.getInt(1) + " (only Anitha)");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
Inserted Anitha
Inserted Bala
Rolled back to savepoint (Bala undone)
Committed
Rows in table for Anitha/Bala: 1 (only Anitha)
*/
