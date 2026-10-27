import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * J01 — Your first JDBC program: open a connection to MySQL.
 *
 * Concepts: JDBC URL, DriverManager, Connection, try-with-resources, SQLException.
 * Before running: run sql/01_setup.sql and set the DB_PASSWORD environment variable.
 */
public class J01_ConnectionDemo {
    public static void main(String[] args) {

        // jdbc:mysql://  host    : port / database
        String url = "jdbc:mysql://localhost:3306/college_db";

        String user = System.getenv().getOrDefault("DB_USER", "root");
        String password = System.getenv("DB_PASSWORD");   // never hard-code passwords

        // try-with-resources: the connection is closed automatically at the closing brace
        try (Connection con = DriverManager.getConnection(url, user, password)) {

            System.out.println("Database connected successfully!");
            System.out.println("Connected to : " + con.getMetaData().getDatabaseProductName()
                               + " " + con.getMetaData().getDatabaseProductVersion());
            System.out.println("Is it open?  : " + !con.isClosed());

        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
Database connected successfully!
Connected to : MySQL 8.x.x  (your server version)
Is it open?  : true
*/
