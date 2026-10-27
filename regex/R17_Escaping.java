import java.util.regex.Pattern;

/**
 * R17 — Escaping: the TWO layers of backslashes.
 *
 *   You want the regex      \d          (a digit)
 *   Java string must hold   \d
 *   In Java source you type "\\d"       because "\\" in Java = one real backslash
 *
 *   You want a literal dot  \.          regex
 *   In Java source:         "\\."
 *
 *   You want a literal \    \\          regex
 *   In Java source:         "\\\\"      (four!)
 *
 * Shortcut: Pattern.quote(text) escapes everything for you.
 */
public class R17_Escaping {
    public static void main(String[] args) {

        String javaSource = "\\d{3}";
        System.out.println("Typed in Java source : \"\\\\d{3}\"");
        System.out.println("String really holds  : " + javaSource);
        System.out.println("Length of that string: " + javaSource.length() + "  (\\ d { 3 } -> 5 chars)");

        System.out.println();
        System.out.println("\"file.txt\".matches(\"file.txt\")   = " + "file.txt".matches("file.txt"));
        System.out.println("\"fileXtxt\".matches(\"file.txt\")   = " + "fileXtxt".matches("file.txt") + "   <-- oops, . matched X");
        System.out.println("\"fileXtxt\".matches(\"file\\\\.txt\") = " + "fileXtxt".matches("file\\.txt"));

        System.out.println();
        String path = "C:\\Users\\KK";                         // a Windows path
        System.out.println("Path: " + path);
        System.out.println("Split on backslash: " + String.join(" | ", path.split("\\\\")));

        System.out.println();
        String userText = "1+1=2?";
        String safe = Pattern.quote(userText);                // \Q1+1=2?\E
        System.out.println("Pattern.quote(\"1+1=2?\") = " + safe);
        System.out.println("matches itself? " + userText.matches(safe));
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R17_Escaping.java) =======================
Typed in Java source : "\\d{3}"
String really holds  : \d{3}
Length of that string: 5  (\ d { 3 } -> 5 chars)

"file.txt".matches("file.txt")   = true
"fileXtxt".matches("file.txt")   = true   <-- oops, . matched X
"fileXtxt".matches("file\\.txt") = false

Path: C:\Users\KK
Split on backslash: C: | Users | KK

Pattern.quote("1+1=2?") = \Q1+1=2?\E
matches itself? true
*/
