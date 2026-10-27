/**
 * R19 — When regex goes wrong: catastrophic backtracking (ReDoS).
 *
 * Nested quantifiers like (a+)+ let the engine split "aaaa" between the inner and
 * outer + in many ways. When the match finally FAILS (no 'b' at the end), a
 * backtracking engine tries every split before giving up -> work roughly doubles
 * with each extra 'a'.
 *
 * Java 9+ quietly optimises the textbook case (a+)+b, so it stays fast in Java 21.
 * Add one backreference, (a+)+\1?b, and that protection switches off. Engines in
 * other languages (JavaScript, Python) blow up even on the textbook case.
 *
 * Real incident: in July 2019 one badly written regex pushed Cloudflare's servers
 * to 100% CPU and took much of their network down for about 27 minutes.
 *
 * Fix: remove the nesting. a+b accepts exactly the same strings and stays fast.
 * n stops at 26 so the demo finishes in a few seconds (timings vary by laptop).
 */
public class R19_CatastrophicBacktracking {

    static long timeMs(String regex, String input) {
        long start = System.nanoTime();
        input.matches(regex);
        return (System.nanoTime() - start) / 1_000_000;
    }

    public static void main(String[] args) {

        System.out.printf("%-4s %-16s %-16s %s%n", "n", "(a+)+b", "(a+)+\\1?b", "a+b  (ms)");
        for (int n = 16; n <= 26; n += 2) {
            String input = "a".repeat(n) + "!";            // no 'b' -> every attempt fails
            System.out.printf("%-4d %-16d %-16d %d%n", n,
                    timeMs("(a+)+b", input),
                    timeMs("(a+)+\\1?b", input),
                    timeMs("a+b", input));
        }
        System.out.println("\nMiddle column keeps multiplying as n grows: that's exponential time.");
    }
}

/* ======================= EXPECTED OUTPUT (run with:  java R19_CatastrophicBacktracking.java) =======================
n    (a+)+b           (a+)+\1?b        a+b  (ms)
16   0                10               0
18   0                34               0
20   0                39               0
22   0                140              0
24   0                500              0
26   0                1610             0

Middle column keeps multiplying as n grows: that's exponential time.

(Your millisecond numbers will differ; what matters is the middle column growing fast
 while the first and last columns stay near 0.)
*/
