/**
 * R04 — Password rules using LOOKAHEADS.
 *
 * Rule: at least 8 characters, no spaces, and at least one
 *       uppercase, one lowercase, one digit, one special character.
 *
 * Regex:  ^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[^A-Za-z0-9\s])\S{8,}$
 *   (?=.*[A-Z])          "somewhere ahead there is an uppercase letter"
 *   (?=.*[a-z])          ... a lowercase letter
 *   (?=.*\d)             ... a digit
 *   (?=.*[^A-Za-z0-9\s]) ... a character that is NOT letter/digit/space = a special char
 *   \S{8,}               the actual password: 8+ non-space characters
 *
 * A lookahead checks a condition WITHOUT moving forward, so all four checks start
 * from the beginning. Think of four security guards each checking one thing.
 *
 * Also shows WHICH rule failed — users need that, not just "invalid".
 * Real systems must also hash passwords (bcrypt/Argon2); regex only checks the shape.
 */
public class R04_PasswordValidation {

    static final String RULE =
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s])\\S{8,}$";

    static String explain(String p) {
        StringBuilder sb = new StringBuilder();
        if (p.length() < 8)                    sb.append("too short; ");
        if (!p.matches(".*[A-Z].*"))           sb.append("no uppercase; ");
        if (!p.matches(".*[a-z].*"))           sb.append("no lowercase; ");
        if (!p.matches(".*\\d.*"))             sb.append("no digit; ");
        if (!p.matches(".*[^A-Za-z0-9\\s].*")) sb.append("no special char; ");
        if (p.matches(".*\\s.*"))              sb.append("contains space; ");
        return sb.toString();
    }

    public static void main(String[] args) {

        String[] inputs = { "Java@2026", "Kk#12345", "java@2026", "JAVA@2026",
                            "Java2026", "J@v1", "Java @2026" };

        for (String p : inputs) {
            if (p.matches(RULE)) {
                System.out.printf("%-12s -> STRONG%n", p);
            } else {
                System.out.printf("%-12s -> weak: %s%n", p, explain(p));
            }
        }
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R04_PasswordValidation.java) =======================
Java@2026    -> STRONG
Kk#12345     -> STRONG
java@2026    -> weak: no uppercase; 
JAVA@2026    -> weak: no lowercase; 
Java2026     -> weak: no special char; 
J@v1         -> weak: too short; 
Java @2026   -> weak: contains space; 
*/
