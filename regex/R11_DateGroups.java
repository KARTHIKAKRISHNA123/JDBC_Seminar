import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * R11 — Capturing groups: pull year, month and day out of a date.
 *
 *   ( ... )            numbered group:  group(1), group(2), group(3)
 *   (?<name> ... )     NAMED group:     group("year") — easier to read
 *   group(0)           the whole match
 *   "$3/$2/$1"         use groups inside a replacement to reformat text
 */
public class R11_DateGroups {
    public static void main(String[] args) {

        // 1) Numbered groups
        Matcher m = Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})").matcher("2026-10-27");
        if (m.matches()) {
            System.out.println("group(0) whole : " + m.group(0));
            System.out.println("group(1) year  : " + m.group(1));
            System.out.println("group(2) month : " + m.group(2));
            System.out.println("group(3) day   : " + m.group(3));
            System.out.println("groupCount()   : " + m.groupCount());
        }

        // 2) Named groups
        Matcher n = Pattern.compile("(?<year>\\d{4})-(?<month>\\d{2})-(?<day>\\d{2})")
                           .matcher("Seminar on 2026-11-05 at 10 AM");
        if (n.find()) {
            System.out.println("\nNamed -> day=" + n.group("day")
                               + ", month=" + n.group("month") + ", year=" + n.group("year"));
        }

        // 3) Reformat YYYY-MM-DD into DD/MM/YYYY (Indian style) with $ references
        String log = "Joined 2023-09-04, internship 2026-06-15, seminar 2026-11-05";
        String indian = log.replaceAll("(\\d{4})-(\\d{2})-(\\d{2})", "$3/$2/$1");
        System.out.println("\nBefore: " + log);
        System.out.println("After : " + indian);
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R11_DateGroups.java) =======================
group(0) whole : 2026-10-27
group(1) year  : 2026
group(2) month : 10
group(3) day   : 27
groupCount()   : 3

Named -> day=05, month=11, year=2026

Before: Joined 2023-09-04, internship 2026-06-15, seminar 2026-11-05
After : Joined 04/09/2023, internship 15/06/2026, seminar 05/11/2026
*/
