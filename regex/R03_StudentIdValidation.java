/**
 * R03 — Validate a student register ID like  CSE-2023-045
 *
 * Rule we invent for the demo:
 *   department : CSE, ECE, EEE, IT or MECH
 *   year       : 4 digits starting with 20
 *   roll no.   : exactly 3 digits
 *
 * Regex:  (CSE|ECE|EEE|IT|MECH)-20\d{2}-\d{3}
 *   (A|B|C)   alternation: one of the listed words
 *   -         a literal hyphen
 *   20\d{2}   "20" followed by two digits
 *   \d{3}     exactly three digits
 */
public class R03_StudentIdValidation {
    public static void main(String[] args) {

        String regex = "(CSE|ECE|EEE|IT|MECH)-20\\d{2}-\\d{3}";

        String[] inputs = {
            "CSE-2023-045",
            "MECH-2024-001",
            "IT-2022-120",
            "cse-2023-045",   // lowercase department
            "CSE-1999-045",   // year does not start with 20
            "CSE-2023-45",    // roll number has 2 digits
            "CIVIL-2023-045", // department not in the list
            "CSE2023045"      // missing hyphens
        };

        for (String id : inputs) {
            System.out.printf("%-15s -> %s%n", id, id.matches(regex) ? "VALID" : "invalid");
        }
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R03_StudentIdValidation.java) =======================
CSE-2023-045    -> VALID
MECH-2024-001   -> VALID
IT-2022-120     -> VALID
cse-2023-045    -> invalid
CSE-1999-045    -> invalid
CSE-2023-45     -> invalid
CIVIL-2023-045  -> invalid
CSE2023045      -> invalid
*/
