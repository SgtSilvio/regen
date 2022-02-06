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

    private final @NotNull RegexPart part;
    private final int minRepetitions;
    private final int maxRepetitions;
    private final int maxQuantity;
    private final boolean randomize;
    private final int quantity;

    // validation if maxSize != Integer.MAX_VALUE && maxSize > getSize() warn
    public Repetition(
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

        // factor can not overflow because Integer.MAX_VALUE * (Integer.MAX_VALUE + 1) < Long.MAX_VALUE
        final long factor =
                ((maxRepetitions * ((long) maxRepetitions + 1)) - ((long) (minRepetitions - 1) * minRepetitions)) / 2;
        if (factor >= Integer.MAX_VALUE) {
            return maxQuantity;
        }
        // size can not overflow because Integer.MAX_VALUE * (Integer.MAX_VALUE - 1) + 1 < Long.MAX_VALUE
        final long quantity = part.getQuantity() * factor + ((minRepetitions == 0) ? 1 : 0);
        // cast to int is safe as maxSize <= Integer.MAX_VALUE
        return (int) Math.min(quantity, maxQuantity);
    }

    @Override
    public int getQuantity() {
        return quantity;
    }
}
