/**
 * R18 — replaceFirst() vs replaceAll(), and masking sensitive data.
 *
 *   replaceFirst(regex, repl)  only the first match
 *   replaceAll(regex, repl)    every match
 *   $1, $2 ...                 insert captured groups into the replacement
 *
 * Real use: masking a phone number before printing it in logs or on a receipt.
 */
public class R18_ReplaceFirstAndAll {
    public static void main(String[] args) {

        String s = "Java Java Java";
        System.out.println("replaceFirst: " + s.replaceFirst("Java", "JDBC"));
        System.out.println("replaceAll  : " + s.replaceAll("Java", "JDBC"));

        // Mask a phone number: keep first 2 and last 2 digits
        String phone = "9876543210";
        String masked = phone.replaceAll("(\\d{2})\\d{6}(\\d{2})", "$1******$2");
        System.out.println("\nPhone : " + phone);
        System.out.println("Masked: " + masked);

        // Mask the user part of an email: kk.student@example.com -> k*********@example.com
        String email = "kk.student@example.com";
        String hidden = email.replaceAll("(?<=^.)[^@]*(?=@)", "*".repeat(9));
        System.out.println("\nEmail : " + email);
        System.out.println("Hidden: " + hidden);
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R18_ReplaceFirstAndAll.java) =======================
replaceFirst: JDBC Java Java
replaceAll  : JDBC JDBC JDBC

Phone : 9876543210
Masked: 98******10

Email : kk.student@example.com
Hidden: k*********@example.com
*/
