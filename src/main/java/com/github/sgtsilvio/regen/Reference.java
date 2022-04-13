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
}
