import java.util.regex.Pattern;

/**
 * R02 — Check whether text LOOKS like an email address.
 *
 * Regex:  [A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}
 *   [A-Za-z0-9._%+-]+   one or more allowed characters before @
 *   @                   a literal @
 *   [A-Za-z0-9.-]+      domain name part (gmail, annauniv.edu ...)
 *   \.                  a literal dot  (written "\\." in a Java string)
 *   [A-Za-z]{2,}        top-level domain: 2 or more letters (in, com, org)
 *
 * IMPORTANT: this is a simplified "email-like" check, not the full email standard.
 * The only real proof that an email exists is sending a verification mail.
 * Pattern.compile() once + reuse is faster than String.matches() in a loop.
 */
public class R02_EmailLikeValidation {

    private static final Pattern EMAIL =
        Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");

    public static void main(String[] args) {

        String[] inputs = {
            "kk@example.com",
            "karthika.krishna@annauniv.edu.in",
            "student+java@gmail.com",
            "kk@example",          // no .tld
            "kk.example.com",      // no @
            "@example.com",        // nothing before @
            "kk@@example.com",     // two @
            "kk@exa mple.com"      // space
        };

        for (String email : inputs) {
            boolean ok = EMAIL.matcher(email).matches();
            System.out.printf("%-34s -> %s%n", email, ok ? "looks valid" : "invalid");
        }
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R02_EmailLikeValidation.java) =======================
kk@example.com                     -> looks valid
karthika.krishna@annauniv.edu.in   -> looks valid
student+java@gmail.com             -> looks valid
kk@example                         -> invalid
kk.example.com                     -> invalid
@example.com                       -> invalid
kk@@example.com                    -> invalid
kk@exa mple.com                    -> invalid
*/
