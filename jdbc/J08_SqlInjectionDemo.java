import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * J08 — Why Statement + string concatenation is dangerous (SQL injection),
 *       and how PreparedStatement fixes it.
 *
 * Scenario: a "find my record" screen where the user types their email.
 * The attacker types:   x' OR '1'='1
 * Run this only against your own local practice database.
 */
public class J08_SqlInjectionDemo {
    public static void main(String[] args) {

        String userInput = "x' OR '1'='1";

        try (Connection con = DB.getConnection()) {

            // ---------- UNSAFE: user text is glued into the SQL ----------
            String unsafeSql = "SELECT id, name FROM students WHERE email = '" + userInput + "'";
            System.out.println("UNSAFE SQL sent to MySQL:");
            System.out.println("  " + unsafeSql);

            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(unsafeSql)) {
                System.out.println("Rows returned: " + countRows(rs) + "   <-- every student leaked!");
            }

            // ---------- SAFE: the value travels separately from the SQL ----------
            String safeSql = "SELECT id, name FROM students WHERE email = ?";
            System.out.println("\nSAFE SQL sent to MySQL:");
            System.out.println("  " + safeSql + "    with value [" + userInput + "]");

            try (PreparedStatement ps = con.prepareStatement(safeSql)) {
                ps.setString(1, userInput);   // treated as plain text, never as SQL code
                try (ResultSet rs = ps.executeQuery()) {
                    System.out.println("Rows returned: " + countRows(rs) + "   <-- no student has that literal email");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    static int countRows(ResultSet rs) throws SQLException {
        int n = 0;
        while (rs.next()) n++;
        return n;
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
UNSAFE SQL sent to MySQL:
  SELECT id, name FROM students WHERE email = 'x' OR '1'='1'
Rows returned: 5   <-- every student leaked!

SAFE SQL sent to MySQL:
  SELECT id, name FROM students WHERE email = ?    with value [x' OR '1'='1]
Rows returned: 0   <-- no student has that literal email
*/
