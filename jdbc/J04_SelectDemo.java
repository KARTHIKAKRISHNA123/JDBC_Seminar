import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * J04 — READ: fetch every student and print them as a table.
 *
 * Concepts: executeQuery(), ResultSet, the cursor (starts BEFORE row 1), rs.next(), getXxx("column").
 * A plain Statement is fine here because the SQL has no user input.
 */
public class J04_SelectDemo {
    public static void main(String[] args) {

        String sql = "SELECT id, name, email, age, department FROM students ORDER BY id";

        try (Connection con = DB.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            System.out.printf("%-3s %-11s %-22s %-4s %s%n", "ID", "NAME", "EMAIL", "AGE", "DEPT");
            System.out.println("-".repeat(50));

            int count = 0;
            while (rs.next()) {                    // move cursor to the next row; false when no rows left
                System.out.printf("%-3d %-11s %-22s %-4s %s%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("age"),       // getString shows NULL as "null"; getInt would show 0
                        rs.getString("department"));
                count++;
            }
            System.out.println(count + " student(s) found.");

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
ID  NAME        EMAIL                  AGE  DEPT
--------------------------------------------------
1   Arun Kumar  arun@example.com       20   CSE
2   Priya S     priya@example.com      21   ECE
3   Karthik R   karthik@example.com    22   CSE
4   Divya M     divya@example.com      19   IT
5   Meena L     meena@example.com      null MECH
6   Rahul V     rahul@example.com      23   EEE
6 student(s) found.
*/
