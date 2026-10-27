import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * J14 — Call a stored procedure with CallableStatement.
 *
 * The procedure (created in 01_setup.sql):
 *   count_students_above_age(IN min_age INT, OUT total INT)
 *
 * Concepts: {call ...} escape syntax, IN parameters (setInt),
 *           OUT parameters (registerOutParameter + getInt).
 */
public class J14_StoredProcedureDemo {
    public static void main(String[] args) {

        String call = "{call count_students_above_age(?, ?)}";

        try (Connection con = DB.getConnection();
             CallableStatement cs = con.prepareCall(call)) {

            cs.setInt(1, 21);                             // IN  parameter
            cs.registerOutParameter(2, Types.INTEGER);    // OUT parameter: tell JDBC its type first

            cs.execute();

            System.out.println("Students aged 21 or above: " + cs.getInt(2));

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
Students aged 21 or above: 5
*/
