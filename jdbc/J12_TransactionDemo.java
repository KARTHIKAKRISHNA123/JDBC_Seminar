import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * J12 — Transactions: a bank transfer must be ALL or NOTHING.
 *
 * Concepts: setAutoCommit(false), commit(), rollback(), atomicity.
 * Money uses BigDecimal, never double (0.1 + 0.2 != 0.3 in double!).
 * Uses the accounts table (101 Arun = 5000, 102 Priya = 3000) from 01_setup.sql.
 *
 * Three scenarios:
 *   1. 500 from 101 to 102           -> succeeds, committed
 *   2. 1000 from 101 to account 999  -> debit worked, credit found no account -> rolled back
 *   3. 99999 from 101 to 102         -> debit breaks CHECK (balance >= 0)      -> rolled back
 */
public class J12_TransactionDemo {

    static void transfer(Connection con, int from, int to, BigDecimal amount) throws SQLException {
        String debit  = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
        String credit = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";

        con.setAutoCommit(false);              // from now on, nothing is saved until commit()
        try (PreparedStatement d = con.prepareStatement(debit);
             PreparedStatement c = con.prepareStatement(credit)) {

            d.setBigDecimal(1, amount);
            d.setInt(2, from);
            if (d.executeUpdate() != 1) throw new SQLException("Source account " + from + " not found");

            c.setBigDecimal(1, amount);
            c.setInt(2, to);
            if (c.executeUpdate() != 1) throw new SQLException("Target account " + to + " not found");

            con.commit();                      // both steps worked -> make them permanent
            System.out.println("  COMMITTED: moved " + amount + " from " + from + " to " + to);

        } catch (SQLException e) {
            con.rollback();                    // anything failed -> undo the debit too
            System.out.println("  ROLLED BACK: " + e.getMessage());
        } finally {
            con.setAutoCommit(true);           // put the connection back to normal
        }
    }

    static void printBalances(Connection con) throws SQLException {
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT account_id, holder, balance FROM accounts ORDER BY account_id")) {
            while (rs.next()) {
                System.out.println("  " + rs.getInt(1) + " " + rs.getString(2) + " = " + rs.getBigDecimal(3));
            }
        }
    }

    public static void main(String[] args) {
        try (Connection con = DB.getConnection()) {
            System.out.println("Before:");
            printBalances(con);

            System.out.println("\n1) Valid transfer");
            transfer(con, 101, 102, new BigDecimal("500.00"));
            printBalances(con);

            System.out.println("\n2) Transfer to a missing account");
            transfer(con, 101, 999, new BigDecimal("1000.00"));
            printBalances(con);

            System.out.println("\n3) Transfer more than the balance");
            transfer(con, 101, 102, new BigDecimal("99999.00"));
            printBalances(con);

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
Before:
  101 Arun = 5000.00
  102 Priya = 3000.00

1) Valid transfer
  COMMITTED: moved 500.00 from 101 to 102
  101 Arun = 4500.00
  102 Priya = 3500.00

2) Transfer to a missing account
  ROLLED BACK: Target account 999 not found
  101 Arun = 4500.00
  102 Priya = 3500.00

3) Transfer more than the balance
  ROLLED BACK: Check constraint 'chk_balance' is violated.
  101 Arun = 4500.00
  102 Priya = 3500.00
*/
