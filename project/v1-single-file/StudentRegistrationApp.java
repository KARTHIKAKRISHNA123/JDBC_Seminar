import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * STUDENT REGISTRATION SYSTEM — version 1 (everything in one file)
 *
 * Flow:  User input -> Regex validation -> JDBC (PreparedStatement) -> MySQL -> result
 *
 * This version is easy to read top-to-bottom for a first demo. Version 2 (../v2-multi-class)
 * splits the same logic into Student, StudentValidator, StudentDAO, DBConnection and Main.
 *
 * Run (Windows):   java -cp "..\..\lib\*" StudentRegistrationApp.java
 * Run (Mac/Linux): java -cp "../../lib/*" StudentRegistrationApp.java
 */
public class StudentRegistrationApp {

    // ---------------- configuration (from environment variables) ----------------
    static final String URL  = System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/college_db");
    static final String USER = System.getenv().getOrDefault("DB_USER", "root");
    static final String PASS = System.getenv().getOrDefault("DB_PASSWORD", "");

    // ---------------- validation rules (regex) ----------------
    static final String NAME_REGEX  = "[A-Za-z][A-Za-z .']{1,99}";          // 2-100 chars, letters first
    static final String EMAIL_REGEX = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
    static final String PHONE_REGEX = "[6-9]\\d{9}";                         // Indian 10-digit mobile
    static final String AGE_REGEX   = "\\d{2}";                              // then range-checked 16-60
    static final String DEPT_REGEX  = "(?i)CSE|ECE|EEE|IT|MECH|CIVIL";

    static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("Connected to college_db.");

            while (true) {
                System.out.println("""

                    ===== STUDENT REGISTRATION =====
                    1. Register new student
                    2. View all students
                    3. Search by ID
                    4. Search by name
                    5. Update email / phone
                    6. Delete student
                    0. Exit""");
                String choice = ask("Choose");

                try {
                    switch (choice) {
                        case "1" -> register(con);
                        case "2" -> viewAll(con);
                        case "3" -> searchById(con);
                        case "4" -> searchByName(con);
                        case "5" -> update(con);
                        case "6" -> delete(con);
                        case "0" -> { System.out.println("Bye!"); return; }
                        default  -> System.out.println("Please enter a number from the menu.");
                    }
                } catch (SQLIntegrityConstraintViolationException e) {
                    // UNIQUE / CHECK / FOREIGN KEY problems -> explain in plain words
                    if (e.getMessage().contains("email"))      System.out.println("[X] That email is already registered.");
                    else if (e.getMessage().contains("phone")) System.out.println("[X] That phone number is already registered.");
                    else                                       System.out.println("[X] Rejected by the database: " + e.getMessage());
                } catch (SQLException e) {
                    System.out.println("[X] Database error: " + e.getMessage());
                }
            }

        } catch (SQLException e) {
            System.out.println("Could not connect: " + e.getMessage());
            System.out.println("Check that MySQL is running and DB_PASSWORD is set.");
        }
    }

    // ---------------- 1. CREATE ----------------
    static void register(Connection con) throws SQLException {
        String name  = ask("Name").strip().replaceAll("\\s+", " ");   // tidy spaces (R07)
        String email = ask("Email").strip().toLowerCase();
        String phone = ask("Phone (10 digits)").strip();
        String age   = ask("Age (blank if unknown)").strip();
        String dept  = ask("Department (CSE/ECE/EEE/IT/MECH/CIVIL)").strip().toUpperCase();

        List<String> errors = new ArrayList<>();
        if (!name.matches(NAME_REGEX))   errors.add("Name: letters, spaces, dot or apostrophe only (2-100 chars).");
        if (!email.matches(EMAIL_REGEX)) errors.add("Email: must look like name@domain.com");
        if (!phone.matches(PHONE_REGEX)) errors.add("Phone: exactly 10 digits, starting with 6-9.");
        if (!age.isEmpty() && (!age.matches(AGE_REGEX) || Integer.parseInt(age) < 16 || Integer.parseInt(age) > 60))
                                         errors.add("Age: a number between 16 and 60.");
        if (!dept.matches(DEPT_REGEX))   errors.add("Department: one of CSE, ECE, EEE, IT, MECH, CIVIL.");

        if (!errors.isEmpty()) {
            System.out.println("[X] Not saved. Please fix:");
            errors.forEach(err -> System.out.println("   - " + err));
            return;
        }

        String sql = "INSERT INTO students (name, email, phone, age, department) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, phone);
            if (age.isEmpty()) ps.setNull(4, Types.INTEGER); else ps.setInt(4, Integer.parseInt(age));
            ps.setString(5, dept);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) System.out.println("[OK] Registered! Student ID = " + keys.getInt(1));
            }
        }
    }

    // ---------------- 2. READ all ----------------
    static void viewAll(Connection con) throws SQLException {
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                 "SELECT id, name, email, phone, age, department FROM students ORDER BY id")) {
            printTable(rs);
        }
    }

    // ---------------- 3. READ one ----------------
    static void searchById(Connection con) throws SQLException {
        Integer id = askInt("Student ID");
        if (id == null) return;
        try (PreparedStatement ps = con.prepareStatement(
                 "SELECT id, name, email, phone, age, department FROM students WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { printTable(rs); }
        }
    }

    // ---------------- 4. READ by partial name (LIKE with a parameter) ----------------
    static void searchByName(Connection con) throws SQLException {
        String term = ask("Part of the name");
        try (PreparedStatement ps = con.prepareStatement(
                 "SELECT id, name, email, phone, age, department FROM students WHERE name LIKE ? ORDER BY name")) {
            ps.setString(1, "%" + term + "%");     // the % wildcards go in the VALUE, not the SQL
            try (ResultSet rs = ps.executeQuery()) { printTable(rs); }
        }
    }

    // ---------------- 5. UPDATE ----------------
    static void update(Connection con) throws SQLException {
        Integer id = askInt("Student ID to update");
        if (id == null) return;
        String email = ask("New email").strip().toLowerCase();
        String phone = ask("New phone").strip();

        if (!email.matches(EMAIL_REGEX) || !phone.matches(PHONE_REGEX)) {
            System.out.println("[X] Not updated: check the email and phone format.");
            return;
        }
        try (PreparedStatement ps = con.prepareStatement(
                 "UPDATE students SET email = ?, phone = ? WHERE id = ?")) {
            ps.setString(1, email);
            ps.setString(2, phone);
            ps.setInt(3, id);
            int rows = ps.executeUpdate();
            System.out.println(rows == 1 ? "[OK] Updated." : "[X] No student with ID " + id);
        }
    }

    // ---------------- 6. DELETE ----------------
    static void delete(Connection con) throws SQLException {
        Integer id = askInt("Student ID to delete");
        if (id == null) return;
        if (!ask("Type YES to confirm").equals("YES")) {
            System.out.println("Cancelled.");
            return;
        }
        try (PreparedStatement ps = con.prepareStatement("DELETE FROM students WHERE id = ?")) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows == 1 ? "[OK] Deleted." : "[X] No student with ID " + id);
        }
    }

    // ---------------- helpers ----------------
    static String ask(String prompt) {
        System.out.print(prompt + ": ");
        return in.hasNextLine() ? in.nextLine() : "0";
    }

    static Integer askInt(String prompt) {
        String s = ask(prompt).strip();
        if (!s.matches("\\d{1,9}")) {           // regex guards Integer.parseInt from crashing
            System.out.println("[X] Please enter a whole number.");
            return null;
        }
        return Integer.parseInt(s);
    }

    static void printTable(ResultSet rs) throws SQLException {
        System.out.printf("%-4s %-14s %-24s %-11s %-4s %s%n", "ID", "NAME", "EMAIL", "PHONE", "AGE", "DEPT");
        int count = 0;
        while (rs.next()) {
            Object age = rs.getObject("age");                    // keeps NULL as null
            System.out.printf("%-4d %-14s %-24s %-11s %-4s %s%n",
                    rs.getInt("id"), rs.getString("name"), rs.getString("email"),
                    rs.getString("phone") == null ? "-" : rs.getString("phone"),
                    age == null ? "-" : age, rs.getString("department"));
            count++;
        }
        System.out.println(count == 0 ? "(no matching students)" : count + " row(s).");
    }
}
