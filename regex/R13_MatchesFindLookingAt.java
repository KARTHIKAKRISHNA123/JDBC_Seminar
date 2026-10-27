import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * R13 — The most misunderstood trio:
 *
 *   matches()    the ENTIRE input must match the pattern       (validation)
 *   lookingAt()  the pattern must match at the START of input   (prefix check)
 *   find()       the pattern may appear ANYWHERE in the input   (search)
 *
 * Analogy with an exam hall ticket check:
 *   matches()   -> every detail on the ticket must be correct
 *   lookingAt() -> only the first line (your name) must be correct
 *   find()      -> "is your roll number written anywhere on this page?"
 */
public class R13_MatchesFindLookingAt {
    public static void main(String[] args) {

        Pattern tenDigits = Pattern.compile("\\d{10}");

        String[] inputs = { "9876543210", "9876543210 is my number", "My number is 9876543210" };

        System.out.printf("%-26s %-10s %-11s %s%n", "INPUT", "matches()", "lookingAt()", "find()");
        for (String s : inputs) {
            Matcher m1 = tenDigits.matcher(s);
            Matcher m2 = tenDigits.matcher(s);
            Matcher m3 = tenDigits.matcher(s);
            System.out.printf("%-26s %-10s %-11s %s%n",
                    "\"" + s + "\"", m1.matches(), m2.lookingAt(), m3.find());
        }

        // String.matches() is the same as Pattern.matches() -> whole string
        System.out.println("\n\"My number is 9876543210\".matches(\"\\\\d{10}\") = "
                           + "My number is 9876543210".matches("\\d{10}"));
        // To "search" with String.matches you'd need .* around it:
        System.out.println("\"My number is 9876543210\".matches(\".*\\\\d{10}.*\") = "
                           + "My number is 9876543210".matches(".*\\d{10}.*"));
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R13_MatchesFindLookingAt.java) =======================
INPUT                      matches()  lookingAt() find()
"9876543210"               true       true        true
"9876543210 is my number"  false      true        true
"My number is 9876543210"  false      false       true

"My number is 9876543210".matches("\\d{10}") = false
"My number is 9876543210".matches(".*\\d{10}.*") = true
*/
