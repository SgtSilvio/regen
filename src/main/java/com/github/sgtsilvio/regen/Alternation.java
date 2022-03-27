package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
class Alternation implements RegexPart {

    final @NotNull RegexPart @NotNull [] alternatives;
    private final int quantity;

    Alternation(final @NotNull RegexPart @NotNull [] alternatives) {
        this.alternatives = alternatives;
        this.quantity = calculateQuantity(alternatives);
    }

    private static int calculateQuantity(final @NotNull RegexPart @NotNull [] alternatives) {
        int quantity = 0;
        for (final RegexPart alternative : alternatives) {
            quantity += alternative.getQuantity();
            if (quantity < 0) { // overflow
                return Integer.MAX_VALUE;
            }
        }
        return quantity;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }
}
