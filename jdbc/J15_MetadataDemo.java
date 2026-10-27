import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * J15 — Metadata: "data about the data".
 *
 *   DatabaseMetaData  -> facts about the database and driver (version, tables...)
 *   ResultSetMetaData -> facts about a query result (how many columns, names, types)
 *
 * Real use: database tools like MySQL Workbench and DBeaver use exactly this to draw their UI.
 */
public class J15_MetadataDemo {
    public static void main(String[] args) {

        try (Connection con = DB.getConnection()) {

            DatabaseMetaData db = con.getMetaData();
            System.out.println("Database : " + db.getDatabaseProductName() + " " + db.getDatabaseProductVersion());
            System.out.println("Driver   : " + db.getDriverName() + " " + db.getDriverVersion());
            System.out.println("JDBC URL : " + db.getURL());

            System.out.println("\nTables in college_db:");
            try (ResultSet tables = db.getTables("college_db", null, "%", new String[] {"TABLE"})) {
                while (tables.next()) {
                    System.out.println("  - " + tables.getString("TABLE_NAME"));
                }
            }

            System.out.println("\nColumns of SELECT * FROM students:");
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT * FROM students")) {

                ResultSetMetaData md = rs.getMetaData();
                for (int i = 1; i <= md.getColumnCount(); i++) {      // columns are numbered from 1
                    System.out.printf("  %d. %-11s %-10s nullable=%s%n",
                            i,
                            md.getColumnName(i),
                            md.getColumnTypeName(i),
                            md.isNullable(i) == ResultSetMetaData.columnNullable ? "yes" : "no");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

/* ======================= EXPECTED OUTPUT (after a fresh sql/01_setup.sql, running J01..J17 in order) =======================
Database : MySQL 8.x.x  (your server version)
Driver   : MySQL Connector/J mysql-connector-j-9.x.x (Revision: ...)
JDBC URL : jdbc:mysql://localhost:3306/college_db

Tables in college_db:
  - accounts
  - courses
  - enrollments
  - students

Columns of SELECT * FROM students:
  1. id          INT        nullable=no
  2. name        VARCHAR    nullable=no
  3. email       VARCHAR    nullable=no
  4. phone       CHAR       nullable=yes
  5. age         INT        nullable=yes
  6. department  VARCHAR    nullable=no
  7. created_at  TIMESTAMP  nullable=yes
*/
