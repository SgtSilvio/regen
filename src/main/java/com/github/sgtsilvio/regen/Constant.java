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

    @Override
    public int generatedSize(final int index) {
        if (index != 0) {
            throw new IllegalArgumentException("index too big"); // TODO message
        }
        return value.length;
    }

    @Override
    public int generate(final int index, final byte @NotNull [] bytes, final int end) {
        if (index != 0) {
            throw new IllegalArgumentException("index too big"); // TODO message
        }
        System.arraycopy(value, 0, bytes, end - value.length + 1, value.length);
        return end - value.length;
    }
}
