package com.github.sgtsilvio.regen.internal;

import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
public class Constant implements RegexPart {

    private final byte @NotNull [] value;

    public Constant(final byte @NotNull [] value) {
        this.value = value;
    }

    @Override
    public int getQuantity() {
        return 0;
    }
}
