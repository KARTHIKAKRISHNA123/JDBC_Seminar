import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection — the only class that knows WHERE the database is and HOW to log in.
 * Change databases (or move to a connection pool like HikariCP) by editing only this file.
 */
public class DBConnection {

    private static final String URL  = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/college_db");
    private static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    private static final String PASS = System.getenv().getOrDefault("DB_PASSWORD", "");

    private DBConnection() { }   // utility class: no objects needed

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
