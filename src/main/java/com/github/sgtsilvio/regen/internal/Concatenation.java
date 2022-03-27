package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
public class Concatenation implements RegexPart {

    final @NotNull RegexPart @NotNull [] parts;
    private final int quantity;

    public Concatenation(final @NotNull RegexPart @NotNull [] parts) {
        this.parts = parts;
        this.quantity = calculateQuantity(parts);
    }

    private static int calculateQuantity(final @NotNull RegexPart @NotNull [] parts) {
        int quantity = 1;
        for (final RegexPart part : parts) {
            quantity *= part.getQuantity();
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
