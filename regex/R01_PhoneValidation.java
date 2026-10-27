/**
 * R01 — Validate a 10-digit phone number.
 *
 * Regex:  [6-9][0-9]{9}
 *   [6-9]   first digit must be 6, 7, 8 or 9 (Indian mobile numbers)
 *   [0-9]   any one digit
 *   {9}     exactly 9 more of them  -> 10 digits total
 *
 * String.matches() checks the WHOLE string, so no ^ $ anchors are needed here.
 * Run:  java R01_PhoneValidation.java
 */
public class R01_PhoneValidation {
    public static void main(String[] args) {

        String regex = "[6-9][0-9]{9}";

        String[] inputs = {
            "9876543210",     // valid
            "6123456789",     // valid
            "5876543210",     // starts with 5
            "987654321",      // only 9 digits
            "98765432101",    // 11 digits
            "98765 43210",    // contains a space
            "+919876543210"   // has country code
        };

        for (String phone : inputs) {
            System.out.printf("%-15s -> %s%n", phone, phone.matches(regex) ? "VALID" : "invalid");
        }
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R01_PhoneValidation.java) =======================
9876543210      -> VALID
6123456789      -> VALID
5876543210      -> invalid
987654321       -> invalid
98765432101     -> invalid
98765 43210     -> invalid
+919876543210   -> invalid
*/
