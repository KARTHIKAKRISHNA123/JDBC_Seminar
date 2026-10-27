import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * R06 — Find every email-like string inside a paragraph.
 *
 * Same pattern as R02, but we use find() (search inside) instead of matches() (whole string).
 * Pattern.CASE_INSENSITIVE lets us write [a-z] and still match capitals.
 */
public class R06_ExtractEmails {
    public static void main(String[] args) {

        String text = """
            Contact the placement cell at placement@aucet.edu.in or the
            HOD at HOD.CSE@aucet.edu.in. For internships, mail
            kk+intern@example.com. Ignore broken ones like test@site or @nobody.com.
            """;

        Pattern email = Pattern.compile(
            "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}", Pattern.CASE_INSENSITIVE);

        Matcher m = email.matcher(text);
        int count = 0;
        while (m.find()) {
            count++;
            System.out.println(count + ". " + m.group());
        }
        System.out.println("Total: " + count);
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R06_ExtractEmails.java) =======================
1. placement@aucet.edu.in
2. HOD.CSE@aucet.edu.in
3. kk+intern@example.com
Total: 3
*/
