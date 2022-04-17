package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
class Concatenation implements RegexPart {

    final @NotNull RegexPart @NotNull [] parts;
    private final int quantity;

    Concatenation(final @NotNull RegexPart @NotNull [] parts) {
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

    @Override
    public int generatedSize(int index) {
        int generatedSize = 0;
        for (int i = parts.length - 1; i >= 0; i--) {
            final RegexPart part = parts[i];
            final int partQuantity = part.getQuantity();
            generatedSize += part.generatedSize(index % partQuantity);
            index /= partQuantity;
        }
        if (index != 0) {
            throw new IllegalArgumentException("index too big"); // TODO message
        }
        return generatedSize;
    }

    @Override
    public int generate(int index, final byte @NotNull [] bytes, int end) {
        for (int i = parts.length - 1; i >= 0; i--) {
            final RegexPart part = parts[i];
            final int partQuantity = part.getQuantity();
            end = part.generate(index % partQuantity, bytes, end);
            index /= partQuantity;
        }
        if (index != 0) {
            throw new IllegalArgumentException("index too big"); // TODO message
        }
        return end;
    }
}
