package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.NotNull;

/**
 * <table>
 *   <tr>
 *     <th> Notation </th><th> minRepetitions </th><th> maxRepetitions </th><th> maxQuantity </th><th> randomize </th>
 *   </tr>
 *   <tr> <td> ?          </td><td> 0  </td><td> 1                 </td><td> Integer.MAX_VALUE </td><td> false </td> </tr>
 *   <tr> <td> *          </td><td> 0  </td><td> Integer.MAX_VALUE </td><td> Integer.MAX_VALUE </td><td> false </td> </tr>
 *   <tr> <td> +          </td><td> 1  </td><td> Integer.MAX_VALUE </td><td> Integer.MAX_VALUE </td><td> false </td> </tr>
 *   <tr> <td> {10}       </td><td> 10 </td><td> 10                </td><td> Integer.MAX_VALUE </td><td> false </td> </tr>
 *   <tr> <td> {2,10}     </td><td> 2  </td><td> 10                </td><td> Integer.MAX_VALUE </td><td> false </td> </tr>
 *   <tr> <td> {2,}       </td><td> 2  </td><td> Integer.MAX_VALUE </td><td> Integer.MAX_VALUE </td><td> false </td> </tr>
 *   <tr> <td> {10m24}    </td><td> 10 </td><td> 10                </td><td> 24                </td><td> false </td> </tr>
 *   <tr> <td> {10m24r}   </td><td> 10 </td><td> 10                </td><td> 24                </td><td> true  </td> </tr>
 *   <tr> <td> {2,m24}    </td><td> 2  </td><td> Integer.MAX_VALUE </td><td> 24                </td><td> false </td> </tr>
 *   <tr> <td> {2,10m24}  </td><td> 2  </td><td> 10                </td><td> 24                </td><td> false </td> </tr>
 * </table>
 *
 * @author Silvio Giebl
 */
public class Repetition implements RegexPart {

    static @NotNull Repetition neverOrOnce(final @NotNull RegexPart part) {
        return new Repetition(part, 0, 1, Integer.MAX_VALUE, false);
    }

    static @NotNull Repetition neverOrMore(final @NotNull RegexPart part) {
        return new Repetition(part, 0, Integer.MAX_VALUE, Integer.MAX_VALUE, false);
    }

    static @NotNull Repetition onceOrMore(final @NotNull RegexPart part) {
        return new Repetition(part, 1, Integer.MAX_VALUE, Integer.MAX_VALUE, false);
    }

    static @NotNull Repetition nTimes(
            final @NotNull RegexPart part, final int n, final int maxQuantity, final boolean randomize) {
        return new Repetition(part, n, n, maxQuantity, randomize);
    }

    static @NotNull Repetition nToMTimes(
            final @NotNull RegexPart part, final int n, final int m, final int maxQuantity) {
        return new Repetition(part, n, m, maxQuantity, false);
    }

    private final @NotNull RegexPart part;
    private final int minRepetitions;
    private final int maxRepetitions;
    private final int maxQuantity;
    private final boolean randomize;
    private final int quantity;

    // validation if maxSize != Integer.MAX_VALUE && maxSize > getSize() warn
    private Repetition(
            final @NotNull RegexPart part,
            final int minRepetitions,
            final int maxRepetitions,
            final int maxQuantity,
            final boolean randomize) {

        this.part = part;
        this.minRepetitions = minRepetitions;
        this.maxRepetitions = maxRepetitions;
        this.maxQuantity = maxQuantity;
        this.randomize = randomize;
        this.quantity = calculateQuantity(part, minRepetitions, maxRepetitions, maxQuantity);
    }

    private static int calculateQuantity(
            final @NotNull RegexPart part, final int minRepetitions, final int maxRepetitions, final int maxQuantity) {

        if (maxRepetitions == Integer.MAX_VALUE) {
            return maxQuantity;
        }
        final int partQuantity = part.getQuantity();
        int pow = (int) Math.pow(partQuantity, minRepetitions);
        int sum = pow;
        for (int i = minRepetitions; i < maxRepetitions; i++) {
            final long product = (long) pow * partQuantity;
            if (product > maxQuantity) {
                return maxQuantity;
            }
            pow = (int) product;
            sum += pow;
            if (sum < 0) {
                return maxQuantity;
            }
        }
        return Math.min(sum, maxQuantity);
    }

    @Override
    public int getQuantity() {
        return quantity;
    }
}
