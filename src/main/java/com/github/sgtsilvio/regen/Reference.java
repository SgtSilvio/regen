package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
class Reference implements RegexPart {

    private final int index;
    private final @NotNull String name;

    Reference(final int index, final @NotNull String name) {
        this.index = index;
        this.name = name;
    }

    @Override
    public int getQuantity() {
        return 1;
    }

    @Override
    public int generatedSize(final int index) {
        if (index != 0) {
            throw new IllegalArgumentException("index too big"); // TODO message
        }
        return 0; // TODO
    }

    @Override
    public int generate(final int index, final byte @NotNull [] bytes, final int end) {
        if (index != 0) {
            throw new IllegalArgumentException("index too big"); // TODO message
        }
        return end; // TODO
    }
}
