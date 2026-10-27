/**
 * R07 — Clean up messy whitespace (very common before saving form input).
 *
 *   \s+          one or more whitespace characters (space, tab, newline)
 *   replaceAll   replaces EVERY match
 *   strip()      removes leading/trailing whitespace (Java 11+)
 */
public class R07_ReplaceSpaces {
    public static void main(String[] args) {

        String messy = "   Karthika    Krishna\t\tM   ";

        String cleaned = messy.replaceAll("\\s+", " ").strip();

        System.out.println("Before: [" + messy + "]");
        System.out.println("After : [" + cleaned + "]");

        // Bonus: replaceAll treats its FIRST argument as a regex.
        // To replace a literal dot you must escape it:
        System.out.println("1.2.3".replaceAll(".", "-"));     // . = any char -> -----
        System.out.println("1.2.3".replaceAll("\\.", "-"));   // \. = a real dot -> 1-2-3
        System.out.println("1.2.3".replace(".", "-"));        // replace() is NOT regex -> 1-2-3
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R07_ReplaceSpaces.java) =======================
Before: [   Karthika    Krishna		M   ]
After : [Karthika Krishna M]
-----
1-2-3
1-2-3
*/
