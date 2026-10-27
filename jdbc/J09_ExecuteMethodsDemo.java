import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * J09 — execute() vs executeQuery() vs executeUpdate().
 *
 *   executeQuery(sql)  -> for SELECT; returns a ResultSet
 *   executeUpdate(sql) -> for INSERT/UPDATE/DELETE (and DDL); returns an int (rows changed)
 *   execute(sql)       -> for "I don't know yet"; returns true if a ResultSet came back
 */
public class J09_ExecuteMethodsDemo {
    public static void main(String[] args) {

        try (Connection con = DB.getConnection();
             Statement st = con.createStatement()) {

            // 1) executeQuery
            try (ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM students")) {
                rs.next();
                System.out.println("executeQuery  -> count = " + rs.getInt(1));   // column index 1
            }

            // 2) executeUpdate  (changes nothing real: sets CSE students' department to CSE)
            int rows = st.executeUpdate("UPDATE students SET department = 'CSE' WHERE department = 'CSE'");
            System.out.println("executeUpdate -> rows matched/changed = " + rows);

            // 3) execute with a SELECT
            boolean isResultSet = st.execute("SELECT name FROM students WHERE id = 1");
            System.out.println("execute(SELECT) returned " + isResultSet);
            if (isResultSet) {
                try (ResultSet rs = st.getResultSet()) {
                    rs.next();
                    System.out.println("   first name = " + rs.getString("name"));
                }
            }

            // 4) execute with an UPDATE
            isResultSet = st.execute("UPDATE students SET age = age WHERE id = 1");
            System.out.println("execute(UPDATE) returned " + isResultSet
                               + ", update count = " + st.getUpdateCount());

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
executeQuery  -> count = 5
executeUpdate -> rows matched/changed = 2
execute(SELECT) returned true
   first name = Arun Kumar
execute(UPDATE) returned false, update count = 1
*/
