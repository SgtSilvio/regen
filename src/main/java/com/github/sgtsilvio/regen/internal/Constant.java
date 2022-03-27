package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
public class Constant implements RegexPart {

    static final @NotNull Constant EMPTY = new Constant(new byte[0]);

    final byte @NotNull [] value;

    public Constant(final byte @NotNull [] value) {
        this.value = value;
    }

    @Override
    public int getQuantity() {
        return 1;
    }
}
