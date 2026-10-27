import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * R10 — "Looks like a date" (regex) vs "is a real calendar date" (java.time).
 *
 * Regex:  \d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\d|3[01])
 *   \d{4}                 year
 *   (0[1-9]|1[0-2])       month 01..12
 *   (0[1-9]|[12]\d|3[01]) day 01..31
 *
 * Regex can't know that February has 28/29 days or that April has 30.
 * Lesson: use regex for FORMAT, use the right library for MEANING.
 */
public class R10_DateFormatVsRealDate {

    static final String DATE_FORMAT = "\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])";

    // uuuu = year; STRICT = reject impossible dates instead of "fixing" them
    static final DateTimeFormatter STRICT =
        DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    static boolean isRealDate(String s) {
        try {
            LocalDate.parse(s, STRICT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static void main(String[] args) {

        String[] inputs = { "2026-10-27", "2024-02-29", "2026-02-29", "2026-04-31",
                            "2026-13-01", "26-10-2026", "2026/10/27" };

        System.out.printf("%-12s %-14s %s%n", "INPUT", "FORMAT OK?", "REAL DATE?");
        for (String d : inputs) {
            System.out.printf("%-12s %-14s %s%n", d, d.matches(DATE_FORMAT), isRealDate(d));
        }
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R10_DateFormatVsRealDate.java) =======================
INPUT        FORMAT OK?     REAL DATE?
2026-10-27   true           true
2024-02-29   true           true
2026-02-29   true           false
2026-04-31   true           false
2026-13-01   false          false
26-10-2026   false          false
2026/10/27   false          false
*/
