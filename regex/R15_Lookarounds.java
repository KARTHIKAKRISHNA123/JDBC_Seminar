import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * R15 — Lookarounds: check what is NEXT TO the match without including it.
 *
 *   X(?=Y)    positive lookahead  : X, only if followed by Y
 *   X(?!Y)    negative lookahead  : X, only if NOT followed by Y
 *   (?<=Y)X   positive lookbehind : X, only if preceded by Y
 *   (?<!Y)X   negative lookbehind : X, only if NOT preceded by Y
 *
 * "Zero-width": they check, but don't consume — the checked text is not part of group().
 */
public class R15_Lookarounds {

    static void findAll(String label, String regex, String text) {
        Matcher m = Pattern.compile(regex).matcher(text);
        StringBuilder sb = new StringBuilder();
        while (m.find()) sb.append(m.group()).append("  ");
        System.out.printf("%-22s %-20s -> %s%n", label, regex, sb.toString().trim());
    }

    public static void main(String[] args) {

        String prices = "Fees: ₹45000, Hostel: ₹12000, Laptop: $900, Books: 1500";
        System.out.println("Text: " + prices + "\n");

        findAll("Positive lookbehind", "(?<=₹)\\d+",    prices);   // rupee amounts, without ₹
        findAll("Negative lookbehind", "(?<![₹$\\d])\\d+", prices); // numbers with no currency
        findAll("Positive lookahead",  "\\w+(?=:)",     prices);   // words right before a colon

        String users = "admin, admin123, kk_admin, adminuser, kk";
        System.out.println("\nText: " + users + "\n");
        findAll("Negative lookahead", "\\b(?!admin)\\w+", users);  // names NOT starting with admin
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R15_Lookarounds.java) =======================
Text: Fees: ₹45000, Hostel: ₹12000, Laptop: $900, Books: 1500

Positive lookbehind    (?<=₹)\d+            -> 45000  12000
Negative lookbehind    (?<![₹$\d])\d+       -> 1500
Positive lookahead     \w+(?=:)             -> Fees  Hostel  Laptop  Books

Text: admin, admin123, kk_admin, adminuser, kk

Negative lookahead     \b(?!admin)\w+       -> kk_admin  kk
*/
