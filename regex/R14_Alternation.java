import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * R14 — Alternation ( | ) and non-capturing groups (?: ).
 *
 *   cat|dog            "cat" OR "dog"
 *   gr(a|e)y           "gray" or "grey"   (the group limits what | applies to)
 *   (?:Mr|Ms|Dr)\.     group WITHOUT creating group(1) — use when you only need grouping
 *
 * Trap: ^cat|dog$ means (^cat) OR (dog$), not ^(cat|dog)$.
 */
public class R14_Alternation {
    public static void main(String[] args) {

        // 1) Simple alternation
        for (String s : new String[] {"cat", "dog", "cow"}) {
            System.out.println(s + " matches cat|dog ? " + s.matches("cat|dog"));
        }

        // 2) Alternation inside a group
        for (String s : new String[] {"gray", "grey", "groy"}) {
            System.out.println(s + " matches gr(a|e)y ? " + s.matches("gr(a|e)y"));
        }

        // 3) Find every programming language mentioned
        String text = "I code in Java and Python, and I'm learning SQL. Not JavaScript though.";
        Matcher m = Pattern.compile("\\b(?:Java|Python|SQL|C\\+\\+)\\b").matcher(text);
        System.out.print("\nLanguages found:");
        while (m.find()) System.out.print(" " + m.group());
        System.out.println("   (JavaScript skipped thanks to \\b)");

        // 4) Capturing vs non-capturing: count the groups
        System.out.println("\n(Mr|Ms|Dr)\\.   groupCount = " + Pattern.compile("(Mr|Ms|Dr)\\.").matcher("").groupCount());
        System.out.println("(?:Mr|Ms|Dr)\\. groupCount = " + Pattern.compile("(?:Mr|Ms|Dr)\\.").matcher("").groupCount());
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R14_Alternation.java) =======================
cat matches cat|dog ? true
dog matches cat|dog ? true
cow matches cat|dog ? false
gray matches gr(a|e)y ? true
grey matches gr(a|e)y ? true
groy matches gr(a|e)y ? false

Languages found: Java Python SQL   (JavaScript skipped thanks to \b)

(Mr|Ms|Dr)\.   groupCount = 1
(?:Mr|Ms|Dr)\. groupCount = 0
*/
