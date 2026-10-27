import java.util.Arrays;

/**
 * R08 — Split text that uses several different separators.
 *
 * Regex:  [,;|\s]+
 *   [ ... ]   any ONE of: comma, semicolon, pipe, whitespace
 *   +         one or more in a row count as a single separator
 *
 * Inside [ ], | is a plain character (not "OR").
 */
public class R08_SplitDelimiters {
    public static void main(String[] args) {

        String skills = "Java,Python; C++ |SQL,,  MongoDB";

        String[] parts = skills.split("[,;|\\s]+");

        System.out.println("Input : " + skills);
        System.out.println("Parts : " + Arrays.toString(parts));
        System.out.println("Count : " + parts.length);

        // Classic trap: split(".") gives an EMPTY array because "." means "any character"
        System.out.println("\"a.b.c\".split(\".\")   length = " + "a.b.c".split(".").length);
        System.out.println("\"a.b.c\".split(\"\\\\.\") length = " + "a.b.c".split("\\.").length);
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R08_SplitDelimiters.java) =======================
Input : Java,Python; C++ |SQL,,  MongoDB
Parts : [Java, Python, C++, SQL, MongoDB]
Count : 5
"a.b.c".split(".")   length = 0
"a.b.c".split("\\.") length = 3
*/
