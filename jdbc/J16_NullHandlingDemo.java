import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

/**
 * J16 — SQL NULL vs Java values.
 *
 * Trap: rs.getInt("age") returns 0 when the column is NULL. 0 is a real age-like number,
 *       so you can't tell "age 0" from "age unknown" without wasNull() or getObject().
 * Concepts: setNull(), wasNull(), getObject(column, Integer.class).
 */
public class J16_NullHandlingDemo {
    public static void main(String[] args) {

        try (Connection con = DB.getConnection()) {

            // Writing NULL: you cannot do ps.setInt(2, null) — int can't be null. Use setNull.
            String insert = "INSERT INTO students (name, email, age) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insert)) {
                ps.setString(1, "Gopi S");
                ps.setString(2, "gopi@example.com");
                ps.setNull(3, Types.INTEGER);          // age unknown
                ps.executeUpdate();
                System.out.println("Inserted Gopi with age = NULL");
            }

            // Reading NULL three ways
            String select = "SELECT age FROM students WHERE email = ?";
            try (PreparedStatement ps = con.prepareStatement(select)) {
                ps.setString(1, "gopi@example.com");
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();

                    int asInt = rs.getInt("age");
                    System.out.println("getInt(\"age\")              -> " + asInt + "   (misleading!)");
                    System.out.println("wasNull()                  -> " + rs.wasNull());

                    Integer asObject = rs.getObject("age", Integer.class);
                    System.out.println("getObject(\"age\", Integer) -> " + asObject);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
Inserted Gopi with age = NULL
getInt("age")              -> 0   (misleading!)
wasNull()                  -> true
getObject("age", Integer) -> null
*/
