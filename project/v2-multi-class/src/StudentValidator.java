import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * StudentValidator — all REGEX rules live here, in one place.
 *
 * Patterns are compiled ONCE (static final) and reused — faster than String.matches()
 * which recompiles the regex on every call.
 *
 * Validation here is the FIRST line of defence (friendly messages for the user).
 * The database CHECK / UNIQUE constraints are the LAST line (they protect the data even
 * if another program skips this class). And PreparedStatement — not regex — is what
 * stops SQL injection.
 */
public class StudentValidator {

    // ASCII-only name rule for the demo. For Tamil/Hindi names use [\p{L}\p{M} .'] instead.
    private static final Pattern NAME  = Pattern.compile("[A-Za-z][A-Za-z .']{1,99}");
    private static final Pattern EMAIL = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    private static final Pattern PHONE = Pattern.compile("[6-9]\\d{9}");
    private static final Pattern DEPT  = Pattern.compile("CSE|ECE|EEE|IT|MECH|CIVIL");

    private StudentValidator() { }

    public static boolean isValidEmail(String email) { return email != null && EMAIL.matcher(email).matches(); }
    public static boolean isValidPhone(String phone) { return phone != null && PHONE.matcher(phone).matches(); }

    /** Returns a list of problems. Empty list = the student is valid. */
    public static List<String> validate(Student s) {
        List<String> errors = new ArrayList<>();

        if (s.getName() == null || !NAME.matcher(s.getName()).matches())
            errors.add("Name: letters, spaces, dot or apostrophe only (2-100 chars).");

        if (!isValidEmail(s.getEmail()) || s.getEmail().length() > 150)
            errors.add("Email: must look like name@domain.com");

        if (!isValidPhone(s.getPhone()))
            errors.add("Phone: exactly 10 digits, starting with 6-9.");

        if (s.getAge() != null && (s.getAge() < 16 || s.getAge() > 60))
            errors.add("Age: a number between 16 and 60.");

        if (s.getDepartment() == null || !DEPT.matcher(s.getDepartment()).matches())
            errors.add("Department: one of CSE, ECE, EEE, IT, MECH, CIVIL.");

        return errors;
    }
}
