import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * R12 — Greedy vs reluctant vs possessive quantifiers.
 *
 *   .*    GREEDY     : grab as much as possible, then give back only if needed
 *   .*?   RELUCTANT  : grab as little as possible, take more only if needed
 *   .*+   POSSESSIVE : grab as much as possible and NEVER give back
 *
 * Analogy: eating from a plate of idlis.
 *   Greedy takes the whole plate, then returns some if mom insists.
 *   Reluctant takes one at a time.
 *   Possessive takes the whole plate and refuses to return any.
 */
public class R12_GreedyVsReluctant {

    static void show(String label, String regex, String text) {
        Matcher m = Pattern.compile(regex).matcher(text);
        StringBuilder found = new StringBuilder();
        while (m.find()) found.append("[").append(m.group()).append("] ");
        System.out.printf("%-11s %-12s -> %s%n", label, regex,
                          found.length() == 0 ? "NO MATCH" : found.toString().trim());
    }

    public static void main(String[] args) {

        String html = "<b>Hello</b> and <b>Java</b>";
        System.out.println("Text: " + html);
        show("Greedy",     "<b>.*</b>",  html);
        show("Reluctant",  "<b>.*?</b>", html);
        show("Possessive", "<b>.*+</b>", html);   // .*+ eats "</b>" too and won't give it back

        String num = "aaa111";
        System.out.println("\nText: " + num);
        show("Greedy",    "a+",  num);
        show("Reluctant", "a+?", num);
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R12_GreedyVsReluctant.java) =======================
Text: <b>Hello</b> and <b>Java</b>
Greedy      <b>.*</b>    -> [<b>Hello</b> and <b>Java</b>]
Reluctant   <b>.*?</b>   -> [<b>Hello</b>] [<b>Java</b>]
Possessive  <b>.*+</b>   -> NO MATCH

Text: aaa111
Greedy      a+           -> [aaa]
Reluctant   a+?          -> [a] [a] [a]
*/
