import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Main — the USER INTERFACE: shows the menu, reads input, prints results.
 * It never writes SQL (that's StudentDAO) and never writes regex (that's StudentValidator).
 *
 *   Main  ->  StudentValidator  (is the input OK?)
 *   Main  ->  StudentDAO        (save / load)  ->  DBConnection  ->  MySQL
 *
 * Compile & run from the v2-multi-class folder:
 *   Windows:   javac -d out src\*.java
 *              java -cp "out;..\..\lib\*" Main
 *   Mac/Linux: javac -d out src/*.java
 *              java -cp "out:../../lib/*" Main
 */
public class Main {

    private static final Scanner in = new Scanner(System.in);
    private static final StudentDAO dao = new StudentDAO();

    public static void main(String[] args) {
        while (true) {
            System.out.println("""

                ===== STUDENT REGISTRATION (v2) =====
                1. Register new student
                2. View all students
                3. Search by ID
                4. Search by name
                5. Update email / phone
                6. Delete student
                0. Exit""");

            try {
                switch (ask("Choose")) {
                    case "1" -> register();
                    case "2" -> print(dao.findAll());
                    case "3" -> searchById();
                    case "4" -> print(dao.findByName(ask("Part of the name")));
                    case "5" -> update();
                    case "6" -> delete();
                    case "0" -> { System.out.println("Bye!"); return; }
                    default  -> System.out.println("Please enter a number from the menu.");
                }
            } catch (SQLIntegrityConstraintViolationException e) {
                if (e.getMessage().contains("email"))      System.out.println("[X] That email is already registered.");
                else if (e.getMessage().contains("phone")) System.out.println("[X] That phone number is already registered.");
                else                                       System.out.println("[X] Rejected by the database: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("[X] Database error: " + e.getMessage());
                System.out.println("    Is MySQL running? Is DB_PASSWORD set?");
            }
        }
    }

    private static void register() throws SQLException {
        String name  = ask("Name").strip().replaceAll("\\s+", " ");
        String email = ask("Email").strip().toLowerCase();
        String phone = ask("Phone (10 digits)").strip();
        String ageIn = ask("Age (blank if unknown)").strip();
        String dept  = ask("Department (CSE/ECE/EEE/IT/MECH/CIVIL)").strip().toUpperCase();

        Integer age = null;
        if (!ageIn.isEmpty()) {
            if (!ageIn.matches("\\d{1,3}")) { System.out.println("[X] Age must be a number."); return; }
            age = Integer.parseInt(ageIn);
        }

        Student s = new Student(null, name, email, phone, age, dept);

        List<String> errors = StudentValidator.validate(s);
        if (!errors.isEmpty()) {
            System.out.println("[X] Not saved. Please fix:");
            errors.forEach(err -> System.out.println("   - " + err));
            return;
        }

        int id = dao.insert(s);
        System.out.println("[OK] Registered! Student ID = " + id);
    }

    private static void searchById() throws SQLException {
        Integer id = askInt("Student ID");
        if (id == null) return;
        Optional<Student> s = dao.findById(id);
        if (s.isPresent()) print(List.of(s.get()));
        else               System.out.println("[X] No student with ID " + id);
    }

    private static void update() throws SQLException {
        Integer id = askInt("Student ID to update");
        if (id == null) return;
        String email = ask("New email").strip().toLowerCase();
        String phone = ask("New phone").strip();

        if (!StudentValidator.isValidEmail(email) || !StudentValidator.isValidPhone(phone)) {
            System.out.println("[X] Not updated: check the email and phone format.");
            return;
        }
        System.out.println(dao.updateContact(id, email, phone) ? "[OK] Updated." : "[X] No student with ID " + id);
    }

    private static void delete() throws SQLException {
        Integer id = askInt("Student ID to delete");
        if (id == null) return;
        if (!ask("Type YES to confirm").equals("YES")) { System.out.println("Cancelled."); return; }
        System.out.println(dao.delete(id) ? "[OK] Deleted." : "[X] No student with ID " + id);
    }

    // ---------- console helpers ----------
    private static String ask(String prompt) {
        System.out.print(prompt + ": ");
        return in.hasNextLine() ? in.nextLine() : "0";
    }

    private static Integer askInt(String prompt) {
        String s = ask(prompt).strip();
        if (!s.matches("\\d{1,9}")) { System.out.println("[X] Please enter a whole number."); return null; }
        return Integer.parseInt(s);
    }

    private static void print(List<Student> students) {
        System.out.printf("%-4s %-14s %-24s %-11s %-4s %s%n", "ID", "NAME", "EMAIL", "PHONE", "AGE", "DEPT");
        students.forEach(System.out::println);
        System.out.println(students.isEmpty() ? "(no matching students)" : students.size() + " row(s).");
    }
}
