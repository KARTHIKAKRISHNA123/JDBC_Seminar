import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * R09 — Find accidentally repeated words ("the the") using a BACKREFERENCE.
 *
 * Regex:  \b(\w+)\s+\1\b
 *   \b      word boundary (so "the theory" is NOT a repeat)
 *   (\w+)   capture a word into group 1
 *   \s+     whitespace between the words
 *   \1      "the exact same text that group 1 captured"
 *
 * Real use: grammar checkers, cleaning up typed notes.
 */
public class R09_RepeatedWords {
    public static void main(String[] args) {

        String text = "JDBC is is a Java API. The the driver talks to MySQL. "
                    + "Read the theory first, then then practise.";

        Pattern p = Pattern.compile("\\b(\\w+)\\s+\\1\\b", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);

        while (m.find()) {
            System.out.println("Repeated: \"" + m.group() + "\"  (word = " + m.group(1) + ")");
        }

        // Fix them: replace "word word" with "word" using $1 (group 1) in the replacement
        String fixed = p.matcher(text).replaceAll("$1");
        System.out.println("\nFixed: " + fixed);
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R09_RepeatedWords.java) =======================
Repeated: "is is"  (word = is)
Repeated: "The the"  (word = The)
Repeated: "then then"  (word = then)

Fixed: JDBC is a Java API. The driver talks to MySQL. Read the theory first, then practise.
*/
