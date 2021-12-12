package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
public class Alternation implements RegexPart {

    private final @NotNull RegexPart @NotNull [] alternatives;
    private final int size;

    public Alternation(final @NotNull RegexPart @NotNull [] alternatives) {
        this.alternatives = alternatives;
        this.size = getSize(alternatives);
    }

    private static int getSize(final @NotNull RegexPart @NotNull [] alternatives) {
        int size = 0;
        for (final RegexPart alternative : alternatives) {
            size += alternative.getSize();
        }
        return size;
    }

    @Override
    public int getSize() {
        return size;
    }
}
