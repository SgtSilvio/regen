package com.github.sgtsilvio.regen;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
class Constant implements RegexPart {

    static final @NotNull Constant EMPTY = new Constant(new byte[0]);

    final byte @NotNull [] value;

    Constant(final byte @NotNull [] value) {
        this.value = value;
    }

    @Override
    public int getQuantity() {
        return 1;
    }
}
