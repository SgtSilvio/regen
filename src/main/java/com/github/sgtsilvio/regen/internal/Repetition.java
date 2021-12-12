package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.NotNull;

/**
 * <table>
 *   <tr>
 *       <th> Notation </th><th> minRepetitions </th><th> maxRepetitions </th><th> maxSize </th><th> randomize </th>
 *   </tr>
 *   <tr> <td> ?          </td><td> 0  </td><td> 1   </td><td> ??? </td><td> false </td> </tr>
 *   <tr> <td> *          </td><td> 0  </td><td> ??? </td><td> ??? </td><td> false </td> </tr>
 *   <tr> <td> +          </td><td> 1  </td><td> ??? </td><td> ??? </td><td> false </td> </tr>
 *   <tr> <td> {10}       </td><td> 10 </td><td> 10  </td><td> ??? </td><td> false </td> </tr>
 *   <tr> <td> {2,10}     </td><td> 2  </td><td> 10  </td><td> ??? </td><td> false </td> </tr>
 *   <tr> <td> {2,}       </td><td> 2  </td><td> ??? </td><td> ??? </td><td> false </td> </tr>
 *   <tr> <td> {10m24}    </td><td> 10 </td><td> 10  </td><td> 24  </td><td> false </td> </tr>
 *   <tr> <td> {10m24r}   </td><td> 10 </td><td> 10  </td><td> 24  </td><td> true  </td> </tr>
 *   <tr> <td> {2,m24}    </td><td> 2  </td><td> ??? </td><td> 24  </td><td> false </td> </tr>
 *   <tr> <td> {2,m24r}   </td><td> 2  </td><td> ??? </td><td> 24  </td><td> true  </td> </tr>
 *   <tr> <td> {2,10m24}  </td><td> 2  </td><td> 10  </td><td> 24  </td><td> false </td> </tr>
 * </table>
 *
 * @author Silvio Giebl
 */
public class Repetition implements RegexPart {

    private final @NotNull RegexPart part;
    private final int minRepetitions;
    private final int maxRepetitions;
    private final int maxSize;
    private final boolean randomize;
    private final int size;

    public Repetition(
            final @NotNull RegexPart part,
            final int minRepetitions,
            final int maxRepetitions,
            final int maxSize,
            final boolean randomize) {

        this.part = part;
        this.minRepetitions = minRepetitions;
        this.maxRepetitions = maxRepetitions;
        this.maxSize = maxSize;
        this.randomize = randomize;
        this.size = getSize(part, minRepetitions, maxRepetitions, maxSize);
    }

    public static int getSize(
            final @NotNull RegexPart part, final int minRepetitions, final int maxRepetitions, final int maxSize) {

        final int factor = ((maxRepetitions * (maxRepetitions + 1)) - ((minRepetitions - 1) * minRepetitions)) / 2;
        return Math.min(part.getSize() * factor, maxSize) + ((minRepetitions == 0) ? 1 : 0);
    }

    @Override
    public int getSize() {
        return size;
    }
}
