import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * J11 — Batch processing: send many INSERTs in one trip.
 *
 * Real use: importing 500 students from an Excel/CSV sheet.
 * Concepts: addBatch(), executeBatch() returns one count per statement;
 *           wrapped in a transaction so it is all-or-nothing.
 */
public class J11_BatchInsertDemo {
    public static void main(String[] args) {

        String[][] newStudents = {
            {"Vijay K",   "vijay@example.com",   "21"},
            {"Lakshmi N", "lakshmi@example.com", "20"},
            {"Suresh T",  "suresh@example.com",  "22"}
        };

        String sql = "INSERT INTO students (name, email, age) VALUES (?, ?, ?)";

        try (Connection con = DB.getConnection()) {
            con.setAutoCommit(false);                       // start a transaction

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                for (String[] s : newStudents) {
                    ps.setString(1, s[0]);
                    ps.setString(2, s[1]);
                    ps.setInt(3, Integer.parseInt(s[2]));
                    ps.addBatch();                          // queue it, don't send yet
                }

                int[] counts = ps.executeBatch();           // send all queued statements
                con.commit();
                System.out.println("Batch result per row: " + Arrays.toString(counts));
                System.out.println("Inserted " + counts.length + " students in one batch.");

            } catch (SQLException e) {
                con.rollback();                             // one failed -> undo all
                System.out.println("Batch failed, rolled back: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
Batch result per row: [1, 1, 1]
Inserted 3 students in one batch.
*/
