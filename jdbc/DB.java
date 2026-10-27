import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB — the ONE place that knows how to open a database connection.
 *
 * This is exactly the code from J01_ConnectionDemo, moved into a method so
 * every other demo can simply call DB.getConnection() instead of repeating
 * the URL, user and password in every file.
 *
 * Credentials come from environment variables, never from the source code:
 *   DB_URL      (optional) default: jdbc:mysql://localhost:3306/college_db
 *   DB_USER     (optional) default: root
 *   DB_PASSWORD (required unless your MySQL root password is empty, e.g. XAMPP)
 */
public class DB {

    private static final String URL =
        System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/college_db");

    private static final String USER =
        System.getenv().getOrDefault("DB_USER", "root");

    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        String password = PASSWORD;
        if (password == null) {
            System.out.println("[warning] DB_PASSWORD is not set - trying an empty password.");
            password = "";
        }
        return DriverManager.getConnection(URL, USER, password);
    }
}
