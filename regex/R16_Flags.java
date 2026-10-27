import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * R16 — Flags change how the pattern behaves.
 *
 *   Pattern.CASE_INSENSITIVE  or (?i)  : java == JAVA == Java
 *   Pattern.MULTILINE         or (?m)  : ^ and $ match at the start/end of EVERY line
 *   Pattern.DOTALL            or (?s)  : . also matches newline characters
 *   Pattern.UNICODE_CHARACTER_CLASS or (?U) : \w also matches Tamil/Hindi letters, etc.
 */
public class R16_Flags {

    static int count(Pattern p, String text) {
        Matcher m = p.matcher(text);
        int n = 0;
        while (m.find()) n++;
        return n;
    }

    public static void main(String[] args) {

        // CASE_INSENSITIVE
        System.out.println("\"JAVA\".matches(\"java\")     = " + "JAVA".matches("java"));
        System.out.println("\"JAVA\".matches(\"(?i)java\") = " + "JAVA".matches("(?i)java"));

        // MULTILINE
        String lines = "Java is fun\nPython is fun\nJava is fast";
        System.out.println("\nLines starting with Java, without MULTILINE: "
                + count(Pattern.compile("^Java"), lines));
        System.out.println("Lines starting with Java, with MULTILINE   : "
                + count(Pattern.compile("^Java", Pattern.MULTILINE), lines));

        // DOTALL
        String twoLines = "START\nEND";
        System.out.println("\n\"START.*END\" without DOTALL: " + twoLines.matches("START.*END"));
        System.out.println("\"START.*END\" with    DOTALL: " + twoLines.matches("(?s)START.*END"));

        // UNICODE_CHARACTER_CLASS
        String tamil = "வணக்கம்";
        System.out.println("\n\\w+ on Tamil word, default : " + tamil.matches("\\w+"));
        System.out.println("\\w+ on Tamil word, (?U)    : " + tamil.matches("(?U)\\w+"));

        // Combine flags with |
        Pattern both = Pattern.compile("^java", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        System.out.println("\nCASE_INSENSITIVE | MULTILINE count: " + count(both, lines));
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R16_Flags.java) =======================
"JAVA".matches("java")     = false
"JAVA".matches("(?i)java") = true

Lines starting with Java, without MULTILINE: 1
Lines starting with Java, with MULTILINE   : 2

"START.*END" without DOTALL: false
"START.*END" with    DOTALL: true

\w+ on Tamil word, default : false
\w+ on Tamil word, (?U)    : true

CASE_INSENSITIVE | MULTILINE count: 2
*/
