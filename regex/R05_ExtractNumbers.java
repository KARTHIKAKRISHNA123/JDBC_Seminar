import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * R05 — Pull every number out of a sentence.
 *
 * Regex:  \d+(\.\d+)?
 *   \d+         one or more digits
 *   (\.\d+)?    optionally: a dot followed by digits (decimal part)
 *
 * Concepts: Matcher.find() in a while-loop visits every match, left to right.
 */
public class R05_ExtractNumbers {
    public static void main(String[] args) {

        String text = "Java 17 and Java 21 are LTS. Python 3.12 is out. Our CGPA target is 8.75!";

        Matcher m = Pattern.compile("\\d+(\\.\\d+)?").matcher(text);

        List<String> numbers = new ArrayList<>();
        while (m.find()) {
            numbers.add(m.group());
            System.out.println("Found '" + m.group() + "' at index " + m.start() + " to " + m.end());
        }
        System.out.println("All numbers: " + numbers);
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R05_ExtractNumbers.java) =======================
Found '17' at index 5 to 7
Found '21' at index 17 to 19
Found '3.12' at index 36 to 40
Found '8.75' at index 68 to 72
All numbers: [17, 21, 3.12, 8.75]
*/
